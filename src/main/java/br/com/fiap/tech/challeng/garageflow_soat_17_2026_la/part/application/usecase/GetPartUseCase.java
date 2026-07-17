package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.exception.PartNotFoundException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model.Part;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.repository.PartRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class GetPartUseCase {

    private final PartRepository partRepository;

    public GetPartUseCase(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    public Optional<Part> getPartbyCode(String code) {
        Optional<Part> byCode = partRepository.findByCode(code);
        if(byCode.isPresent()){
            log.debug("[DEBUG] - GET part: {}", byCode);
            return byCode;
        }
        throw new PartNotFoundException("There is no part with code " + code);
    }

    public Optional<Part> getPartbyId(String id) {
        Optional<Part> byId = partRepository.findByCode(id);
        if(byId.isPresent()){
            return byId;
        }
        throw new PartNotFoundException(id);
    }
}
