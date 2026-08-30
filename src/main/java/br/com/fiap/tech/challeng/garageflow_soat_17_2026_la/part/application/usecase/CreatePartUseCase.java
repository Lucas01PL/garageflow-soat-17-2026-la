package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.exception.DuplicatePartException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model.Part;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.repository.PartRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.DuplicateResourceException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CreatePartUseCase {

    private final PartRepository partRepository;

    public CreatePartUseCase(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    public Part createPart(Part part){
        boolean isPresent = partRepository.existsByCode(part.getCode());
        if(isPresent){
            log.debug("[DEBUG] - Trying to register duplicated part with code: {}", part.getCode());
            throw new DuplicateResourceException("Part", "id", part.getCode());
        }

        log.debug("[DEBUG] - POST PART: {}", part);
        return partRepository.save(part);
    }
}
