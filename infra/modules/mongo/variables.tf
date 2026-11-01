variable "name_prefix" {
  description = "Prefixo dos nomes dos recursos (ex.: garageflow-dev)"
  type        = string
}

variable "vpc_id" {
  description = "ID da VPC onde a instância e o security group serão criados"
  type        = string
}

variable "subnet_id" {
  description = "Sub-rede da instância. Precisa dar saída à internet (para baixar a imagem do Mongo)"
  type        = string
}

variable "nodes_security_group_id" {
  description = "Security group dos nós do EKS. É o único autorizado a acessar a porta 27017"
  type        = string
}

variable "instance_type" {
  description = "Tipo da instância (x86)"
  type        = string
  default     = "t3.small"
}

variable "ssm_parameter_name" {
  description = "Nome do parâmetro do SSM que guarda a senha do Mongo (a pipeline lê dele)"
  type        = string
}
