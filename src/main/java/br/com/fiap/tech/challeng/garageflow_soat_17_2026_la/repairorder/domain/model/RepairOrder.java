package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.InsufficientQuantityException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.InvalidRepairOrderItemException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.InvalidRepairOrderStateException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.PartNotFoundException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.WorkshopServiceNotFoundException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.RepairOrderStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.WorkshopServiceStatus;
import lombok.Builder;
import lombok.Getter;
import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Builder
@Getter
public class RepairOrder {

    private String id;
    private String number;
    private RepairOrderStatus status;
    private LocalDateTime initDate;
    private LocalDateTime finishDate;
    private BigDecimal totalServices;
    private BigDecimal totalParts;
    private BigDecimal total;
    private CustomerSnapshot customer;
    private VehicleSnapshot vehicle;

    @Builder.Default
    private List<WorkshopServiceSnapshot> workshopServices = new ArrayList<>();

    @Builder.Default
    private List<PartSnapshot> parts = new ArrayList<>();
    private String userId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;


    public void addWorkshopService(
            WorkshopServiceSnapshot workshopService) {

        Objects.requireNonNull(workshopService);

        validateCanModifyItems();

        this.workshopServices.stream()
                .filter(existingWorkshopService -> existingWorkshopService.getWorkshopServiceId().equals(workshopService.getWorkshopServiceId()))
                .findFirst()
                .ifPresentOrElse(
                        existingService ->
                        existingService.setQuantity(
                                existingService.getQuantity()
                                        + workshopService.getQuantity()),
                () -> this.workshopServices.add(workshopService)
        );
        recalculateTotals();
    }

    public void addPart(PartSnapshot partSnapshot) {

        Objects.requireNonNull(partSnapshot);

        validateCanModifyItems();

        this.parts.stream()
                .filter(existingPart -> existingPart.getId().equals(partSnapshot.getId()))
                .findFirst()
                .ifPresentOrElse(
                existingPart ->
                        existingPart.setQuantity(
                                existingPart.getQuantity()
                                        + partSnapshot.getQuantity()),
                () -> this.parts.add(partSnapshot)
        );
        recalculateTotals();
    }

    public void received() {
        status = RepairOrderStatus.RECEIVED;
        createdDate = LocalDateTime.now(ZoneId.systemDefault());
        initDate = LocalDateTime.now(ZoneId.systemDefault());
    }

    public void startInDiagnosis() {
        if (status != RepairOrderStatus.RECEIVED) {
            throw new InvalidRepairOrderStateException(
                    "Repair Order must be RECEIVED to start diagnosis.");
        }

        status = RepairOrderStatus.IN_DIAGNOSIS;
        updatedDate = LocalDateTime.now(ZoneId.systemDefault());
    }

