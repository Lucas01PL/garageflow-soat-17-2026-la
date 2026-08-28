package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.AverageWorkshopServiceExecutionTime;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderMonitoringRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAverageWorkshopServiceExecutionTimeUseCaseTest {

    @Mock
    private RepairOrderMonitoringRepository repository;

    @InjectMocks
    private GetAverageWorkshopServiceExecutionTimeUseCase useCase;

    @Test
    void shouldReturnAverageExecutionTimeByWorkshopService() {

        AverageWorkshopServiceExecutionTime metric =
                AverageWorkshopServiceExecutionTime.builder()
                        .workshopServiceId("service-1")
                        .description("Troca de óleo")
                        .completedServices(20)
                        .averageDurationInMinutes(
                                new BigDecimal("35.00")
                        )
                        .minimumDurationInMinutes(25)
                        .maximumDurationInMinutes(45)
                        .build();

        when(repository.findAverageWorkshopServiceExecutionTime())
                .thenReturn(List.of(metric));

        List<AverageWorkshopServiceExecutionTime> result =
                useCase.execute();

        assertEquals(1, result.size());

        assertSame(
                metric,
                result.getFirst()
        );

        verify(repository)
                .findAverageWorkshopServiceExecutionTime();
    }

    @Test
    void shouldReturnEmptyListWhenThereAreNoCompletedServices() {

        when(repository.findAverageWorkshopServiceExecutionTime())
                .thenReturn(List.of());

        List<AverageWorkshopServiceExecutionTime> result =
                useCase.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(repository)
                .findAverageWorkshopServiceExecutionTime();
    }
}