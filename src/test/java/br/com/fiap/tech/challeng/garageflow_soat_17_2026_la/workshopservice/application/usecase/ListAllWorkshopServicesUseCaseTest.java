package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.model.WorkshopService;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.repository.WorkshopServiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListAllWorkshopServicesUseCaseTest {

    @Mock
    private WorkshopServiceRepository repository;

    @InjectMocks
    private ListAllWorkshopServicesUseCase useCase;

    @Test
    void shouldReturnAllServices() {
        WorkshopService s1 = new WorkshopService("A", new BigDecimal("10"));
        WorkshopService s2 = new WorkshopService("B", new BigDecimal("20"));

        when(repository.findAll()).thenReturn(Arrays.asList(s1, s2));

        List<WorkshopService> result = useCase.execute();

        assertNotNull(result);
        assertEquals(2, result.size());
    }
}

