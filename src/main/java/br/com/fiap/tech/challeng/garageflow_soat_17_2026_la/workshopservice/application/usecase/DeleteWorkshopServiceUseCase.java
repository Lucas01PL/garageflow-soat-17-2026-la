package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.RequiredFieldException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.repository.WorkshopServiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class DeleteWorkshopServiceUseCase {

    private WorkshopServiceRepository repository;

    public boolean execute(String id) {
        if (id == null || id.isBlank()) {
            throw new RequiredFieldException("id");
        }
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("WorkshopService", "id", id);
        }
        repository.deleteById(id);
        return true;
    }
}

