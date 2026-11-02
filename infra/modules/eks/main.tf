resource "aws_eks_cluster" "this" {

  name = var.cluster_name

  role_arn = aws_iam_role.eks_cluster_role.arn

  version = var.kubernetes_version

  vpc_config {
    subnet_ids = var.subnet_ids

    endpoint_private_access = true
    endpoint_public_access  = true
  }

  access_config {
    authentication_mode = "API"

    # false: o único admin é o definido em aws_eks_access_entry.admin. Com o
    # padrão (true) a AWS cria sozinha uma entrada para quem criou o cluster e
    # a entrada explícita abaixo falharia com ResourceInUseException.
    bootstrap_cluster_creator_admin_permissions = false
  }

  depends_on = [
    aws_iam_role_policy_attachment.eks_cluster_policy
  ]
}

resource "aws_launch_template" "nodes" {
  name_prefix = "${var.cluster_name}-node-template-"
  description = "Launch template para o node group do EKS vinculando os security groups"

  # Security groups dos nós. O IP público já vem das sub-redes
  # (map_public_ip_on_launch = true), por isso não é preciso declarar
  # network_interfaces aqui (o que o EKS pode rejeitar).
  vpc_security_group_ids = [
    aws_security_group.eks_nodes_security_group.id,
    aws_eks_cluster.this.vpc_config[0].cluster_security_group_id
  ]

  tag_specifications {
    resource_type = "instance"
    tags = {
      Name = "${var.cluster_name}-node"
    }
  }

  lifecycle {
    create_before_destroy = true
  }
}

resource "aws_eks_node_group" "main" {

  cluster_name = aws_eks_cluster.this.name

  node_group_name = var.node_group_name

  node_role_arn = aws_iam_role.eks_node.arn

  subnet_ids = var.subnet_ids

  # t3.micro não comporta a app (limite de ~4 pods e 1 GiB de RAM).
  instance_types = [
    "t3.medium"
  ]

  capacity_type = "SPOT"

  scaling_config {
    desired_size = 1
    min_size     = 1
    max_size     = 1
  }

  launch_template {
    id      = aws_launch_template.nodes.id
    version = aws_launch_template.nodes.latest_version
  }

  depends_on = [
    aws_iam_role_policy_attachment.eks_worker_node,
    aws_iam_role_policy_attachment.eks_cni,
    aws_iam_role_policy_attachment.eks_ecr_read_only,
  ]
}

resource "aws_eks_access_entry" "admin" {
  cluster_name = aws_eks_cluster.this.name

  principal_arn = var.admin_role_arn

  type = "STANDARD"
}

resource "aws_eks_access_policy_association" "admin" {

  cluster_name = aws_eks_cluster.this.name

  principal_arn = var.admin_role_arn

  policy_arn = "arn:aws:eks::aws:cluster-access-policy/AmazonEKSClusterAdminPolicy"

  access_scope {
    type = "cluster"
  }

  # A política só pode ser associada depois que a entrada de acesso existir.
  depends_on = [aws_eks_access_entry.admin]
}