package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model.Part;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.repository.PartRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UpdateUseCase {

    private final PartRepository partRepository;

    public UpdateUseCase(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    public Part updatePartWithId(String id, Part updatedPart) {
        Part existingPart = partRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id));

        existingPart.update(
                updatedPart.getName(),
                updatedPart.getQuantity(),
                updatedPart.getPrice()
        );

        log.debug("[DEBUG] - UPDATE part: {}", existingPart);
        return partRepository.save(existingPart);
    }
}
