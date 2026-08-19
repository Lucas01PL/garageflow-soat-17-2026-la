package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model.Part;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.repository.PartRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PartListToBuyUseCase {

    public static final int DEFAULT_LOW_STOCK_THRESHOLD = 5;

    private final PartRepository partRepository;

    public PartListToBuyUseCase(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    public List<Part> findPartsToBuy(Integer threshold) {
        Integer appliedThreshold = threshold != null ? threshold : DEFAULT_LOW_STOCK_THRESHOLD;
        log.debug("[DEBUG] - GETTING PARTS WITH STOCK LESS THAN OR EQUAL TO {}", appliedThreshold);
        return partRepository.findLowStock(appliedThreshold);
    }
}
