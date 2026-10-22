package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model.Part;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.InvalidFieldValueException;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

@NoArgsConstructor
@Data
public class PartSnapshot {
    private String id;
    private String code;
    private String description;
    private Integer quantity;
    private BigDecimal unitPrice;

    public PartSnapshot (String id, @NotNull(message = "Quantity cannot be null") @Positive(message = "Quantity must be greater than zero") @Min(value = 1, message = "Quantity must be a positive integer") Integer quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public static PartSnapshot from(Part part, Integer quantity) {

        Objects.requireNonNull(part);
        Objects.requireNonNull(quantity);

        if (quantity <= 0) {
            throw new InvalidFieldValueException("quantity", "Part quantity must be greater than zero");
        }

        PartSnapshot snapshot = new PartSnapshot();
        snapshot.setId(part.getId());
        snapshot.setCode(part.getCode());
        snapshot.setDescription(part.getName());
        snapshot.setQuantity(quantity);
        snapshot.setUnitPrice(part.getPrice());
        return snapshot;
    }
}
