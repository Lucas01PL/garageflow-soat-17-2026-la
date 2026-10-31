package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la
        .shared.config;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la
        .repairorder.infrastructure.notification.email
        .MailNotificationProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
@EnableConfigurationProperties(MailNotificationProperties.class)
public class NotificationConfiguration {
}