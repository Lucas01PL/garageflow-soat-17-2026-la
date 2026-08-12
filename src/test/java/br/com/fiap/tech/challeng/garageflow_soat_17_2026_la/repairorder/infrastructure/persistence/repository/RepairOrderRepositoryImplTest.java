package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.infrastructure.persistence.repository;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.RepairOrderStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.infrastructure.persistence.document.RepairOrderDocument;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.infrastructure.persistence.mongo.RepairOrderMongoRepository;
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
class RepairOrderRepositoryImplTest {

    @Mock
    private RepairOrderMongoRepository mongoRepository;

    @InjectMocks
    private RepairOrderRepositoryImpl repository;

    @Test
    void shouldSaveAndReturnDomain() {
        RepairOrder domain = RepairOrder.builder()
                .status(RepairOrderStatus.RECEIVED)
                .total(new BigDecimal("500.00"))
                .userId("user1")
                .build();

        RepairOrderDocument saved = new RepairOrderDocument();
        saved.setStatus(RepairOrderStatus.RECEIVED);
        saved.setTotal(new BigDecimal("500.00"));
        saved.setUserId("user1");
        saved.setId("ro1");

        when(mongoRepository.save(any())).thenReturn(saved);

        RepairOrder result = repository.save(domain);

        assertNotNull(result);
        assertEquals("ro1", result.getId());
        assertEquals(domain.getStatus(), result.getStatus());

        // verify mapping
        ArgumentCaptor<RepairOrderDocument> captor = ArgumentCaptor.forClass(RepairOrderDocument.class);
        verify(mongoRepository).save(captor.capture());
        RepairOrderDocument passed = captor.getValue();
        assertEquals(domain.getStatus(), passed.getStatus());
    }

    @Test
    void shouldDelegateExistsById() {
        when(mongoRepository.existsById("ro1")).thenReturn(true);
        assertTrue(repository.existsById("ro1"));

        when(mongoRepository.existsById("ro2")).thenReturn(false);
        assertFalse(repository.existsById("ro2"));
    }

    @Test
    void shouldDeleteById() {
        doNothing().when(mongoRepository).deleteById("ro1");

        repository.deleteById("ro1");

        verify(mongoRepository).deleteById("ro1");
    }

    @Test
    void shouldFindByIdAndMapToDomain() {
        RepairOrderDocument doc = new RepairOrderDocument();
        doc.setStatus(RepairOrderStatus.RECEIVED);
        doc.setTotal(new BigDecimal("500.00"));
        doc.setUserId("user1");
        doc.setId("ro1");

        when(mongoRepository.findById("ro1")).thenReturn(Optional.of(doc));

        Optional<RepairOrder> result = repository.findById("ro1");

        assertTrue(result.isPresent());
        assertEquals("ro1", result.get().getId());
        assertEquals(doc.getStatus(), result.get().getStatus());
    }

    @Test
    void shouldReturnEmptyWhenFindByIdNotFound() {
        when(mongoRepository.findById("notfound")).thenReturn(Optional.empty());

        Optional<RepairOrder> result = repository.findById("notfound");

        assertFalse(result.isPresent());
    }

    @Test
    void shouldFindAllAndMapToDomainList() {
        RepairOrderDocument d1 = new RepairOrderDocument();
        d1.setStatus(RepairOrderStatus.RECEIVED);
        d1.setTotal(new BigDecimal("500.00"));
        d1.setUserId("user1");
        d1.setId("ro1");

        RepairOrderDocument d2 = new RepairOrderDocument();
        d2.setStatus(RepairOrderStatus.FINISHED);
        d2.setTotal(new BigDecimal("100.00"));
        d2.setUserId("user2");
        d2.setId("ro2");

        when(mongoRepository.findAll()).thenReturn(Arrays.asList(d1, d2));

        List<RepairOrder> all = repository.findAll();

        assertNotNull(all);
        assertEquals(2, all.size());
        assertEquals("ro1", all.getFirst().getId());
    }

    @Test
    void shouldFindByStatusContainingIgnoreCase() {
        RepairOrderDocument d = new RepairOrderDocument();
        d.setStatus(RepairOrderStatus.RECEIVED);
        d.setTotal(new BigDecimal("500.00"));
        d.setUserId("user1");
        d.setId("ro1");

        when(mongoRepository.findByStatusContainingIgnoreCase("PENDING")).thenReturn(List.of(d));

        List<RepairOrder> result = repository.findByStatusContainingIgnoreCase("PENDING");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ro1", result.getFirst().getId());
        assertEquals("RECEIVED", result.getFirst().getStatus().name());
    }
}

