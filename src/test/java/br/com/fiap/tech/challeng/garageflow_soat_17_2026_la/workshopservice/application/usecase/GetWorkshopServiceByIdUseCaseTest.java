package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.model.WorkshopService;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.repository.WorkshopServiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetWorkshopServiceByIdUseCaseTest {

    @Mock
    private WorkshopServiceRepository repository;

    @InjectMocks
    private GetWorkshopServiceByIdUseCase useCase;

    @Test
    void shouldReturnServiceWhenFound() {
        WorkshopService svc = new WorkshopService("Service", new BigDecimal("50"));
        svc.setId("abc");

        when(repository.findById("abc")).thenReturn(Optional.of(svc));

        Optional<WorkshopService> result = useCase.execute("abc");

        assertTrue(result.isPresent());
        assertEquals("abc", result.get().getId());
    }

    @Test
    void shouldThrowWhenIdIsNullOrBlank() {
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> useCase.execute(null));
        assertEquals("Service ID cannot be empty", ex1.getMessage());

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> useCase.execute("   "));
        assertEquals("Service ID cannot be empty", ex2.getMessage());
    }

    @Test
    void shouldReturnEmptyWhenNotFound() {
        when(repository.findById("missing")).thenReturn(Optional.empty());

        Optional<WorkshopService> result = useCase.execute("missing");

        assertFalse(result.isPresent());
    }
}

