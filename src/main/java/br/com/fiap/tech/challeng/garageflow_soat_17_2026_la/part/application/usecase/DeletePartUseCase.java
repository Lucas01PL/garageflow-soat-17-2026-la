package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.exception.PartNotFoundException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model.Part;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.repository.PartRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class DeletePartUseCase {

    private final PartRepository partRepository;

    public DeletePartUseCase(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    public void deletePart(String id){
        Optional<Part> byId = partRepository.findByCode(id);
        if(byId.isPresent()){
            log.debug("[DEBUG] - DELETE part: {}", byId.get());
            partRepository.delete(id);
        }
        throw new PartNotFoundException(id);
    }
}
