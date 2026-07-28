package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model.Part;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.repository.PartRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ListAllPartsUseCase {

    private final PartRepository partRepository;

    public ListAllPartsUseCase(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    public List<Part> findAll() {
        log.debug("[DEBUG] - GETTING ALL PARTS");
        return partRepository.findAll();
    }
}
