package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.InvalidFieldValueException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.RequiredFieldException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.RequiredObjectException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.model.WorkshopService;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.repository.WorkshopServiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateWorkshopServiceUseCaseTest {

    @Mock
    private WorkshopServiceRepository repository;

    @InjectMocks
    private CreateWorkshopServiceUseCase useCase;

    @Test
    void shouldCreateServiceSuccessfully() {
        WorkshopService input = new WorkshopService("Oil change", new BigDecimal("99.90"));
        WorkshopService saved = new WorkshopService("Oil change", new BigDecimal("99.90"));
        saved.setId("1");

        when(repository.save(any())).thenReturn(saved);

        WorkshopService result = useCase.execute(input);

        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals(input.getDescription(), result.getDescription());
        verify(repository).save(input);
    }

    @Test
    void shouldThrowWhenServiceIsNull() {
        RequiredObjectException ex = assertThrows(RequiredObjectException.class, () -> useCase.execute(null));
        assertEquals("Service cannot be null.", ex.getMessage());
    }

    @Test
    void shouldThrowWhenDescriptionIsEmpty() {
        WorkshopService svc = new WorkshopService("", new BigDecimal("10"));
        RequiredFieldException ex = assertThrows(RequiredFieldException.class, () -> useCase.execute(svc));
        assertEquals("Field 'description' is required.", ex.getMessage());
    }

    @Test
    void shouldThrowWhenValueNotGreaterThanZero() {
        WorkshopService svc = new WorkshopService("Test", BigDecimal.ZERO);
        InvalidFieldValueException ex = assertThrows(InvalidFieldValueException.class, () -> useCase.execute(svc));
        assertEquals("Field 'price' is invalid. Price must be greater than zero.", ex.getMessage());
    }
}

