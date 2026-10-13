package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.application.usecase.GetCustomerUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.model.Customer;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase.*;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.AverageWorkshopServiceExecutionTime;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.request.*;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public RepairOrder addWorkshopService(String repairOrderId, AddWorkshopServiceRequest request) {
        return addWorkshopServiceUseCase.execute(repairOrderId, request);
    }

    public RepairOrder addPart(String repairOrderId, AddPartRequest request) {
        return addPartUseCase.execute(repairOrderId, request);
    }

    public RepairOrder removeWorkshopService(String repairOrderId, String workshopServiceId, RemoveWorkshopServiceRequest request) {
        return removeWorkshopServiceUseCase.execute(repairOrderId, workshopServiceId, request);
    }

    public RepairOrder removePart(String repairOrderId, String partId, RemovePartRequest request) {
        return removePartUseCase.execute(repairOrderId, partId, request);
    }

    public RepairOrder startDiagnosis(String repairOrderId) {
        return startRepairOrderDiagnosisUseCase.execute(repairOrderId);
    }

    public RepairOrder startWorkshopService(String repairOrderId, String workshopServiceId) {
        return startWorkshopServiceUseCase.execute(repairOrderId, workshopServiceId);
    }

    public RepairOrder finishWorkshopService(String repairOrderId, String workshopServiceId, FinishWorkshopServiceRequest request) {
        return finishWorkshopServiceUseCase.execute(repairOrderId, workshopServiceId, request);
    }

    public RepairOrder approve(String repairOrderId, String userId) {
        return approveRepairOrderUseCase.execute(repairOrderId, userId);
    }

    public RepairOrder reject(String repairOrderId) {
        return rejectRepairOrderUseCase.execute(repairOrderId);
    }

    public RepairOrder deliver(String repairOrderId) {
        return deliverRepairOrderUseCase.execute(repairOrderId);
    }

    public RepairOrder startExecution(String repairOrderId) {
        return startRepairOrderExecutionUseCase.execute(repairOrderId);
    }

    public RepairOrder requestApproval(String repairOrderId) {
        return requestRepairOrderApprovalUseCase.execute(repairOrderId);
    }

    public RepairOrder cancel(String repairOrderId) {
        return cancelRepairOrderUseCase.execute(repairOrderId);
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
}
