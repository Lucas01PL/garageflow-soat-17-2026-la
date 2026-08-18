package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.infrastructure.persistence;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model.PurchaseList;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model.PurchaseListItem;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model.PurchaseListStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PurchaseListRepositoryImplTest {

    @Mock
    private PurchaseListMongoRepository purchaseListMongoRepository;

    @InjectMocks
    private PurchaseListRepositoryImpl repository;

    private PurchaseListDocument sampleDocument() {
        PurchaseListDocument document = new PurchaseListDocument();
        document.setId("pl-1");
        document.setGeneratedAt(LocalDateTime.now());
        document.setStatus(PurchaseListStatus.PENDING);
        document.setItems(List.of(new PurchaseListItemDocument("part-1", "Filtro de oleo", 2, 8, new BigDecimal("29.90"))));
        return document;
    }

    @Test
    void shouldSaveAndReturnDomain() {
        PurchaseListDocument saved = sampleDocument();
        when(purchaseListMongoRepository.save(any())).thenReturn(saved);

        PurchaseList purchaseList = PurchaseList.builder()
                .id("pl-1")
                .status(PurchaseListStatus.PENDING)
                .items(List.of(new PurchaseListItem("part-1", "Filtro de oleo", 2, 8, new BigDecimal("29.90"))))
                .build();

        PurchaseList result = repository.save(purchaseList);

        assertEquals("pl-1", result.getId());
        assertEquals(1, result.getItems().size());
        assertEquals("part-1", result.getItems().get(0).getPartId());

        ArgumentCaptor<PurchaseListDocument> captor = ArgumentCaptor.forClass(PurchaseListDocument.class);
        verify(purchaseListMongoRepository).save(captor.capture());
        assertEquals("pl-1", captor.getValue().getId());
        assertEquals(1, captor.getValue().getItems().size());
    }

    @Test
    void shouldFindByIdAndMapToDomain() {
        when(purchaseListMongoRepository.findById("pl-1")).thenReturn(Optional.of(sampleDocument()));

        Optional<PurchaseList> result = repository.findById("pl-1");

        assertTrue(result.isPresent());
        assertEquals("pl-1", result.get().getId());
    }

    @Test
    void shouldReturnEmptyWhenFindByIdNotFound() {
        when(purchaseListMongoRepository.findById("missing")).thenReturn(Optional.empty());

        assertFalse(repository.findById("missing").isPresent());
    }

    @Test
    void shouldFindAllAndMapToDomainList() {
        when(purchaseListMongoRepository.findAll()).thenReturn(List.of(sampleDocument()));

        List<PurchaseList> result = repository.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void shouldFindByStatusAndMapToDomainList() {
        when(purchaseListMongoRepository.findByStatus(PurchaseListStatus.PENDING)).thenReturn(List.of(sampleDocument()));

        List<PurchaseList> result = repository.findByStatus(PurchaseListStatus.PENDING);

        assertEquals(1, result.size());
        verify(purchaseListMongoRepository).findByStatus(PurchaseListStatus.PENDING);
    }
}
