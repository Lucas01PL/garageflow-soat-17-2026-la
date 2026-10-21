provider "aws" {
  region = var.aws_region

  # Tags aplicadas automaticamente em TODOS os recursos (VPC, EKS, ALB, IAM...).
  # Servem para achar o que sobrou na AWS depois do destroy (leftover).
  default_tags {
    tags = {
      Project     = "garageflow"
      Environment = var.environment
    }
  }
}

# 1. Rede AWS: VPC, subnets públicas/privadas, Internet Gateway e NAT Gateway
module "vpc" {
  source = "../../modules/vpc"
}

# 2. Application Load Balancer público provisionado via Terraform
module "alb" {
  source = "../../modules/alb"

  alb_name          = "${var.project_name}-${var.environment}-alb"
  vpc_id            = module.vpc.vpc_id
  public_subnet_ids = module.vpc.public_subnet_ids
  target_port       = 30080 # NodePort padrão da aplicação no Kubernetes

  # A app usa context-path /api e só libera rotas públicas específicas.
  # A raiz "/" devolve 403/404 e deixaria os nós "unhealthy". Se o actuator
  # entrar na app, trocar por /api/actuator/health.
  health_check_path = "/api/openapi"

  tags = {
    Project     = var.project_name
    Environment = var.environment
  }
}

# 3. Cluster Kubernetes: EKS e Managed Node Group
module "eks" {
  source = "../../modules/eks"

  cluster_name       = "${var.project_name}-${var.environment}"
  kubernetes_version = var.kubernetes_version

  # Dados fornecidos pelo módulo VPC (subnets públicas sem NAT)
  vpc_id     = module.vpc.vpc_id
  subnet_ids = module.vpc.public_subnet_ids

  # Libera tráfego vindo do Security Group do ALB para os nós
  alb_security_group_id = module.alb.alb_security_group_id

  # Role IAM autorizada a administrar o cluster
  admin_role_arn = var.admin_role_arn
}

# 4. Conecta o Target Group do ALB ao Auto Scaling Group dos nós do EKS
# Isso garante que as instâncias EC2 do node group recebam o tráfego do ALB automaticamente
resource "aws_autoscaling_attachment" "eks_nodes" {
  autoscaling_group_name = module.eks.node_group_autoscaling_group_names[0][0]
  lb_target_group_arn    = module.alb.target_group_arn
}
