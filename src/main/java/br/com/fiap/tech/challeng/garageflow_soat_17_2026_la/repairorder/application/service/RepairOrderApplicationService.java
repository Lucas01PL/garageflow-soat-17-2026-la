package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.application.usecase.GetCustomerUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.model.Customer;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.event.RepairOrderStatusChangedEvent;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase.*;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.command.AddPartCommand;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.command.AddWorkshopServiceCommand;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.command.FinishWorkshopServiceCommand;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.command.RemovePartCommand;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.command.RemoveWorkshopServiceCommand;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.AverageWorkshopServiceExecutionTime;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.RepairOrderStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RepairOrderApplicationService {

    private final CreateRepairOrderUseCase createUseCase;
    private final GetRepairOrderByIdUseCase getByIdUseCase;
    private final ListAllRepairOrdersUseCase listAllUseCase;
    private final ListRepairOrdersByStatusUseCase listRepairOrdersByStatusUseCase;
    private final AddWorkshopServiceUseCase addWorkshopServiceUseCase;
    private final AddPartUseCase addPartUseCase;
    private final RemovePartUseCase removePartUseCase;
    private final RemoveWorkshopServiceUseCase removeWorkshopServiceUseCase;
    private final StartRepairOrderDiagnosisUseCase startRepairOrderDiagnosisUseCase;
    private final StartWorkshopServiceUseCase startWorkshopServiceUseCase;
    private final FinishWorkshopServiceUseCase finishWorkshopServiceUseCase;
    private final ApproveRepairOrderUseCase approveRepairOrderUseCase;
    private final RejectRepairOrderUseCase rejectRepairOrderUseCase;
    private final DeliverRepairOrderUseCase deliverRepairOrderUseCase;
    private final StartRepairOrderExecutionUseCase startRepairOrderExecutionUseCase;
    private final RequestRepairOrderApprovalUseCase requestRepairOrderApprovalUseCase;
    private final CancelRepairOrderUseCase cancelRepairOrderUseCase;
    private final ListRepairOrdersByCustomerUseCase listRepairOrdersByCustomerUseCase;
    private final GetAverageWorkshopServiceExecutionTimeUseCase averageWorkshopServiceExecutionTimeUseCase;
    private final GetCustomerUseCase getCustomerUseCase;
    private final ApplicationEventPublisher eventPublisher;

    public RepairOrder create(RepairOrder repairOrder) {
        return createUseCase.execute(repairOrder);
    }

    public Optional<RepairOrder> getById(String repairOrderId) {
        return getByIdUseCase.execute(repairOrderId);
    }

    public List<RepairOrder> list(String status) {
        if (status == null || status.isBlank()) {
            return listAllUseCase.execute();
        }
        return listRepairOrdersByStatusUseCase.execute(status);
    }

    public RepairOrder addWorkshopService(String repairOrderId, AddWorkshopServiceCommand command) {
        return addWorkshopServiceUseCase.execute(repairOrderId, command);
    }

    public RepairOrder addPart(String repairOrderId, AddPartCommand command) {
        return addPartUseCase.execute(repairOrderId, command);
    }

    public RepairOrder removeWorkshopService(String repairOrderId, String workshopServiceId, RemoveWorkshopServiceCommand command) {
        return removeWorkshopServiceUseCase.execute(repairOrderId, workshopServiceId, command);
    }

    public RepairOrder removePart(String repairOrderId, String partId, RemovePartCommand command) {
        return removePartUseCase.execute(repairOrderId, partId, command);
    }

    public RepairOrder startDiagnosis(String repairOrderId) {
        RepairOrder repairOrder =
                startRepairOrderDiagnosisUseCase.execute(repairOrderId);
        publishStatusNotification(repairOrder);
        return repairOrder;
    }

    public RepairOrder startWorkshopService(String repairOrderId, String workshopServiceId) {
        return startWorkshopServiceUseCase.execute(repairOrderId, workshopServiceId);
    }

    public RepairOrder finishWorkshopService(String repairOrderId, String workshopServiceId, FinishWorkshopServiceCommand command) {
        RepairOrder repairOrder = finishWorkshopServiceUseCase.execute(repairOrderId, workshopServiceId, command);
        if (repairOrder.getStatus() == RepairOrderStatus.FINISHED) {
            publishStatusNotification(repairOrder);
        }
        return repairOrder;
    }

    public RepairOrder approve(String repairOrderId, String userId) {
        RepairOrder repairOrder = approveRepairOrderUseCase.execute(repairOrderId, userId);
        publishStatusNotification(repairOrder);
        return repairOrder;
    }

    public RepairOrder reject(String repairOrderId) {
        RepairOrder repairOrder = rejectRepairOrderUseCase.execute(repairOrderId);
        publishStatusNotification(repairOrder);
        return repairOrder;
    }

    public RepairOrder deliver(String repairOrderId) {
        RepairOrder repairOrder = deliverRepairOrderUseCase.execute(repairOrderId);
        publishStatusNotification(repairOrder);
        return repairOrder;
    }

    public RepairOrder startExecution(String repairOrderId) {
        RepairOrder repairOrder = startRepairOrderExecutionUseCase.execute(repairOrderId);
        publishStatusNotification(repairOrder);
        return repairOrder;
    }

    public RepairOrder requestApproval(String repairOrderId) {
        RepairOrder repairOrder = requestRepairOrderApprovalUseCase.execute(repairOrderId);
        publishStatusNotification(repairOrder);
        return repairOrder;
    }

    public RepairOrder cancel(String repairOrderId) {
        RepairOrder repairOrder = cancelRepairOrderUseCase.execute(repairOrderId);
        publishStatusNotification(repairOrder);
        return repairOrder;
    }

    public List<RepairOrder> listByCustomerEmail(String email) {
        String customerId = getCustomerUseCase.getCustomerByEmail(email)
                .map(Customer::getId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "email", email));

        return listRepairOrdersByCustomerUseCase.execute(customerId);
    }

    public List<AverageWorkshopServiceExecutionTime> getAverageWorkshopServiceExecutionTime() {
        return averageWorkshopServiceExecutionTimeUseCase.execute();
    }

    private void publishStatusNotification(
            RepairOrder repairOrder) {

        var customer = repairOrder.getCustomer();

        eventPublisher.publishEvent(
                new RepairOrderStatusChangedEvent(
                        repairOrder.getId(),
                        repairOrder.getNumber(),
                        customer == null ? null : customer.getName(),
                        customer == null ? null : customer.getEmail(),
                        repairOrder.getStatus(),
                        Instant.now()
                )
        );
    }
}
