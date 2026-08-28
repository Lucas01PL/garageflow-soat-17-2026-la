package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.InsufficientQuantityException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.InvalidRepairOrderItemException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.InvalidRepairOrderStateException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.PartNotFoundException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.WorkshopServiceNotFoundException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.RepairOrderStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.WorkshopServiceStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RepairOrderTest {

    private RepairOrder repairOrder;

    @BeforeEach
    void setUp() {
        repairOrder = RepairOrder.builder()
                .status(RepairOrderStatus.RECEIVED)
                .build();
    }

    // ============================================================
    // ADD WORKSHOP SERVICE
    // ============================================================

    @Test
    void shouldAddWorkshopService() {

        WorkshopServiceSnapshot service =
                workshopService("service-1", 2, "100.00");

        repairOrder.addWorkshopService(service);

        assertThat(repairOrder.getWorkshopServices())
                .hasSize(1)
                .containsExactly(service);

        assertThat(repairOrder.getTotalServices())
                .isEqualByComparingTo("200.00");

        assertThat(repairOrder.getTotalParts())
                .isEqualByComparingTo("0");

        assertThat(repairOrder.getTotal())
                .isEqualByComparingTo("200.00");
    }

    @Test
    void shouldIncreaseQuantityWhenAddingSameWorkshopService() {

        repairOrder.addWorkshopService(
                workshopService("service-1", 2, "100.00"));

        repairOrder.addWorkshopService(
                workshopService("service-1", 3, "100.00"));

        assertThat(repairOrder.getWorkshopServices())
                .hasSize(1);

        assertThat(
                repairOrder.getWorkshopServices()
                        .getFirst()
                        .getQuantity())
                .isEqualTo(5);

        assertThat(repairOrder.getTotalServices())
                .isEqualByComparingTo("500.00");
    }

    @Test
    void shouldNotAllowAddingWorkshopServiceWhenStatusIsNotReceivedOrInDiagnosis() {

        repairOrder = RepairOrder.builder()
                .status(RepairOrderStatus.AWAITING_APPROVAL)
                .build();

        assertThatThrownBy(() ->
                repairOrder.addWorkshopService(
                        workshopService("service-1", 1, "100.00")))
                .isInstanceOf(InvalidRepairOrderItemException.class);
    }

    @Test
    void shouldNotAllowNullWorkshopService() {

        assertThatThrownBy(() ->
                repairOrder.addWorkshopService(null))
                .isInstanceOf(NullPointerException.class);
    }

    // ============================================================
    // ADD PART
    // ============================================================

    @Test
    void shouldAddPart() {

        PartSnapshot part =
                part("part-1", 2, "50.00");

        repairOrder.addPart(part);

        assertThat(repairOrder.getParts())
                .hasSize(1)
                .containsExactly(part);

        assertThat(repairOrder.getTotalParts())
                .isEqualByComparingTo("100.00");

        assertThat(repairOrder.getTotal())
                .isEqualByComparingTo("100.00");
    }

    @Test
    void shouldIncreaseQuantityWhenAddingSamePart() {

        repairOrder.addPart(
                part("part-1", 2, "50.00"));

        repairOrder.addPart(
                part("part-1", 3, "50.00"));

        assertThat(repairOrder.getParts())
                .hasSize(1);

        assertThat(
                repairOrder.getParts()
                        .getFirst()
                        .getQuantity())
                .isEqualTo(5);

        assertThat(repairOrder.getTotalParts())
                .isEqualByComparingTo("250.00");
    }

    @Test
    void shouldNotAllowAddingPartWhenStatusIsNotReceivedOrInDiagnosis() {

        repairOrder = RepairOrder.builder()
                .status(RepairOrderStatus.APPROVED)
                .build();

        assertThatThrownBy(() ->
                repairOrder.addPart(
                        part("part-1", 1, "50.00")))
                .isInstanceOf(InvalidRepairOrderItemException.class);
    }

    // ============================================================
    // REMOVE PART
    // ============================================================

    @Test
    void shouldDecreasePartQuantity() {

        repairOrder.addPart(
                part("part-1", 5, "50.00"));

        repairOrder.removePart(
                part("part-1", 2, "50.00"));

        assertThat(repairOrder.getParts())
                .hasSize(1);

        assertThat(repairOrder.getParts()
                .getFirst()
                .getQuantity())
                .isEqualTo(3);

        assertThat(repairOrder.getTotalParts())
                .isEqualByComparingTo("150.00");
    }

    @Test
    void shouldRemovePartWhenRemovingEntireQuantity() {

        repairOrder.addPart(
                part("part-1", 5, "50.00"));

        repairOrder.removePart(
                part("part-1", 5, "50.00"));

        assertThat(repairOrder.getParts())
                .isEmpty();

        assertThat(repairOrder.getTotalParts())
                .isEqualByComparingTo("0");

        assertThat(repairOrder.getTotal())
                .isEqualByComparingTo("0");
    }

    @Test
    void shouldThrowWhenRemovingMorePartsThanAvailable() {

        repairOrder.addPart(
                part("part-1", 2, "50.00"));

        assertThatThrownBy(() ->
                repairOrder.removePart(
                        part("part-1", 3, "50.00")))
                .isInstanceOf(InsufficientQuantityException.class);
    }

    @Test
    void shouldThrowWhenRemovingNonExistingPart() {

        assertThatThrownBy(() ->
                repairOrder.removePart(
                        part("part-1", 1, "50.00")))
                .isInstanceOf(PartNotFoundException.class);
    }

    // ============================================================
    // REMOVE WORKSHOP SERVICE
    // ============================================================

    @Test
    void shouldDecreaseWorkshopServiceQuantity() {

        repairOrder.addWorkshopService(
                workshopService("service-1", 5, "100.00"));

        repairOrder.removeWorkshopService(
                workshopService("service-1", 2, "100.00"));

        assertThat(repairOrder.getWorkshopServices())
                .hasSize(1);

        assertThat(repairOrder.getWorkshopServices()
                .getFirst()
                .getQuantity())
                .isEqualTo(3);

        assertThat(repairOrder.getTotalServices())
                .isEqualByComparingTo("300.00");
    }

    @Test
    void shouldRemoveWorkshopServiceWhenRemovingEntireQuantity() {

        repairOrder.addWorkshopService(
                workshopService("service-1", 2, "100.00"));

        repairOrder.removeWorkshopService(
                workshopService("service-1", 2, "100.00"));

        assertThat(repairOrder.getWorkshopServices())
                .isEmpty();

        assertThat(repairOrder.getTotalServices())
                .isEqualByComparingTo("0");
    }

    @Test
    void shouldThrowWhenRemovingMoreWorkshopServicesThanAvailable() {

        repairOrder.addWorkshopService(
                workshopService("service-1", 2, "100.00"));

        assertThatThrownBy(() ->
                repairOrder.removeWorkshopService(
                        workshopService("service-1", 3, "100.00")))
                .isInstanceOf(InsufficientQuantityException.class);
    }

    @Test
    void shouldThrowWhenRemovingNonExistingWorkshopService() {

        assertThatThrownBy(() ->
                repairOrder.removeWorkshopService(
                        workshopService("service-1", 1, "100.00")))
                .isInstanceOf(WorkshopServiceNotFoundException.class);
    }

    // ============================================================
    // TOTALS
    // ============================================================

    @Test
    void shouldCalculateTotalWithPartsAndServices() {

        repairOrder.addWorkshopService(
                workshopService("service-1", 2, "100.00"));

        repairOrder.addPart(
                part("part-1", 3, "50.00"));

        assertThat(repairOrder.getTotalServices())
                .isEqualByComparingTo("200.00");

        assertThat(repairOrder.getTotalParts())
                .isEqualByComparingTo("150.00");

        assertThat(repairOrder.getTotal())
                .isEqualByComparingTo("350.00");
    }

    // ============================================================
    // STATUS - RECEIVED
    // ============================================================

    @Test
    void shouldSetRepairOrderAsReceived() {

        RepairOrder order = RepairOrder.builder().build();

        order.received();

        assertThat(order.getStatus())
                .isEqualTo(RepairOrderStatus.RECEIVED);

        assertThat(order.getCreatedDate())
                .isNotNull();

        assertThat(order.getInitDate())
                .isNotNull();
    }

    // ============================================================
    // STATUS - DIAGNOSIS
    // ============================================================

    @Test
    void shouldStartDiagnosisWhenRepairOrderIsReceived() {

        repairOrder.startInDiagnosis();

        assertThat(repairOrder.getStatus())
                .isEqualTo(RepairOrderStatus.IN_DIAGNOSIS);

        assertThat(repairOrder.getUpdatedDate())
                .isNotNull();
    }

    @Test
    void shouldNotStartDiagnosisWhenRepairOrderIsNotReceived() {

        repairOrder = RepairOrder.builder()
                .status(RepairOrderStatus.APPROVED)
                .build();

        assertThatThrownBy(() ->
                repairOrder.startInDiagnosis())
                .isInstanceOf(InvalidRepairOrderStateException.class);
    }

    // ============================================================
    // STATUS - APPROVAL
    // ============================================================

    @Test
    void shouldRequestApprovalWhenInDiagnosisAndHasService() {

        repairOrder = RepairOrder.builder()
                .status(RepairOrderStatus.IN_DIAGNOSIS)
                .build();

        repairOrder.addWorkshopService(
                workshopService("service-1", 1, "100.00"));

        repairOrder.requestApproval();

        assertThat(repairOrder.getStatus())
                .isEqualTo(RepairOrderStatus.AWAITING_APPROVAL);
    }

    @Test
    void shouldNotRequestApprovalWithoutWorkshopService() {

        repairOrder = RepairOrder.builder()
                .status(RepairOrderStatus.IN_DIAGNOSIS)
                .build();

        assertThatThrownBy(() ->
                repairOrder.requestApproval())
                .isInstanceOf(InvalidRepairOrderItemException.class);
    }

    @Test
    void shouldNotRequestApprovalWhenNotInDiagnosis() {

        assertThatThrownBy(() ->
                repairOrder.requestApproval())
                .isInstanceOf(InvalidRepairOrderStateException.class);
    }

    @Test
    void shouldApproveWhenAwaitingApproval() {

        repairOrder = RepairOrder.builder()
                .status(RepairOrderStatus.AWAITING_APPROVAL)
                .build();

        repairOrder.approve();

        assertThat(repairOrder.getStatus())
                .isEqualTo(RepairOrderStatus.APPROVED);
    }

    @Test
    void shouldNotApproveWhenNotAwaitingApproval() {

        assertThatThrownBy(() ->
                repairOrder.approve())
                .isInstanceOf(InvalidRepairOrderStateException.class);
    }

    // ============================================================
    // STATUS - REJECTED
    // ============================================================

    @Test
    void shouldRejectWhenAwaitingApproval() {

        repairOrder = RepairOrder.builder()
                .status(RepairOrderStatus.AWAITING_APPROVAL)
                .build();

        repairOrder.reject();

        assertThat(repairOrder.getStatus())
                .isEqualTo(RepairOrderStatus.REJECTED);
    }

    @Test
    void shouldNotRejectWhenNotAwaitingApproval() {

        assertThatThrownBy(() ->
                repairOrder.reject())
                .isInstanceOf(InvalidRepairOrderStateException.class);
    }

    // ============================================================
    // STATUS - EXECUTION
    // ============================================================

    @Test
    void shouldStartExecutionWhenApproved() {

        repairOrder = RepairOrder.builder()
                .status(RepairOrderStatus.APPROVED)
                .build();

        repairOrder.startExecution();

        assertThat(repairOrder.getStatus())
                .isEqualTo(RepairOrderStatus.IN_EXECUTION);
    }

    @Test
    void shouldNotStartExecutionWhenNotApproved() {

        repairOrder = RepairOrder.builder()
                .status(RepairOrderStatus.AWAITING_APPROVAL)
                .build();

        assertThatThrownBy(() ->
                repairOrder.startExecution())
                .isInstanceOf(InvalidRepairOrderStateException.class);
    }

    // ============================================================
    // STATUS - FINISHED
    // ============================================================

    @Test
    void shouldFinishRepairOrderWhenInExecution() {

        repairOrder = RepairOrder.builder()
                .status(RepairOrderStatus.IN_EXECUTION)
                .build();

        repairOrder.finish();

        assertThat(repairOrder.getStatus())
                .isEqualTo(RepairOrderStatus.FINISHED);

        assertThat(repairOrder.getFinishDate())
                .isNotNull();

        assertThat(repairOrder.getUpdatedDate())
                .isNotNull();
    }

    @Test
    void shouldNotFinishRepairOrderWhenNotInExecution() {

        repairOrder = RepairOrder.builder()
                .status(RepairOrderStatus.APPROVED)
                .build();

        assertThatThrownBy(() ->
                repairOrder.finish())
                .isInstanceOf(InvalidRepairOrderStateException.class);
    }

    // ============================================================
    // STATUS - DELIVERED
    // ============================================================

    @Test
    void shouldDeliverRepairOrderWhenFinished() {

        repairOrder = RepairOrder.builder()
                .status(RepairOrderStatus.FINISHED)
                .build();

        repairOrder.deliver();

        assertThat(repairOrder.getStatus())
                .isEqualTo(RepairOrderStatus.DELIVERED);
    }

    @Test
    void shouldNotDeliverRepairOrderWhenNotFinished() {

        repairOrder = RepairOrder.builder()
                .status(RepairOrderStatus.IN_EXECUTION)
                .build();

        assertThatThrownBy(() ->
                repairOrder.deliver())
                .isInstanceOf(InvalidRepairOrderStateException.class);
    }

    // ============================================================
    // STATUS - CANCELLED
    // ============================================================

    @Test
    void shouldCancelRepairOrderWhenReceived() {

        RepairOrder repairOrder =
                RepairOrder.builder()
                        .status(RepairOrderStatus.RECEIVED)
                        .build();

        repairOrder.cancel();

        assertEquals(
                RepairOrderStatus.CANCELLED,
                repairOrder.getStatus()
        );
    }

    @Test
    void shouldThrowWhenCancellingRepairOrderInExecution() {

        RepairOrder repairOrder =
                RepairOrder.builder()
                        .status(RepairOrderStatus.IN_EXECUTION)
                        .build();

        InvalidRepairOrderStateException exception =
                assertThrows(
                        InvalidRepairOrderStateException.class,
                        repairOrder::cancel
                );

        assertEquals(
                "Invalid Repair Order State: Repair Order cannot be cancelled in the current state.",
                exception.getMessage()
        );
    }

    // ============================================================
    // WORKSHOP SERVICE EXECUTION
    // ============================================================

    @Test
    void shouldStartWorkshopServiceWhenWaitingAttendance() {

        WorkshopServiceSnapshot service =
                workshopService("service-1", 1, "100.00");
        service.setStatus(WorkshopServiceStatus.WAITING_ATTENDING);

        repairOrder = RepairOrder.builder()
                .status(RepairOrderStatus.IN_EXECUTION)
                .workshopServices(List.of(service))
                .build();

        repairOrder.startWorkshopService("service-1");

        assertThat(service.getStatus())
                .isEqualTo(WorkshopServiceStatus.IN_EXECUTION);
    }

    @Test
    void shouldThrowWhenStartingNonExistingWorkshopService() {

        repairOrder = RepairOrder.builder()
                .status(RepairOrderStatus.IN_EXECUTION)
                .build();

        assertThatThrownBy(() ->
                repairOrder.startWorkshopService("service-1"))
                .isInstanceOf(WorkshopServiceNotFoundException.class);
    }

    @Test
    void shouldNotStartWorkshopServiceWhenNotWaitingAttendance() {

        WorkshopServiceSnapshot service =
                workshopService("service-1", 1, "100.00");

        service.start();

        repairOrder = RepairOrder.builder()
                .status(RepairOrderStatus.IN_EXECUTION)
                .workshopServices(List.of(service))
                .build();

        assertThatThrownBy(() ->
                repairOrder.startWorkshopService("service-1"))
                .isInstanceOf(InvalidRepairOrderStateException.class);
    }

    @Test
    void shouldRejectInvalidWorkshopServiceDuration() {

        WorkshopServiceSnapshot service =
                workshopService("service-1", 1, "100.00");

        service.start();

        repairOrder = RepairOrder.builder()
                .status(RepairOrderStatus.IN_EXECUTION)
                .workshopServices(List.of(service))
                .build();

        assertThatThrownBy(() ->
                repairOrder.finishWorkshopService("service-1", 0))
                .isInstanceOf(InvalidRepairOrderItemException.class);
    }

    // ============================================================
    // IMMUTABILITY OF LISTS
    // ============================================================

    @Test
    void shouldNotAllowExternalModificationOfPartsList() {

        repairOrder.addPart(
                part("part-1", 1, "50.00"));

        assertThatThrownBy(() ->
                repairOrder.getParts().clear())
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldNotAllowExternalModificationOfWorkshopServicesList() {

        repairOrder.addWorkshopService(
                workshopService("service-1", 1, "100.00"));

        assertThatThrownBy(() ->
                repairOrder.getWorkshopServices().clear())
                .isInstanceOf(UnsupportedOperationException.class);
    }

    // ============================================================
    // HELPERS
    // ============================================================

    private PartSnapshot part(
            String id,
            Integer quantity,
            String unitPrice) {

        PartSnapshot part = new PartSnapshot();

        part.setId(id);
        part.setQuantity(quantity);
        part.setUnitPrice(new BigDecimal(unitPrice));

        return part;
    }

    private WorkshopServiceSnapshot workshopService(
            String id,
            Integer quantity,
            String unitPrice) {

        WorkshopServiceSnapshot service =
                new WorkshopServiceSnapshot();

        service.setWorkshopServiceId(id);
        service.setQuantity(quantity);
        service.setUnitPrice(new BigDecimal(unitPrice));

        return service;
    }
}