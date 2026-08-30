package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.RequiredFieldException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.model.WorkshopService;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.repository.WorkshopServiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchWorkshopServiceByDescriptionUseCaseTest {

    @Mock
    private WorkshopServiceRepository repository;

    @InjectMocks
    private SearchWorkshopServiceByDescriptionUseCase useCase;

    @Test
    void shouldReturnMatchingServices() {
        WorkshopService s = new WorkshopService("Oil change", new BigDecimal("30"));
        when(repository.findByDescriptionContainingIgnoreCase("oil")).thenReturn(List.of(s));

        List<WorkshopService> result = useCase.execute("oil");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void shouldThrowWhenDescriptionIsEmpty() {
        RequiredFieldException ex = assertThrows(RequiredFieldException.class, () -> useCase.execute(""));
        assertEquals("Field 'description' is required.", ex.getMessage());
    }
}

