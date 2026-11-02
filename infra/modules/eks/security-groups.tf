resource "aws_security_group" "eks_nodes_security_group" {
  name        = var.eks_nodes_security_group_name
  description = "Security Group para as instancias EC2 dos nos do EKS"
  vpc_id      = var.vpc_id

  tags = {
    Name = var.eks_nodes_security_group_name
  }
}

# Saída — nodes podem acessar a internet via NAT Gateway
resource "aws_vpc_security_group_egress_rule" "eks_nodes_all_egress" {
  security_group_id = aws_security_group.eks_nodes_security_group.id

  cidr_ipv4   = "0.0.0.0/0"
  ip_protocol = "-1"
}

# Comunicação entre os próprios nodes do cluster EKS
resource "aws_vpc_security_group_ingress_rule" "eks_nodes_self" {
  security_group_id = aws_security_group.eks_nodes_security_group.id

  referenced_security_group_id = aws_security_group.eks_nodes_security_group.id

  ip_protocol = "-1"
}

# Entrada do ALB — permite que o ALB encaminhe requisições HTTP para os nós
resource "aws_vpc_security_group_ingress_rule" "eks_nodes_from_alb" {
  #count             = var.alb_security_group_id != "" ? 1 : 0
  security_group_id = aws_security_group.eks_nodes_security_group.id

  referenced_security_group_id = var.alb_security_group_id
  ip_protocol                  = "tcp"
  from_port                    = 0
  to_port                      = 65535
}