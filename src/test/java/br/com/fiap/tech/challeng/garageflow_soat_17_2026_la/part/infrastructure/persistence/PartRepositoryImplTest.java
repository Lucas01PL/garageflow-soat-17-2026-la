package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.infrastructure.persistence;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model.Part;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PartRepositoryImplTest {

    @Mock
    private PartMongoRepository partMongoRepository;

    @InjectMocks
    private PartRepositoryImpl partRepository;

    @Test
    void shouldSaveAndReturnDomain() {
        Part part = new Part("id-1", "P001", "Filtro de oleo", 10, new BigDecimal("29.90"));
        PartDocument savedDocument = new PartDocument("P001", "Filtro de oleo", 10, new BigDecimal("29.90"));
        savedDocument.setId("id-1");

        when(partMongoRepository.save(org.mockito.ArgumentMatchers.any())).thenReturn(savedDocument);

        Part result = partRepository.save(part);

        assertEquals("id-1", result.getId());
        assertEquals("P001", result.getCode());

        ArgumentCaptor<PartDocument> captor = ArgumentCaptor.forClass(PartDocument.class);
        verify(partMongoRepository).save(captor.capture());
        assertEquals("id-1", captor.getValue().getId());
        assertEquals("P001", captor.getValue().getCode());
    }

    @Test
    void shouldFindByIdAndMapToDomain() {
        PartDocument document = new PartDocument("P001", "Filtro de oleo", 10, new BigDecimal("29.90"));
        document.setId("id-1");
        when(partMongoRepository.findById("id-1")).thenReturn(Optional.of(document));

        Optional<Part> result = partRepository.findById("id-1");

        assertTrue(result.isPresent());
        assertEquals("id-1", result.get().getId());
    }

    @Test
    void shouldReturnEmptyWhenFindByIdNotFound() {
        when(partMongoRepository.findById("missing")).thenReturn(Optional.empty());

        Optional<Part> result = partRepository.findById("missing");

        assertFalse(result.isPresent());
    }

    @Test
    void shouldFindByCodeAndMapToDomain() {
        PartDocument document = new PartDocument("P001", "Filtro de oleo", 10, new BigDecimal("29.90"));
        document.setId("id-1");
        when(partMongoRepository.findByCode("P001")).thenReturn(Optional.of(document));

        Optional<Part> result = partRepository.findByCode("P001");

        assertTrue(result.isPresent());
        assertEquals("P001", result.get().getCode());
    }

    @Test
    void shouldReturnEmptyWhenFindByCodeNotFound() {
        when(partMongoRepository.findByCode("missing")).thenReturn(Optional.empty());

        Optional<Part> result = partRepository.findByCode("missing");

        assertFalse(result.isPresent());
    }

    @Test
    void shouldFindAllAndMapToDomainList() {
        PartDocument document1 = new PartDocument("P001", "Filtro de oleo", 10, new BigDecimal("29.90"));
        document1.setId("id-1");
        PartDocument document2 = new PartDocument("P002", "Pastilha de freio", 5, new BigDecimal("89.90"));
        document2.setId("id-2");
        when(partMongoRepository.findAll()).thenReturn(List.of(document1, document2));

        List<Part> result = partRepository.findAll();

        assertEquals(2, result.size());
        assertEquals("id-1", result.get(0).getId());
        assertEquals("id-2", result.get(1).getId());
    }

    @Test
    void shouldDeleteById() {
        partRepository.delete("id-1");

        verify(partMongoRepository).deleteById("id-1");
    }

    @Test
    void shouldFindLowStockAndMapToDomainList() {
        PartDocument document = new PartDocument("P001", "Filtro de oleo", 2, new BigDecimal("29.90"));
        document.setId("id-1");
        when(partMongoRepository.findByQuantityLessThanEqual(5)).thenReturn(List.of(document));

        List<Part> result = partRepository.findLowStock(5);

        assertEquals(1, result.size());
        assertEquals("id-1", result.get(0).getId());
    }

    @Test
    void shouldDelegateExistsByCode() {
        when(partMongoRepository.existsByCode("P001")).thenReturn(true);

        assertTrue(partRepository.existsByCode("P001"));

        when(partMongoRepository.existsByCode("P002")).thenReturn(false);

        assertFalse(partRepository.existsByCode("P002"));
    }
}
