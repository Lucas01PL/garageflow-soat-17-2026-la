output "vpc_id" {
  description = "ID da VPC"
  value       = aws_vpc.vpc_main.id
}

output "public_subnet_ids" {
  description = "IDs das subnets públicas em 2 AZs diferentes (requisito obrigatório para o ALB)"
  value = [
    aws_subnet.public_subnet_a.id,
    aws_subnet.public_subnet_b.id,
  ]
}

output "subnet_ids" {
  description = "Lista com todas as subnets ativas da VPC"
  value = [
    aws_subnet.public_subnet_a.id,
    aws_subnet.public_subnet_b.id,
  ]
}