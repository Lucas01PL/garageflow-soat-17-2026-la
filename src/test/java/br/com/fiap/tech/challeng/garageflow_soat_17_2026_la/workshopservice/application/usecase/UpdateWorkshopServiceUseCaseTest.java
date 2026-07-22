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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateWorkshopServiceUseCaseTest {

    @Mock
    private WorkshopServiceRepository repository;

    @InjectMocks
    private UpdateWorkshopServiceUseCase useCase;

    @Test
    void shouldUpdateDescriptionAndValueWhenProvided() {
        WorkshopService existing = new WorkshopService("Old", new BigDecimal("50"));
        existing.setId("1");

        WorkshopService update = new WorkshopService("New", new BigDecimal("100"));

        when(repository.findById("1")).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<WorkshopService> result = useCase.execute("1", update);

        assertTrue(result.isPresent());
        assertEquals("New", result.get().getDescription());
        assertEquals(new BigDecimal("100"), result.get().getValue());
        verify(repository).save(existing);
    }

    @Test
    void shouldIgnoreInvalidValueInUpdate() {
        WorkshopService existing = new WorkshopService("Desc", new BigDecimal("75"));
        existing.setId("2");

        WorkshopService update = new WorkshopService(null, BigDecimal.ZERO);

        when(repository.findById("2")).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<WorkshopService> result = useCase.execute("2", update);

        assertTrue(result.isPresent());
        // description not updated (null), value not updated (zero is ignored)
        assertEquals("Desc", result.get().getDescription());
        assertEquals(new BigDecimal("75"), result.get().getValue());
    }

    @Test
    void shouldReturnEmptyWhenServiceNotFound() {
        when(repository.findById("x")).thenReturn(Optional.empty());

        Optional<WorkshopService> result = useCase.execute("x", new WorkshopService("a", new BigDecimal("1")));

        assertFalse(result.isPresent());
    }

    @Test
    void shouldThrowWhenIdIsNullOrBlank() {
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> useCase.execute(null, new WorkshopService()));
        assertEquals("Service ID cannot be empty", ex1.getMessage());

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> useCase.execute("  ", new WorkshopService()));
        assertEquals("Service ID cannot be empty", ex2.getMessage());
    }
}

