package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model.Part;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.repository.PartRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.NotEnoughResourceException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class PartStockControlUseCase {

    private final PartRepository partRepository;

    public PartStockControlUseCase(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    public String debitPartStock(String id, Integer quantityToDebit){
        if(quantityToDebit == null || quantityToDebit <= 0){
            throw new NotEnoughResourceException("Quantity to be debited should be greater than 0");
        }

        Part existingPart = partRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Part", "id", id));

        if(existingPart.getQuantity() <= 0 || existingPart.getQuantity() < quantityToDebit){
            var returnMessage = String.format("%s has not enough stock, requested was: %d but actual stock is: %d",
                    existingPart.getName(), quantityToDebit, existingPart.getQuantity());
            log.debug(returnMessage);
            throw new NotEnoughResourceException(returnMessage);
        }

        existingPart.updateQuantity(existingPart.getQuantity() - quantityToDebit);
        partRepository.save(existingPart);

        return String.format("Debited successfully an amout of %s from part: %s with id: %s", quantityToDebit, existingPart.getName(), id);
    }

    public String addPartStock(String id, Integer quantityToAdd){
        Part existingPart = partRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Part", "id", id));

        if(quantityToAdd <= 0){
            throw new NotEnoughResourceException("Quantity to be added should be greater than 0");
        }

        existingPart.updateQuantity(existingPart.getQuantity() + quantityToAdd);
        partRepository.save(existingPart);

        log.debug("Added part {} with id: {} stock now is at {}", existingPart.getName(), existingPart.getId(), existingPart.getQuantity());
        return String.format("Added successfully an amout of %s from part: %s with id: %s", quantityToAdd, existingPart.getName(), id);
    }

}
