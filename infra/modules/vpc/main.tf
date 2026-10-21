# O provider "aws" é herdado do ambiente (environments/dev). Se o módulo tivesse
# um bloco próprio, as default_tags do ambiente não chegariam aos recursos daqui.

resource "aws_vpc" "vpc_main" {
  cidr_block = "10.0.0.0/16"

  enable_dns_support   = true
  enable_dns_hostnames = true

  tags = {
    Name        = "garageflow-vpc"
    Project     = "garageflow"
    Environment = "dev"
  }
}

resource "aws_internet_gateway" "igw_main" {
  vpc_id = aws_vpc.vpc_main.id

  tags = {
    Name = "garageflow-igw"
  }
}

# ----- Subnets Públicas (Multi-AZ obrigatório para o ALB e EKS) -----

# Subnet na Zona de Disponibilidade us-east-1a
resource "aws_subnet" "public_subnet_a" {
  vpc_id                  = aws_vpc.vpc_main.id
  cidr_block              = "10.0.1.0/24"
  availability_zone       = "us-east-1a"
  map_public_ip_on_launch = true

  tags = {
    Name                     = "garageflow-public-a"
    "kubernetes.io/role/elb" = "1"
  }
}

# Subnet na Zona de Disponibilidade us-east-1b (Necessária para cumprir o requisito de 2 AZs do ALB)
resource "aws_subnet" "public_subnet_b" {
  vpc_id                  = aws_vpc.vpc_main.id
  cidr_block              = "10.0.2.0/24"
  availability_zone       = "us-east-1b"
  map_public_ip_on_launch = true

  tags = {
    Name                     = "garageflow-public-b"
    "kubernetes.io/role/elb" = "1"
  }
}

# ----- Tabela de Rotas Pública -----

resource "aws_route_table" "public_route_table" {
  vpc_id = aws_vpc.vpc_main.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw_main.id
  }

  tags = {
    Name = "garageflow-public-rt"
  }
}

# Associação das duas subnets públicas à tabela de rotas com saída para a internet
resource "aws_route_table_association" "public_a" {
  subnet_id      = aws_subnet.public_subnet_a.id
  route_table_id = aws_route_table.public_route_table.id
}

resource "aws_route_table_association" "public_b" {
  subnet_id      = aws_subnet.public_subnet_b.id
  route_table_id = aws_route_table.public_route_table.id
}