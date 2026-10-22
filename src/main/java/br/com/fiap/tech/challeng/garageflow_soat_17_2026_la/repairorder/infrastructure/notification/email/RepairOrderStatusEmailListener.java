package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la
        .repairorder.infrastructure.notification.email;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la
        .repairorder.application.event.RepairOrderStatusChangedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RepairOrderStatusEmailListener {

    private final JavaMailSender mailSender;
    private final MailNotificationProperties properties;

    @Async
    @EventListener
    public void onStatusChanged(
            RepairOrderStatusChangedEvent event) {

        if (event.customerEmail() == null
                || event.customerEmail().isBlank()) {

            log.warn(
                    "Email notification skipped because customer email "
                            + "is missing. RepairOrderId={}",
                    event.repairOrderId()
            );

            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(properties.from());
            message.setTo(event.customerEmail());

            message.setSubject(
                    "GarageFlow - Atualização da OS "
                            + event.repairOrderNumber()
            );

            message.setText(buildMessage(event));

            mailSender.send(message);

            log.info(
                    "Repair Order status notification submitted. "
                            + "RepairOrderId={}, Status={}",
                    event.repairOrderId(),
                    event.newStatus()
            );

        } catch (MailException exception) {
            log.error(
                    "Failed to send Repair Order status notification. "
                            + "RepairOrderId={}, Status={}",
                    event.repairOrderId(),
                    event.newStatus(),
                    exception
            );
        }
    }

    private String buildMessage(
            RepairOrderStatusChangedEvent event) {

        String customerName =
                event.customerName() == null
                        || event.customerName().isBlank()
                        ? "Cliente"
                        : event.customerName();

        String repairOrderNumber =
                event.repairOrderNumber() == null
                        ? event.repairOrderId()
                        : event.repairOrderNumber();

        return """
                Olá, %s!

                O status da sua Ordem de Serviço foi atualizado.

                Ordem de Serviço: %s
                Novo status: %s (%s)
                Data/hora do evento: %s

                Acompanhe sua Ordem de Serviço pelos canais
                oficiais do GarageFlow.

                Esta é uma mensagem automática.
                """.formatted(
                customerName,
                repairOrderNumber,
                event.newStatus().getDescription(),
                event.newStatus().name(),
                event.occurredAt()
        );
    }
}