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

import java.math.BigDecimal;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RepairOrderStatusEmailListenerTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private RepairOrderStatusEmailListener listener;

    @BeforeEach
    void setUp() {
        listener = new RepairOrderStatusEmailListener(
                mailSender,
                new MailNotificationProperties(
                        "no-reply@garageflow.com",
                        "https://garageflow.example/api/"
                )
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
                "one-time-secret",
                List.of(new RepairOrderStatusChangedEvent.BudgetItem(
                        "<Revisão & óleo>",
                        2,
                        new BigDecimal("150.50")
                )),
                List.of(new RepairOrderStatusChangedEvent.BudgetItem(
                        "Filtro",
                        3,
                        new BigDecimal("20.00")
                )),
                new BigDecimal("301.00"),
                new BigDecimal("60.00"),
                new BigDecimal("361.00")
        ));

        ArgumentCaptor<MimeMessage> captor =
                ArgumentCaptor.forClass(MimeMessage.class);
        verify(mailSender).send(captor.capture());

        String content = extractText(captor.getValue());
        assertTrue(content.contains("one-time-secret"));
        assertTrue(content.contains(
                "https://garageflow.example/api/external/repair-orders/budget-decision"
        ));
        assertTrue(content.contains("name=\"approved\" value=\"true\""));
        assertTrue(content.contains("name=\"approved\" value=\"false\""));
        assertTrue(content.contains("name=\"token\" value=\"one-time-secret\""));
        assertTrue(content.contains("Aprovar orçamento"));
        assertTrue(content.contains("Rejeitar orçamento"));
        assertTrue(content.contains("Orçamento para aprovação"));
        assertTrue(content.contains("<Revisão & óleo>"));
        assertTrue(content.contains("&lt;Revis&atilde;o &amp; &oacute;leo&gt;"));
        assertTrue(content.contains("Quantidade: 2"));
        assertTrue(content.contains("150,50"));
        assertTrue(content.contains("301,00"));
        assertTrue(content.contains("Filtro"));
        assertTrue(content.contains("Quantidade: 3"));
        assertTrue(content.contains("20,00"));
        assertTrue(content.contains("60,00"));
        assertTrue(content.contains("361,00"));
        assertTrue(content.contains("Total de serviços"));
        assertTrue(content.contains("Total de peças"));
        assertTrue(content.contains("Total geral"));
    }

    @Test
    void shouldNotIncludeBudgetForOtherStatuses() throws Exception {
        MimeMessage message =
                new MimeMessage(Session.getInstance(new Properties()));
        when(mailSender.createMimeMessage()).thenReturn(message);

        listener.onStatusChanged(new RepairOrderStatusChangedEvent(
                "repair-order-2",
                "RO87654321",
                "Customer",
                "customer@example.com",
                RepairOrderStatus.APPROVED,
                null,
                null,
                List.of(new RepairOrderStatusChangedEvent.BudgetItem(
                        "Item exclusivo do orçamento",
                        1,
                        new BigDecimal("999.99")
                )),
                List.of(),
                new BigDecimal("999.99"),
                BigDecimal.ZERO,
                new BigDecimal("999.99")
        ));

        ArgumentCaptor<MimeMessage> captor =
                ArgumentCaptor.forClass(MimeMessage.class);
        verify(mailSender).send(captor.capture());

        String content = extractText(captor.getValue());
        assertFalse(content.contains("Item exclusivo do orçamento"));
        assertFalse(content.contains("Orçamento para aprovação"));
        assertFalse(content.contains("999,99"));
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
