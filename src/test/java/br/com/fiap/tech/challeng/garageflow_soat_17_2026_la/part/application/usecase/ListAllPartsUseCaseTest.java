package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model.Part;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.repository.PartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListAllPartsUseCaseTest {

    @Mock
    private PartRepository partRepository;

    @InjectMocks
    private ListAllPartsUseCase listAllPartsUseCase;

    @Test
    void shouldReturnAllParts() {
        Part part1 = new Part("id-1", "P001", "Filtro de oleo", 10, new BigDecimal("29.90"));
        Part part2 = new Part("id-2", "P002", "Pastilha de freio", 5, new BigDecimal("89.90"));
        when(partRepository.findAll()).thenReturn(List.of(part1, part2));

        List<Part> result = listAllPartsUseCase.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnEmptyListWhenNoPartsExist() {
        when(partRepository.findAll()).thenReturn(Collections.emptyList());

        List<Part> result = listAllPartsUseCase.findAll();

        assertTrue(result.isEmpty());
    }
}
