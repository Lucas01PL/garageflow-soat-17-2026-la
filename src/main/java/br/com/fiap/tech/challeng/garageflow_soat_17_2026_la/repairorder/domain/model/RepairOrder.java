package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.RepairOrderStatus;
import lombok.Builder;
import lombok.Getter;
import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

        this.workshopServices.add(workshopService);
        repairOrderInDiagnosis();
        recalculateTotals();
    }

    public void addPart(PartSnapshot partSnapshot) {

        Objects.requireNonNull(partSnapshot);

        validateCanModifyItems();

        this.parts.add(partSnapshot);
        repairOrderInDiagnosis();
        recalculateTotals();
    }

    public void repairOrderReceived() {
        status = RepairOrderStatus.RECEIVED;
        createdDate = LocalDateTime.now();
        initDate = LocalDateTime.now();
    }

    public void repairOrderInDiagnosis() {
        if (status != RepairOrderStatus.IN_DIAGNOSIS) {
            status = RepairOrderStatus.IN_DIAGNOSIS;
        }
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

}

