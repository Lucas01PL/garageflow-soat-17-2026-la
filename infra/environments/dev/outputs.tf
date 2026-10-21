output "vpc_id" {
  description = "ID da VPC criada"
  value       = module.vpc.vpc_id
}

output "public_subnet_ids" {
  description = "Subnets públicas para o EKS e o ALB"
  value       = module.vpc.public_subnet_ids
}

output "cluster_name" {
  description = "Nome do cluster EKS"
  value       = module.eks.cluster_name
}

output "cluster_endpoint" {
  description = "Endpoint da API do cluster EKS"
  value       = module.eks.cluster_endpoint
}

output "alb_dns_name" {
  description = "URL / DNS público do Application Load Balancer gerado na AWS"
  value       = module.alb.alb_dns_name
}

output "alb_arn" {
  description = "ARN do Application Load Balancer"
  value       = module.alb.alb_arn
}

output "alb_target_group_arn" {
  description = "ARN do Target Group associado aos nós do EKS"
  value       = module.alb.target_group_arn
}

output "mongo_private_ip" {
  description = "IP privado da instância do MongoDB (acessível só de dentro da VPC)"
  value       = module.mongo.private_ip
}

output "mongo_ssm_parameter" {
  description = "Nome do parâmetro do SSM com a senha do MongoDB"
  value       = module.mongo.ssm_parameter_name
}

