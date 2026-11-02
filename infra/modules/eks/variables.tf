variable "aws_region" {
  type    = string
  default = "us-east-1"
}

variable "cluster_name" {
  description = "Name of the EKS cluster"
  type        = string
  default     = "garageflow-eks-cluster"
}

variable "eks_node_role_name" {
  type    = string
  default = "garageflow-eks-node-role"
}

variable "eks_nodes_security_group_name" {
  type    = string
  default = "garageflow-eks-node-security-group"
}

variable "kubernetes_version" {
  type    = string
  default = "1.36"
}

# Lista de IDs das subnets onde os nodes EKS serão provisionados.
# Devem ser subnets PRIVADAS para segurança.
variable "subnet_ids" {
  description = "List of private subnet IDs for EKS nodes and control plane"
  type        = list(string)
}

variable "vpc_id" {
  description = "ID of the VPC where the EKS cluster will be created"
  type        = string
}

variable "node_group_name" {
  type    = string
  default = "garageflow-node-group"
}

variable "admin_role_arn" {
  description = "ARN of the IAM role that will administer the EKS cluster"
  type        = string
}

variable "alb_security_group_id" {
  description = "ID do Security Group do ALB para permitir tráfego de entrada nos nós"
  type        = string
  default     = ""
}