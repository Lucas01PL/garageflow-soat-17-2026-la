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
  type    = string
  default = "1.32"
}

variable "node_group_name" {
  type    = string
  default = "garageflow-node-group"
}

variable "admin_role_arn" {
  description = "ARN of the IAM role that will administer the EKS cluster"
  type        = string
}