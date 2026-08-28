package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.mapper;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.WorkshopServiceSnapshot;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.WorkshopServiceStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.response.WorkshopServiceResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class WorkshopServiceResponseMapperTest {

    private final WorkshopServiceResponseMapper mapper =
            new WorkshopServiceResponseMapper();

    @Test
    void shouldMapWorkshopServiceSnapshotToResponseSuccessfully() {

        LocalDateTime startedAt =
                LocalDateTime.of(2026, 8, 19, 10, 30);

        LocalDateTime finishedAt =
                LocalDateTime.of(2026, 8, 19, 11, 15);

        WorkshopServiceSnapshot snapshot =
                new WorkshopServiceSnapshot();

        snapshot.setWorkshopServiceId("workshop-service-1");
        snapshot.setDescription("Troca de óleo");
        snapshot.setQuantity(2);
        snapshot.setUnitPrice(new BigDecimal("150.00"));
        snapshot.setDurationInMinutes(45);
        snapshot.setStatus(WorkshopServiceStatus.FINISHED);
        snapshot.setStartedAt(startedAt);
        snapshot.setFinishedAt(finishedAt);

        WorkshopServiceResponse response =
                mapper.toResponse(snapshot);

        assertNotNull(response);

        assertEquals(
                "workshop-service-1",
                response.getWorkshopServiceId()
        );

        assertEquals(
                "Troca de óleo",
                response.getDescription()
        );

        assertEquals(
                2,
                response.getQuantity()
        );

        assertEquals(
                new BigDecimal("150.00"),
                response.getUnitPrice()
        );

        assertEquals(
                45,
                response.getDurationInMinutes()
        );

        assertEquals(
                WorkshopServiceStatus.FINISHED.name(),
                response.getStatus()
        );

        assertEquals(
                startedAt,
                response.getStartedAt()
        );

        assertEquals(
                finishedAt,
                response.getFinishedAt()
        );
    }

    @Test
    void shouldReturnNullWhenSnapshotIsNull() {

        WorkshopServiceResponse response =
                mapper.toResponse(null);

        assertNull(response);
    }

    @Test
    void shouldMapWaitingAttendingStatus() {

        WorkshopServiceSnapshot snapshot =
                createSnapshot();

        snapshot.setStatus(
                WorkshopServiceStatus.WAITING_ATTENDING
        );

        WorkshopServiceResponse response =
                mapper.toResponse(snapshot);

        assertEquals(
                WorkshopServiceStatus.WAITING_ATTENDING.name(),
                response.getStatus()
        );
    }

    @Test
    void shouldMapInExecutionStatus() {

        WorkshopServiceSnapshot snapshot =
                createSnapshot();

        snapshot.setStatus(
                WorkshopServiceStatus.IN_EXECUTION
        );

        WorkshopServiceResponse response =
                mapper.toResponse(snapshot);

        assertEquals(
                WorkshopServiceStatus.IN_EXECUTION.name(),
                response.getStatus()
        );
    }

    @Test
    void shouldMapFinishedStatus() {

        WorkshopServiceSnapshot snapshot =
                createSnapshot();

        snapshot.setStatus(
                WorkshopServiceStatus.FINISHED
        );

        WorkshopServiceResponse response =
                mapper.toResponse(snapshot);

        assertEquals(
                WorkshopServiceStatus.FINISHED.name(),
                response.getStatus()
        );
    }

    @Test
    void shouldMapNullOptionalFields() {

        WorkshopServiceSnapshot snapshot =
                new WorkshopServiceSnapshot();

        snapshot.setWorkshopServiceId("workshop-service-1");
        snapshot.setDescription("Troca de óleo");
        snapshot.setQuantity(1);
        snapshot.setUnitPrice(new BigDecimal("100.00"));
        snapshot.setStatus(
                WorkshopServiceStatus.WAITING_ATTENDING
        );

        WorkshopServiceResponse response =
                mapper.toResponse(snapshot);

        assertNotNull(response);

        assertNull(
                response.getDurationInMinutes()
        );

        assertNull(
                response.getStartedAt()
        );

        assertNull(
                response.getFinishedAt()
        );
    }

    @Test
    void shouldPreserveDecimalUnitPrice() {

        WorkshopServiceSnapshot snapshot =
                createSnapshot();

        snapshot.setUnitPrice(
                new BigDecimal("199.99")
        );

        WorkshopServiceResponse response =
                mapper.toResponse(snapshot);

        assertEquals(
                new BigDecimal("199.99"),
                response.getUnitPrice()
        );
    }

    private WorkshopServiceSnapshot createSnapshot() {

        WorkshopServiceSnapshot snapshot =
                new WorkshopServiceSnapshot();

        snapshot.setWorkshopServiceId("workshop-service-1");
        snapshot.setDescription("Troca de óleo");
        snapshot.setQuantity(1);
        snapshot.setUnitPrice(
                new BigDecimal("150.00")
        );
        snapshot.setStatus(
                WorkshopServiceStatus.WAITING_ATTENDING
        );

        return snapshot;
    }
}