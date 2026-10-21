variable "aws_region" {
  type    = string
  default = "us-east-1"
}

variable "project_name" {
  description = "Name of the project, used to compose resource names"
  type        = string
  default     = "garageflow"
}

variable "environment" {
  description = "Deployment environment (e.g. dev, staging, prod)"
  type        = string
  default     = "dev"
}

variable "kubernetes_version" {
  description = "Versão do Kubernetes no EKS. Use uma em suporte PADRÃO (1.33 e anteriores estão em suporte estendido, que custa bem mais)"
  type        = string
  default     = "1.36"
}

variable "node_group_name" {
  type    = string
  default = "garageflow-node-group"
}

variable "admin_role_arn" {
  description = "ARN of the IAM role that will administer the EKS cluster"
  type        = string
}