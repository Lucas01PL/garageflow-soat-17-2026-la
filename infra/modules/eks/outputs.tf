output "cluster_name" {
  description = "Nome do cluster EKS"
  value       = aws_eks_cluster.this.name
}

output "cluster_endpoint" {
  description = "Endpoint da API do cluster EKS"
  value       = aws_eks_cluster.this.endpoint
}

output "cluster_certificate_authority_data" {
  description = "Dados da autoridade certificadora (CA) do cluster"
  value       = aws_eks_cluster.this.certificate_authority[0].data
}

output "cluster_security_group_id" {
  description = "ID do Security Group principal do cluster EKS"
  value       = aws_eks_cluster.this.vpc_config[0].cluster_security_group_id
}

output "node_security_group_id" {
  description = "ID do Security Group das instâncias dos nós"
  value       = aws_security_group.eks_nodes_security_group.id
}

output "node_group_autoscaling_group_names" {
  description = "Nomes dos Auto Scaling Groups associados ao Managed Node Group"
  value       = aws_eks_node_group.main.resources[*].autoscaling_groups[*].name
}

