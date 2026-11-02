variable "alb_name" {
  description = "Nome do Application Load Balancer"
  type        = string
  default     = "garageflow-alb"
}

variable "vpc_id" {
  description = "ID da VPC onde o ALB e o Target Group serão criados"
  type        = string
}

variable "public_subnet_ids" {
  description = "Lista de subnets públicas onde o ALB será provisionado (mínimo 2 AZs)"
  type        = list(string)
}

variable "target_port" {
  description = "Porta de destino no Target Group (NodePort do serviço no Kubernetes, ex: 30080 ou 80)"
  type        = number
  default     = 80
}

variable "target_protocol" {
  description = "Protocolo de destino no Target Group"
  type        = string
  default     = "HTTP"
}

variable "health_check_path" {
  description = "Caminho do health check no Target Group"
  type        = string
  default     = "/"
}

variable "tags" {
  description = "Tags para os recursos do ALB"
  type        = map(string)
  default     = {}
}

