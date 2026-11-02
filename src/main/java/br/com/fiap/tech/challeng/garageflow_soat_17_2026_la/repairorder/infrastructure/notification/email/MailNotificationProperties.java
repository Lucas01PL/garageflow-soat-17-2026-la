package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.infrastructure.notification.email;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.notification.mail")
public record MailNotificationProperties(
        String from,
        String publicBaseUrl
) {
}