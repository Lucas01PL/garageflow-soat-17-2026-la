output "alb_id" {
  description = "ID do Application Load Balancer"
  value       = aws_lb.this.id
}

output "alb_arn" {
  description = "ARN do Application Load Balancer"
  value       = aws_lb.this.arn
}

output "alb_dns_name" {
  description = "DNS público gerado para o Application Load Balancer"
  value       = aws_lb.this.dns_name
}

output "alb_security_group_id" {
  description = "ID do Security Group do ALB"
  value       = aws_security_group.alb.id
}

output "target_group_arn" {
  description = "ARN do Target Group"
  value       = aws_lb_target_group.this.arn
}

output "target_group_name" {
  description = "Nome do Target Group"
  value       = aws_lb_target_group.this.name
}

output "listener_arn" {
  description = "ARN do Listener HTTP"
  value       = aws_lb_listener.http.arn
}

