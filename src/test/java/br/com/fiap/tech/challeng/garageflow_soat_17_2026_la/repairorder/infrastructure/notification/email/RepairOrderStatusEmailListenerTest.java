package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.infrastructure.notification.email;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.event.RepairOrderStatusChangedEvent;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.RepairOrderStatus;
import jakarta.mail.Multipart;
import jakarta.mail.Part;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RepairOrderStatusEmailListenerTest {

    @Mock
    private JavaMailSender mailSender;

    private RepairOrderStatusEmailListener listener;

    @BeforeEach
    void setUp() {
        listener = new RepairOrderStatusEmailListener(
                mailSender,
                new MailNotificationProperties("no-reply@garageflow.com")
        );
    }

    @Test
    void shouldIncludeApprovalTokenAndInstructionsInEmail() throws Exception {
        MimeMessage message =
                new MimeMessage(Session.getInstance(new Properties()));
        when(mailSender.createMimeMessage()).thenReturn(message);

        listener.onStatusChanged(new RepairOrderStatusChangedEvent(
                "repair-order-1",
                "RO12345678",
                "Customer",
                "customer@example.com",
                RepairOrderStatus.AWAITING_APPROVAL,
                null,
                "one-time-secret"
        ));

        ArgumentCaptor<MimeMessage> captor =
                ArgumentCaptor.forClass(MimeMessage.class);
        verify(mailSender).send(captor.capture());

        String content = extractText(captor.getValue());
        assertTrue(content.contains("one-time-secret"));
        assertTrue(content.contains("/repair-orders/budget-decision"));
        assertTrue(content.contains("X-API-Key"));
        assertTrue(content.contains("{\"approved\": true}"));
        assertTrue(content.contains("{\"approved\": false}"));
    }

    private String extractText(Part part) throws Exception {
        Object content = part.getContent();
        if (content instanceof String text) {
            return text;
        }

        if (content instanceof Multipart multipart) {
            StringBuilder text = new StringBuilder();
            for (int index = 0; index < multipart.getCount(); index++) {
                text.append(extractText(multipart.getBodyPart(index)));
            }
            return text.toString();
        }

        return "";
    }
}
