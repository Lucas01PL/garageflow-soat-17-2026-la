package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model.Part;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.InvalidFieldValueException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PartSnapshotTest {

    @Test
    void shouldCreatePartSnapshotSuccessfully() {

        Part part = new Part(
                "part-1",
                "FIL-001",
                "Filtro de óleo",
                10,
                new BigDecimal("50.00")
        );

        PartSnapshot snapshot =
                PartSnapshot.from(part, 2);

        assertNotNull(snapshot);

        assertEquals(
                "part-1",
                snapshot.getId()
        );

        assertEquals(
                "FIL-001",
                snapshot.getCode()
        );

        assertEquals(
                "Filtro de óleo",
                snapshot.getDescription()
        );

        assertEquals(
                2,
                snapshot.getQuantity()
        );

        assertEquals(
                new BigDecimal("50.00"),
                snapshot.getUnitPrice()
        );
    }

    @Test
    void shouldCreateSnapshotWithQuantityOne() {

        Part part = new Part(
                "part-1",
                "FIL-001",
                "Filtro de óleo",
                10,
                new BigDecimal("50.00")
        );

        PartSnapshot snapshot =
                PartSnapshot.from(part, 1);

        assertEquals(
                1,
                snapshot.getQuantity()
        );
    }

    @Test
    void shouldThrowWhenPartIsNull() {

        NullPointerException exception =
                assertThrows(
                        NullPointerException.class,
                        () -> PartSnapshot.from(null, 1)
                );

        assertNotNull(exception);
    }

    @Test
    void shouldThrowWhenQuantityIsNull() {

        Part part = new Part(
                "part-1",
                "FIL-001",
                "Filtro de óleo",
                10,
                new BigDecimal("50.00")
        );

        NullPointerException exception =
                assertThrows(
                        NullPointerException.class,
                        () -> PartSnapshot.from(part, null)
                );

        assertNotNull(exception);
    }

    @Test
    void shouldThrowWhenQuantityIsZero() {

        Part part = new Part(
                "part-1",
                "FIL-001",
                "Filtro de óleo",
                10,
                new BigDecimal("50.00")
        );

        InvalidFieldValueException exception =
                assertThrows(
                        InvalidFieldValueException.class,
                        () -> PartSnapshot.from(part, 0)
                );

        assertEquals(
                "Field 'quantity' is invalid. Part quantity must be greater than zero",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowWhenQuantityIsNegative() {

        Part part = new Part(
                "part-1",
                "FIL-001",
                "Filtro de óleo",
                10,
                new BigDecimal("50.00")
        );

        InvalidFieldValueException exception =
                assertThrows(
                        InvalidFieldValueException.class,
                        () -> PartSnapshot.from(part, -1)
                );

        assertEquals(
                "Field 'quantity' is invalid. Part quantity must be greater than zero",
                exception.getMessage()
        );
    }

    @Test
    void shouldCopyPartDataToSnapshot() {

        Part part = new Part(
                "part-1",
                "FIL-001",
                "Filtro de óleo",
                10,
                new BigDecimal("75.90")
        );

        PartSnapshot snapshot =
                PartSnapshot.from(part, 3);

        assertEquals(part.getId(), snapshot.getId());
        assertEquals(part.getCode(), snapshot.getCode());
        assertEquals(part.getName(), snapshot.getDescription());
        assertEquals(part.getPrice(), snapshot.getUnitPrice());

        // A quantidade do snapshot deve representar
        // a quantidade utilizada na RepairOrder.
        assertEquals(3, snapshot.getQuantity());
    }

    @Test
    void shouldUseRequestedQuantityInsteadOfPartStockQuantity() {

        Part part = new Part(
                "part-1",
                "FIL-001",
                "Filtro de óleo",
                100,
                new BigDecimal("50.00")
        );

        PartSnapshot snapshot =
                PartSnapshot.from(part, 7);

        assertEquals(
                7,
                snapshot.getQuantity()
        );

        assertEquals(
                100,
                part.getQuantity()
        );
    }
}