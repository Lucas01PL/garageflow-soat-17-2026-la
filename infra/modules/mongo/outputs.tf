output "private_ip" {
  description = "IP privado da instância do Mongo (a app no EKS conecta por ele)"
  value       = aws_instance.mongo.private_ip
}

output "instance_name" {
  description = "Valor da tag Name da instância (a pipeline a localiza por ele)"
  value       = "${var.name_prefix}-mongo"
}

output "ssm_parameter_name" {
  description = "Nome do parâmetro do SSM com a senha do Mongo"
  value       = aws_ssm_parameter.mongo_password.name
}

output "security_group_id" {
  description = "Security group do Mongo"
  value       = aws_security_group.mongo.id
}
