package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.infrastructure.persistence.repository;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.model.WorkshopService;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.infrastructure.persistence.document.WorkshopServiceDocument;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.infrastructure.persistence.mongo.WorkshopServiceMongoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkshopServiceRepositoryImplTest {

    @Mock
    private WorkshopServiceMongoRepository mongoRepository;

    @InjectMocks
    private WorkshopServiceRepositoryImpl repository;

    @Test
    void shouldSaveAndReturnDomain() {
        WorkshopService domain = new WorkshopService("Test", new BigDecimal("12.34"));

        WorkshopServiceDocument saved = new WorkshopServiceDocument("Test", new BigDecimal("12.34"));
        saved.setId("id-1");

        when(mongoRepository.save(any())).thenReturn(saved);

        WorkshopService result = repository.save(domain);

        assertNotNull(result);
        assertEquals("id-1", result.getId());
        assertEquals(domain.getDescription(), result.getDescription());
        assertEquals(domain.getPrice(), result.getPrice());

        // verify mapping: the document passed to mongoRepository.save should have same fields
        ArgumentCaptor<WorkshopServiceDocument> captor = ArgumentCaptor.forClass(WorkshopServiceDocument.class);
        verify(mongoRepository).save(captor.capture());
        WorkshopServiceDocument passed = captor.getValue();
        assertEquals(domain.getDescription(), passed.getDescription());
        assertEquals(domain.getPrice(), passed.getPrice());
    }

    @Test
    void shouldDelegateExistsById() {
        when(mongoRepository.existsById("x")).thenReturn(true);
        assertTrue(repository.existsById("x"));

        when(mongoRepository.existsById("y")).thenReturn(false);
        assertFalse(repository.existsById("y"));
    }

    @Test
    void shouldDeleteById() {
        doNothing().when(mongoRepository).deleteById("d1");

        repository.deleteById("d1");

        verify(mongoRepository).deleteById("d1");
    }

    @Test
    void shouldFindByIdAndMapToDomain() {
        WorkshopServiceDocument doc = new WorkshopServiceDocument("Desc", new BigDecimal("5"));
        doc.setId("z");

        when(mongoRepository.findById("z")).thenReturn(Optional.of(doc));

        Optional<WorkshopService> result = repository.findById("z");

        assertTrue(result.isPresent());
        assertEquals("z", result.get().getId());
        assertEquals(doc.getDescription(), result.get().getDescription());
    }

    @Test
    void shouldReturnEmptyWhenFindByIdNotFound() {
        when(mongoRepository.findById("missing")).thenReturn(Optional.empty());

        Optional<WorkshopService> result = repository.findById("missing");

        assertFalse(result.isPresent());
    }

    @Test
    void shouldFindAllAndMapToDomainList() {
        WorkshopServiceDocument d1 = new WorkshopServiceDocument("A", new BigDecimal("1"));
        d1.setId("1");
        WorkshopServiceDocument d2 = new WorkshopServiceDocument("B", new BigDecimal("2"));
        d2.setId("2");

        when(mongoRepository.findAll()).thenReturn(Arrays.asList(d1, d2));

        List<WorkshopService> all = repository.findAll();

        assertNotNull(all);
        assertEquals(2, all.size());
        assertEquals("1", all.get(0).getId());
        assertEquals("B", all.get(1).getDescription());
    }

    @Test
    void shouldFindByDescriptionContainingIgnoreCase() {
        WorkshopServiceDocument d = new WorkshopServiceDocument("Oil change", new BigDecimal("30"));
        d.setId("o1");

        when(mongoRepository.findByDescriptionContainingIgnoreCase("oil")).thenReturn(List.of(d));

        List<WorkshopService> result = repository.findByDescriptionContainingIgnoreCase("oil");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("o1", result.get(0).getId());
        assertEquals("Oil change", result.get(0).getDescription());
    }
}

