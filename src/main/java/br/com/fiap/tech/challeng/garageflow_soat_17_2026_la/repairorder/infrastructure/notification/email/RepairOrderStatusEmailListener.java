package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la
        .repairorder.infrastructure.notification.email;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la
        .repairorder.application.event.RepairOrderStatusChangedEvent;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.event.EventListener;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
@Slf4j
public class RepairOrderStatusEmailListener {

    private static final ZoneId TIME_ZONE =
            ZoneId.of("America/Fortaleza");

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter
                    .ofPattern("dd/MM/yyyy 'às' HH:mm")
                    .withZone(TIME_ZONE);

    private final JavaMailSender mailSender;
    private final MailNotificationProperties properties;

    @Async
    @EventListener
    public void onStatusChanged(
            RepairOrderStatusChangedEvent event) {

        if (event.customerEmail() == null
                || event.customerEmail().isBlank()) {

            log.warn(
                    "Email notification skipped: customer email is missing. "
                            + "RepairOrderId={}",
                    event.repairOrderId()
            );

            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            true,
                            StandardCharsets.UTF_8.name()
                    );

            String customerName =
                    hasText(event.customerName())
                            ? event.customerName()
                            : "Cliente";

            String repairOrderNumber =
                    hasText(event.repairOrderNumber())
                            ? event.repairOrderNumber()
                            : event.repairOrderId();

            String statusDescription =
                    event.newStatus().getDescription();

            String statusCode =
                    event.newStatus().name();

            String occurredAt =
                    event.occurredAt() == null
                            ? "Não informado"
                            : DATE_FORMATTER.format(event.occurredAt());

            helper.setFrom(properties.from());
            helper.setTo(event.customerEmail());

            helper.setSubject(
                    "GarageFlow | Atualização da OS "
                            + repairOrderNumber
            );

            helper.setText(
                    buildPlainText(
                            customerName,
                            repairOrderNumber,
                            statusDescription,
                            statusCode,
                            occurredAt
                    ),
                    buildHtmlMessage(
                            customerName,
                            repairOrderNumber,
                            statusDescription,
                            statusCode,
                            occurredAt
                    )
            );

            mailSender.send(message);

            log.info(
                    "Repair Order notification sent. "
                            + "RepairOrderId={}, Status={}",
                    event.repairOrderId(),
                    event.newStatus()
            );

        } catch (MessagingException | MailException exception) {

            log.error(
                    "Failed to send Repair Order notification. "
                            + "RepairOrderId={}, Status={}",
                    event.repairOrderId(),
                    event.newStatus(),
                    exception
            );
        }
    }

    private String buildPlainText(
            String customerName,
            String repairOrderNumber,
            String statusDescription,
            String statusCode,
            String occurredAt) {

        return """
                Olá, %s!

                Temos uma atualização sobre sua Ordem de Serviço.

                Ordem de Serviço: %s
                Novo status: %s
                Código do status: %s
                Data da atualização: %s

                Acompanhe sua OS pelos canais oficiais do GarageFlow.

                Esta é uma mensagem automática. Por favor, não responda
                diretamente a este e-mail.
                """.formatted(
                customerName,
                repairOrderNumber,
                statusDescription,
                statusCode,
                occurredAt
        );
    }

    private String buildHtmlMessage(
            String customerName,
            String repairOrderNumber,
            String statusDescription,
            String statusCode,
            String occurredAt) {

        String html = """
                <!DOCTYPE html>
                <html lang="pt-BR">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport"
                          content="width=device-width, initial-scale=1.0">
                    <title>Atualização da Ordem de Serviço</title>
                </head>
                <body style="margin:0; padding:0;
                             background-color:#f3f5f9;
                             font-family:Arial,Helvetica,sans-serif;
                             color:#243247;">

                    <div style="display:none; max-height:0; overflow:hidden;
                                opacity:0; color:transparent;">
                        Sua Ordem de Serviço teve o status atualizado.
                    </div>

                    <table role="presentation"
                           width="100%"
                           cellspacing="0"
                           cellpadding="0"
                           border="0"
                           style="width:100%; background-color:#f3f5f9;">

                        <tr>
                            <td align="center"
                                style="padding:32px 12px;">

                                <table role="presentation"
                                       width="600"
                                       cellspacing="0"
                                       cellpadding="0"
                                       border="0"
                                       style="width:100%; max-width:600px;
                                              background-color:#ffffff;
                                              border-radius:12px;
                                              overflow:hidden;">

                                    <!-- Cabeçalho -->
                                    <tr>
                                        <td style="padding:28px 32px;
                                                   background-color:#173b68;
                                                   color:#ffffff;">

                                            <div style="font-size:13px;
                                                        font-weight:bold;
                                                        letter-spacing:2px;
                                                        color:#c9dcf5;">
                                                GARAGEFLOW
                                            </div>

                                            <div style="margin-top:12px;
                                                        font-size:25px;
                                                        line-height:1.3;
                                                        font-weight:bold;
                                                        color:#ffffff;">
                                                Atualização da sua OS
                                            </div>

                                            <div style="margin-top:8px;
                                                        font-size:14px;
                                                        line-height:1.6;
                                                        color:#e2ebf7;">
                                                Acompanhe o andamento do
                                                atendimento do seu veículo.
                                            </div>
                                        </td>
                                    </tr>

                                    <!-- Conteúdo -->
                                    <tr>
                                        <td style="padding:32px;">

                                            <p style="margin:0 0 12px;
                                                      font-size:16px;
                                                      line-height:1.6;">
                                                Olá,
                                                <strong>{{customerName}}</strong>!
                                            </p>

                                            <p style="margin:0 0 24px;
                                                      font-size:14px;
                                                      line-height:1.8;
                                                      color:#526176;">
                                                O status da sua Ordem de Serviço
                                                foi atualizado. Confira os
                                                detalhes abaixo.
                                            </p>

                                            <!-- Número da OS -->
                                            <table role="presentation"
                                                   width="100%"
                                                   cellspacing="0"
                                                   cellpadding="0"
                                                   border="0"
                                                   style="width:100%;
                                                          background-color:#f7f9fc;
                                                          border:1px solid #e1e7ef;
                                                          border-radius:8px;">

                                                <tr>
                                                    <td style="padding:20px;">

                                                        <div style="font-size:11px;
                                                                    font-weight:bold;
                                                                    letter-spacing:1px;
                                                                    color:#718096;">
                                                            ORDEM DE SERVIÇO
                                                        </div>

                                                        <div style="margin-top:8px;
                                                                    font-size:24px;
                                                                    font-weight:bold;
                                                                    color:#173b68;">
                                                            {{repairOrderNumber}}
                                                        </div>

                                                    </td>
                                                </tr>
                                            </table>

                                            <!-- Status -->
                                            <div style="margin-top:24px;
                                                        font-size:11px;
                                                        font-weight:bold;
                                                        letter-spacing:1px;
                                                        color:#718096;">
                                                STATUS ATUAL
                                            </div>

                                            <table role="presentation"
                                                   cellspacing="0"
                                                   cellpadding="0"
                                                   border="0"
                                                   style="margin-top:10px;">

                                                <tr>
                                                    <td style="padding:10px 16px;
                                                               background-color:#e8f0fe;
                                                               border-radius:6px;
                                                               color:#174ea6;
                                                               font-size:16px;
                                                               font-weight:bold;">
                                                        {{statusDescription}}
                                                    </td>
                                                </tr>
                                            </table>

                                            <div style="margin-top:10px;
                                                        font-size:12px;
                                                        color:#718096;">
                                                Código: {{statusCode}}
                                            </div>

                                            <!-- Data -->
                                            <table role="presentation"
                                                   width="100%"
                                                   cellspacing="0"
                                                   cellpadding="0"
                                                   border="0"
                                                   style="width:100%;
                                                          margin-top:24px;
                                                          border-top:1px solid #e8edf3;">

                                                <tr>
                                                    <td style="padding:18px 0 0;
                                                               font-size:13px;
                                                               color:#718096;">
                                                        Data da atualização
                                                    </td>
                                                </tr>

                                                <tr>
                                                    <td style="padding:6px 0 0;
                                                               font-size:14px;
                                                               font-weight:bold;
                                                               color:#243247;">
                                                        {{occurredAt}}
                                                    </td>
                                                </tr>
                                            </table>

                                            <p style="margin:26px 0 0;
                                                      font-size:14px;
                                                      line-height:1.8;
                                                      color:#526176;">
                                                Para esclarecer dúvidas sobre
                                                o atendimento, entre em contato
                                                com a oficina pelos canais
                                                habituais.
                                            </p>

                                        </td>
                                    </tr>

                                    <!-- Rodapé -->
                                    <tr>
                                        <td style="padding:22px 32px;
                                                   background-color:#f7f9fc;
                                                   border-top:1px solid #e8edf3;">

                                            <div style="font-size:12px;
                                                        line-height:1.7;
                                                        color:#718096;">
                                                Esta é uma mensagem automática
                                                enviada pelo GarageFlow.
                                                Por favor, não responda
                                                diretamente a este e-mail.
                                            </div>

                                            <div style="margin-top:10px;
                                                        font-size:12px;
                                                        color:#9aa5b4;">
                                                GarageFlow · Gestão de Oficina
                                            </div>

                                        </td>
                                    </tr>

                                </table>

                            </td>
                        </tr>
                    </table>

                </body>
                </html>
                """;

        return html
                .replace(
                        "{{customerName}}",
                        escapeHtml(customerName)
                )
                .replace(
                        "{{repairOrderNumber}}",
                        escapeHtml(repairOrderNumber)
                )
                .replace(
                        "{{statusDescription}}",
                        escapeHtml(statusDescription)
                )
                .replace(
                        "{{statusCode}}",
                        escapeHtml(statusCode)
                )
                .replace(
                        "{{occurredAt}}",
                        escapeHtml(occurredAt)
                );
    }

    private String escapeHtml(String value) {
        return HtmlUtils.htmlEscape(value == null ? "" : value);
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}