    public void number() {
        number = "RO" + LocalDateTime.now(ZoneId.systemDefault()).format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    private void validateCanModifyItems() {

        if (status != RepairOrderStatus.RECEIVED &&
                status != RepairOrderStatus.IN_DIAGNOSIS) {

            throw new InvalidRepairOrderItemException(
                    "Repair Order must be RECEIVED or IN DIAGNOSIS.");
        }
    }

    private void recalculateTotals() {

        totalServices = calculateServicesTotals();

        totalParts = calculatePartsTotals();

        total = totalServices
                .add(totalParts);

        if (total.compareTo(BigDecimal.ZERO) < 0) {
            total = BigDecimal.ZERO;
        }

        updatedDate = LocalDateTime.now(ZoneId.systemDefault());
    }

    private @NonNull BigDecimal calculatePartsTotals() {
        return parts.stream()
                .map(part ->
                        part.getUnitPrice()
                                .multiply(BigDecimal.valueOf(part.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private @NonNull BigDecimal calculateServicesTotals() {
        return workshopServices.stream()
                .map(service ->
                        service.getUnitPrice()
                                .multiply(BigDecimal.valueOf(service.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void removePart(PartSnapshot partSnapshot) {

        Objects.requireNonNull(partSnapshot);

        validateCanModifyItems();

        parts.stream()
                .filter(existingPart -> existingPart.getId().equals(partSnapshot.getId()))
                .findFirst()
                .ifPresentOrElse(existingPart -> {
                    if (existingPart.getQuantity() > partSnapshot.getQuantity()) {
                        existingPart.setQuantity(
                                existingPart.getQuantity() - partSnapshot.getQuantity());
                    } else if (existingPart.getQuantity().equals(partSnapshot.getQuantity())) {
                        parts.remove(existingPart);
                    } else {
                        throw new InsufficientQuantityException("Part", partSnapshot.getId());
                    }
                }, () -> {
                    throw new PartNotFoundException(partSnapshot.getId());
                });
        recalculateTotals();
    }

    public void removeWorkshopService(WorkshopServiceSnapshot workshopServiceSnapshot) {

        Objects.requireNonNull(workshopServiceSnapshot);

        validateCanModifyItems();

        workshopServices.stream()
                .filter(existingService -> existingService.getWorkshopServiceId().equals(workshopServiceSnapshot.getWorkshopServiceId()))
                .findFirst()
                .ifPresentOrElse(existingService -> {
                    if (existingService.getQuantity() > workshopServiceSnapshot.getQuantity()) {
                        existingService.setQuantity(
                                existingService.getQuantity() - workshopServiceSnapshot.getQuantity());
                    } else if (existingService.getQuantity().equals(workshopServiceSnapshot.getQuantity())) {
                        workshopServices.remove(existingService);
                    } else {
                        throw new InsufficientQuantityException("Workshop Service", workshopServiceSnapshot.getWorkshopServiceId());
                    }
                }, () -> {
                    throw new WorkshopServiceNotFoundException(workshopServiceSnapshot.getWorkshopServiceId());
                });
        recalculateTotals();
    }

    public List<PartSnapshot> getParts() {
        return Collections.unmodifiableList(parts);
    }

    public List<WorkshopServiceSnapshot> getWorkshopServices() {
        return Collections.unmodifiableList(workshopServices);
    }

    public void startWorkshopService(String workshopServiceId) {

        if (status != RepairOrderStatus.IN_EXECUTION) {
            throw new InvalidRepairOrderStateException(
                    "Repair Order must be IN_EXECUTION to start a workshop service."
            );
        }

        WorkshopServiceSnapshot workshopService = findWorkshopService(workshopServiceId);

        if (!workshopService.isWaitingAttending()) {
            throw new InvalidRepairOrderStateException("Workshop service not waiting attending");
        }

        workshopService.start();
        updatedDate = LocalDateTime.now(ZoneId.systemDefault());
    }

    public void finishWorkshopService(
            String workshopServiceId,
            Integer durationInMinutes) {

        if (status != RepairOrderStatus.IN_EXECUTION) {
            throw new InvalidRepairOrderStateException(
                    "Repair Order must be IN_EXECUTION to start a workshop service."
            );
        }

        WorkshopServiceSnapshot workshopService =
                findWorkshopService(workshopServiceId);

        if (!workshopService.isInExecution()) {
            throw new InvalidRepairOrderStateException("Workshop service not in execution");
        }

        if (durationInMinutes == null || durationInMinutes <= 0) {
            throw new InvalidRepairOrderItemException(
                    "Duration must be greater than zero.");
        }

        workshopService.finish(durationInMinutes);

        if (allWorkshopServicesFinished()) {
            finish();
        }

        updatedDate = LocalDateTime.now(ZoneId.systemDefault());
    }

    public void approve() {

        if (!isAwaitingApproval()) {
            throw new InvalidRepairOrderStateException(
                    "Repair Order must be awaiting customer approval.");
        }

        status = RepairOrderStatus.APPROVED;
        updatedDate = LocalDateTime.now(ZoneId.systemDefault());
    }

    private boolean isAwaitingApproval() {
        return status == RepairOrderStatus.AWAITING_APPROVAL;
    }

    private boolean isApproved() {
        return status == RepairOrderStatus.APPROVED;
    }

    public void reject() {

        if (!isAwaitingApproval()) {
            throw new InvalidRepairOrderStateException(
                    "Repair Order must be awaiting customer approval.");
        }

        status = RepairOrderStatus.REJECTED;
        updatedDate = LocalDateTime.now(ZoneId.systemDefault());
    }

    public void deliver() {

        if (status != RepairOrderStatus.FINISHED) {
            throw new InvalidRepairOrderStateException(
                    "Repair Order must be FINISHED to be delivered."
            );
        }

        status = RepairOrderStatus.DELIVERED;
        updatedDate = LocalDateTime.now(ZoneId.systemDefault());
    }

    public void startExecution() {

        if (!isApproved()) {
            throw new InvalidRepairOrderStateException(
                    "Repair Order must be APPROVED to start execution."
            );
        }

        status = RepairOrderStatus.IN_EXECUTION;
        updatedDate = LocalDateTime.now(ZoneId.systemDefault());
    }

    public void requestApproval() {
        validateCanRequestApproval();

        status = RepairOrderStatus.AWAITING_APPROVAL;
        updatedDate = LocalDateTime.now(ZoneId.systemDefault());
    }

    private void validateCanRequestApproval() {
        if (status != RepairOrderStatus.IN_DIAGNOSIS) {
            throw new InvalidRepairOrderStateException(
                    "Repair Order must be in diagnosis to request approval."
            );
        }

        if (workshopServices.isEmpty()) {
            throw new InvalidRepairOrderItemException(
                    "Repair Order must have at least one workshop service to request approval."
            );
        }
    }

    public void finish() {
        if (status != RepairOrderStatus.IN_EXECUTION) {
            throw new InvalidRepairOrderStateException(
                    "Repair Order must be in execution to be finished."
            );
        }

        status = RepairOrderStatus.FINISHED;
        finishDate = LocalDateTime.now(ZoneId.systemDefault());
        updatedDate = LocalDateTime.now(ZoneId.systemDefault());
    }

    private boolean allWorkshopServicesFinished() {
        return !workshopServices.isEmpty()
                && workshopServices.stream()
                .allMatch(service ->
                        service.getStatus() == WorkshopServiceStatus.FINISHED
                );
    }

    private WorkshopServiceSnapshot findWorkshopService(String workshopServiceId) {
        return workshopServices.stream()
                .filter(service -> service.getWorkshopServiceId().equals(workshopServiceId))
                .findFirst()
                .orElseThrow(() -> new WorkshopServiceNotFoundException(workshopServiceId));
    }
}

