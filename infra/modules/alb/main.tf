# Security Group para o Application Load Balancer (tráfego de entrada da internet)
resource "aws_security_group" "alb" {
  name        = "${var.alb_name}-sg"
  description = "Security Group para o ALB permitindo trafego web de entrada"
  vpc_id      = var.vpc_id

  tags = merge(
    var.tags,
    {
      Name = "${var.alb_name}-sg"
    }
  )
}

# Regra de entrada: HTTP 80 público
resource "aws_vpc_security_group_ingress_rule" "alb_http_ingress" {
  security_group_id = aws_security_group.alb.id

  cidr_ipv4   = "0.0.0.0/0"
  from_port   = 80
  to_port     = 80
  ip_protocol = "tcp"
}

# Regra de saída: tráfego liberado para encaminhar aos nós do EKS
resource "aws_vpc_security_group_egress_rule" "alb_egress" {
  security_group_id = aws_security_group.alb.id

  cidr_ipv4   = "0.0.0.0/0"
  ip_protocol = "-1"
}

# Application Load Balancer público (Internet-facing)
resource "aws_lb" "this" {
  name               = var.alb_name
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.alb.id]
  subnets            = var.public_subnet_ids

  enable_deletion_protection = false

  tags = merge(
    var.tags,
    {
      Name = var.alb_name
    }
  )
}

# Target Group para direcionar tráfego para os nós do EKS
resource "aws_lb_target_group" "this" {
  name        = "${var.alb_name}-tg"
  port        = var.target_port
  protocol    = var.target_protocol
  vpc_id      = var.vpc_id
  target_type = "instance"

  health_check {
    enabled             = true
    path                = var.health_check_path
    port                = "traffic-port"
    protocol            = var.target_protocol
    interval            = 30
    timeout             = 5
    healthy_threshold   = 2
    unhealthy_threshold = 3
    matcher             = "200-399"
  }

  tags = merge(
    var.tags,
    {
      Name = "${var.alb_name}-tg"
    }
  )
}

# Listener HTTP na porta 80 encaminhando para o Target Group
resource "aws_lb_listener" "http" {
  load_balancer_arn = aws_lb.this.arn
  port              = 80
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.this.arn
  }
}

