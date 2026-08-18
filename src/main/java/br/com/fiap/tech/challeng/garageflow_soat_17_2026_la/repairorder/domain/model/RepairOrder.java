package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.RepairOrderStatus;
import lombok.Builder;
import lombok.Getter;
import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
                existingService -> {
                    if (existingService.getWorkshopServiceId().equals(workshopService.getWorkshopServiceId())) {
                        existingService.setQuantity(
                                existingService.getQuantity()
                                        + workshopService.getQuantity());
                    } else {
                        this.workshopServices.add(workshopService);
                    }
                },
                () -> {
                    this.workshopServices.add(workshopService);
                }
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
                existingPart -> {
                    if (existingPart.getId().equals(partSnapshot.getId())) {
                        existingPart.setQuantity(
                                existingPart.getQuantity()
                                        + partSnapshot.getQuantity());
                    } else {
                        this.parts.add(partSnapshot);
                    }
                },
                () -> {
                    this.parts.add(partSnapshot);
                }
        );
        recalculateTotals();
    }

    public void received() {
        status = RepairOrderStatus.RECEIVED;
        createdDate = LocalDateTime.now();
        initDate = LocalDateTime.now();
    }

    public void startInDiagnosis() {
        if (status != RepairOrderStatus.RECEIVED) {
            throw new IllegalArgumentException(
                    "Repair Order must be RECEIVED to start diagnosis.");
        }

        status = RepairOrderStatus.IN_DIAGNOSIS;
        updatedDate = LocalDateTime.now();
    }

    public void number() {
        number = "RO" + LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    private void validateCanModifyItems() {

        if (status != RepairOrderStatus.RECEIVED &&
                status != RepairOrderStatus.IN_DIAGNOSIS) {

            throw new IllegalArgumentException(
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

        updatedDate = LocalDateTime.now();

        // TODO: VERIFICAR A NECESSIDADE DE CRIAR O METODO.
//        estimatedDurationInMinutes =
//                workshopServices.stream()
//                        .mapToInt(service ->
//                                service.getEstimatedDurationInMinutes()
//                                        * service.getQuantity())
//                        .sum();
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
                .ifPresentOrElse((existingPart) -> {
                    if (existingPart.getQuantity() > partSnapshot.getQuantity()) {
                        existingPart.setQuantity(
                                existingPart.getQuantity() - partSnapshot.getQuantity());
                    } else if (existingPart.getQuantity().equals(partSnapshot.getQuantity())) {
                        parts.remove(existingPart);
                    } else {
                        throw new IllegalArgumentException("Quantity of Part is insufficient to remove the requested amount.");
                    }
                }, () -> {
                    throw new IllegalArgumentException("Part not found in repair order");
                });
        recalculateTotals();
    }

    public void removeWorkshopService(WorkshopServiceSnapshot workshopServiceSnapshot) {

        Objects.requireNonNull(workshopServiceSnapshot);

        validateCanModifyItems();

        workshopServices.stream()
                .filter(existingService -> existingService.getWorkshopServiceId().equals(workshopServiceSnapshot.getWorkshopServiceId()))
                .findFirst()
                .ifPresentOrElse((existingService) -> {
                    if (existingService.getQuantity() > workshopServiceSnapshot.getQuantity()) {
                        existingService.setQuantity(
                                existingService.getQuantity() - workshopServiceSnapshot.getQuantity());
                    } else if (existingService.getQuantity().equals(workshopServiceSnapshot.getQuantity())) {
                        workshopServices.remove(existingService);
                    } else {
                        throw new IllegalArgumentException("Quantity of Workshop Service is insufficient to remove the requested amount.");
                    }
                }, () -> {
                    throw new IllegalArgumentException("Workshop Service not found in repair order");
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

        WorkshopServiceSnapshot workshopService = findWorkshopService(workshopServiceId);

        if (!workshopService.isWaitingAttending()) {
            throw new IllegalArgumentException("Workshop service not waiting attending");
        }

        workshopService.start();
        updatedDate = LocalDateTime.now();
    }

    public void finishWorkshopService(
            String workshopServiceId,
            Integer durationInMinutes) {

        WorkshopServiceSnapshot workshopService =
                findWorkshopService(workshopServiceId);

        if (!workshopService.isInExecution()) {
            throw new IllegalArgumentException("Workshop service not in execution");
        }

        if (durationInMinutes == null || durationInMinutes <= 0) {
            throw new IllegalArgumentException(
                    "Duration must be greater than zero.");
        }

        workshopService.finish(durationInMinutes);

        updatedDate = LocalDateTime.now();
    }

    public void approve() {

        if (!isAwaitingApproval()) {
            throw new IllegalStateException(
                    "Repair Order must be awaiting customer approval.");
        }

        status = RepairOrderStatus.APPROVED;
        updatedDate = LocalDateTime.now();
    }

    private boolean isAwaitingApproval() {
        return status == RepairOrderStatus.AWAITING_APPROVAL;
    }

    private boolean isApproved() {
        return status == RepairOrderStatus.APPROVED;
    }

    public void reject() {

        if (!isAwaitingApproval()) {
            throw new IllegalStateException(
                    "Repair Order must be awaiting customer approval.");
        }

        status = RepairOrderStatus.REJECTED;
        updatedDate = LocalDateTime.now();
    }

    public void deliver() {

        if (status != RepairOrderStatus.FINISHED) {
            throw new IllegalStateException(
                    "Repair Order must be FINISHED to be delivered."
            );
        }

        status = RepairOrderStatus.DELIVERED;
        updatedDate = LocalDateTime.now();
    }

    public void startExecution() {

        if (!isApproved()) {
            throw new IllegalStateException(
                    "Repair Order must be APPROVED to start execution."
            );
        }

        status = RepairOrderStatus.IN_EXECUTION;
        updatedDate = LocalDateTime.now();
    }

    public void requestApproval() {
        if (status != RepairOrderStatus.IN_DIAGNOSIS) {
            throw new IllegalStateException(
                    "Repair Order must be in diagnosis to request approval."
            );
        }

        status = RepairOrderStatus.AWAITING_APPROVAL;
        updatedDate = LocalDateTime.now();
    }

    private WorkshopServiceSnapshot findWorkshopService(String workshopServiceId) {
        return workshopServices.stream()
                .filter(service -> service.getWorkshopServiceId().equals(workshopServiceId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Workshop service not found"));
    }
}

