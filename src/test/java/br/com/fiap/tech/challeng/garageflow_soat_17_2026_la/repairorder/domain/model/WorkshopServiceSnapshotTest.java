package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.InvalidRepairOrderStateException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.WorkshopServiceStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.InvalidFieldValueException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.model.WorkshopService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WorkshopServiceSnapshotTest {

    @Test
    void shouldCreateWorkshopServiceSnapshotSuccessfully() {

        WorkshopService workshopService =
                new WorkshopService(
                        "Troca de óleo",
                        new BigDecimal("150.00")
                );

        workshopService.setId("workshop-service-1");

        WorkshopServiceSnapshot snapshot =
                WorkshopServiceSnapshot.from(
                        workshopService,
                        2
                );

        assertNotNull(snapshot);

        assertEquals(
                "workshop-service-1",
                snapshot.getWorkshopServiceId()
        );

        assertEquals(
                "Troca de óleo",
                snapshot.getDescription()
        );

        assertEquals(
                2,
                snapshot.getQuantity()
        );

        assertEquals(
                new BigDecimal("150.00"),
                snapshot.getUnitPrice()
        );

        assertEquals(
                WorkshopServiceStatus.WAITING_ATTENDING,
                snapshot.getStatus()
        );

        assertNull(snapshot.getStartedAt());
        assertNull(snapshot.getFinishedAt());
        assertNull(snapshot.getDurationInMinutes());
    }

    @Test
    void shouldCreateSnapshotWithQuantityOne() {

        WorkshopService workshopService =
                new WorkshopService(
                        "Troca de óleo",
                        new BigDecimal("150.00")
                );

        workshopService.setId("workshop-service-1");

        WorkshopServiceSnapshot snapshot =
                WorkshopServiceSnapshot.from(
                        workshopService,
                        1
                );

        assertEquals(
                1,
                snapshot.getQuantity()
        );
    }

    @Test
    void shouldThrowWhenWorkshopServiceIsNull() {

        NullPointerException exception =
                assertThrows(
                        NullPointerException.class,
                        () -> WorkshopServiceSnapshot.from(
                                null,
                                1
                        )
                );

        assertNotNull(exception);
    }

    @Test
    void shouldThrowWhenQuantityIsNull() {

        WorkshopService workshopService =
                new WorkshopService(
                        "Troca de óleo",
                        new BigDecimal("150.00")
                );

        workshopService.setId("workshop-service-1");

        NullPointerException exception =
                assertThrows(
                        NullPointerException.class,
                        () -> WorkshopServiceSnapshot.from(
                                workshopService,
                                null
                        )
                );

        assertNotNull(exception);
    }

    @Test
    void shouldThrowWhenQuantityIsZero() {

        WorkshopService workshopService =
                new WorkshopService(
                        "Troca de óleo",
                        new BigDecimal("150.00")
                );

        workshopService.setId("workshop-service-1");

        InvalidFieldValueException exception =
                assertThrows(
                        InvalidFieldValueException.class,
                        () -> WorkshopServiceSnapshot.from(
                                workshopService,
                                0
                        )
                );

        assertEquals(
                "Field 'quantity' is invalid. Workshop service quantity must be greater than zero.",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowWhenQuantityIsNegative() {

        WorkshopService workshopService =
                new WorkshopService(
                        "Troca de óleo",
                        new BigDecimal("150.00")
                );

        workshopService.setId("workshop-service-1");

        InvalidFieldValueException exception =
                assertThrows(
                        InvalidFieldValueException.class,
                        () -> WorkshopServiceSnapshot.from(
                                workshopService,
                                -1
                        )
                );

        assertEquals(
                "Field 'quantity' is invalid. Workshop service quantity must be greater than zero.",
                exception.getMessage()
        );
    }

    @Test
    void shouldStartWorkshopServiceSuccessfully() {

        WorkshopServiceSnapshot snapshot =
                createSnapshot();

        snapshot.start();

        assertEquals(
                WorkshopServiceStatus.IN_EXECUTION,
                snapshot.getStatus()
        );

        assertNotNull(snapshot.getStartedAt());

        assertNull(
                snapshot.getFinishedAt()
        );

        assertNull(
                snapshot.getDurationInMinutes()
        );
    }

    @Test
    void shouldReturnTrueWhenWorkshopServiceIsWaitingAttending() {

        WorkshopServiceSnapshot snapshot =
                createSnapshot();

        assertTrue(
                snapshot.isWaitingAttending()
        );

        assertFalse(
                snapshot.isInExecution()
        );
    }

    @Test
    void shouldReturnTrueWhenWorkshopServiceIsInExecution() {

        WorkshopServiceSnapshot snapshot =
                createSnapshot();

        snapshot.start();

        assertTrue(
                snapshot.isInExecution()
        );

        assertFalse(
                snapshot.isWaitingAttending()
        );
    }

    @Test
    void shouldFinishWorkshopServiceSuccessfully() {

        WorkshopServiceSnapshot snapshot =
                createSnapshot();

        snapshot.start();

        snapshot.finish(45);

        assertEquals(
                WorkshopServiceStatus.FINISHED,
                snapshot.getStatus()
        );

        assertEquals(
                45,
                snapshot.getDurationInMinutes()
        );

        assertNotNull(
                snapshot.getStartedAt()
        );

        assertNotNull(
                snapshot.getFinishedAt()
        );
    }

    @Test
    void shouldKeepStartedAtWhenFinishingWorkshopService() {

        WorkshopServiceSnapshot snapshot =
                createSnapshot();

        snapshot.start();

        var startedAt =
                snapshot.getStartedAt();

        snapshot.finish(45);

        assertSame(
                startedAt,
                snapshot.getStartedAt()
        );
    }

    @Test
    void shouldThrowWhenFinishingWorkshopServiceThatIsNotInExecution() {

        WorkshopServiceSnapshot snapshot = createSnapshot();

        InvalidRepairOrderStateException exception =
                assertThrows(
                        InvalidRepairOrderStateException.class,
                        () -> snapshot.finish(45)
                );

        assertEquals(
                "Invalid Repair Order State: Workshop service not in execution",
                exception.getMessage()
        );
    }

    private WorkshopServiceSnapshot createSnapshot() {

        WorkshopService workshopService =
                new WorkshopService(
                        "Troca de óleo",
                        new BigDecimal("150.00")
                );

        workshopService.setId("workshop-service-1");

        return WorkshopServiceSnapshot.from(
                workshopService,
                1
        );
    }
}