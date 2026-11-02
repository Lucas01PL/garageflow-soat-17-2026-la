# Senha do Mongo gerada pelo Terraform. Só letras e números, para não quebrar
# a URI de conexão (caracteres como @ : / precisariam ser codificados).
resource "random_password" "mongo" {
  length  = 24
  special = false
}

# A mesma senha vai para o SSM. É a única fonte: a pipeline lê daqui e o
# Terraform a usa para criar o banco. O parâmetro some junto com o destroy.
resource "aws_ssm_parameter" "mongo_password" {
  name  = var.ssm_parameter_name
  type  = "SecureString"
  value = random_password.mongo.result
}

# AMI mais recente do Ubuntu 24.04 LTS amd64 (Canonical)
data "aws_ami" "ubuntu" {
  most_recent = true
  owners      = ["099720109477"]

  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd-gp3/ubuntu-noble-24.04-amd64-server-*"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }
}

resource "aws_security_group" "mongo" {
  name        = "${var.name_prefix}-mongo"
  description = "MongoDB: acesso apenas dos nos do EKS"
  vpc_id      = var.vpc_id

  tags = {
    Name = "${var.name_prefix}-mongo"
  }
}

# Entrada: porta do Mongo só vinda do security group dos nós do EKS.
# Os pods saem pela interface do nó, então a origem é esse security group.
resource "aws_vpc_security_group_ingress_rule" "mongo_from_nodes" {
  security_group_id = aws_security_group.mongo.id

  referenced_security_group_id = var.nodes_security_group_id
  ip_protocol                  = "tcp"
  from_port                    = 27017
  to_port                      = 27017
}

# Saída livre: necessária para baixar o Docker e a imagem do Mongo
resource "aws_vpc_security_group_egress_rule" "mongo_all_egress" {
  security_group_id = aws_security_group.mongo.id

  cidr_ipv4   = "0.0.0.0/0"
  ip_protocol = "-1"
}

resource "aws_instance" "mongo" {
  ami                    = data.aws_ami.ubuntu.id
  instance_type          = var.instance_type
  subnet_id              = var.subnet_id
  vpc_security_group_ids = [aws_security_group.mongo.id]

  # O IP público só serve para a saída (não há NAT na VPC). O security group
  # não permite nenhuma entrada vinda da internet.
  associate_public_ip_address = true

  # Se o script de boot mudar, a instância é recriada
  user_data_replace_on_change = true

  root_block_device {
    volume_type           = "gp3"
    volume_size           = 20
    delete_on_termination = true
  }

  metadata_options {
    http_endpoint = "enabled"
    http_tokens   = "required"
  }

  # Instala o Docker e sobe o Mongo com autenticação. Evitamos a sintaxe
  # dólar+chave do bash aqui, porque o Terraform a trataria como interpolação.
  user_data = <<-EOT
    #!/bin/bash
    set -euxo pipefail

    apt-get update
    apt-get install -y docker.io
    systemctl enable --now docker

    mkdir -p /var/lib/mongo
    docker run -d --name mongo --restart unless-stopped \
      -p 27017:27017 \
      -v /var/lib/mongo:/data/db \
      -e MONGO_INITDB_ROOT_USERNAME=admin \
      -e MONGO_INITDB_ROOT_PASSWORD='${random_password.mongo.result}' \
      mongo:7
  EOT

  tags = {
    Name = "${var.name_prefix}-mongo"
  }
}
