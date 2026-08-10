package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model.Part;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.repository.PartRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UpdatePartUseCase {

    private final PartRepository partRepository;

    public UpdatePartUseCase(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    public Part updatePartWithId(String id, Part updatedPart) {
        Part existingPart = partRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Part", "id", id));

        existingPart.update(
                updatedPart.getName(),
                updatedPart.getQuantity(),
                updatedPart.getPrice()
        );

        log.debug("[DEBUG] - UPDATED PART: {}", existingPart);
        return partRepository.save(existingPart);
    }
}
