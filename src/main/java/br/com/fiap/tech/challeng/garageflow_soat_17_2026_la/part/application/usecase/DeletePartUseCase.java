package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model.Part;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.repository.PartRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.InvalidPartException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.RepairOrderStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class DeletePartUseCase {

    private final PartRepository partRepository;

    private final RepairOrderRepository repairOrderRepository;

    public DeletePartUseCase(PartRepository partRepository, RepairOrderRepository repairOrderRepository) {
        this.partRepository = partRepository;
        this.repairOrderRepository = repairOrderRepository;
    }

    public void deletePart(String id){
         Part byId = partRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Part", "id", id));

        boolean usedInBlockingRepairOrder =
                repairOrderRepository.existsByPartIdAndStatusIn(
                        id,
                        RepairOrderStatus.preventingPartDeletion()
                );

        if (usedInBlockingRepairOrder) {
            throw new InvalidPartException(
                    "Part cannot be deleted because it is being used by a repair order."
            );
        }

        log.debug("[DEBUG] - DELETED PART: {}", byId);
        partRepository.delete(id);
    }
}
