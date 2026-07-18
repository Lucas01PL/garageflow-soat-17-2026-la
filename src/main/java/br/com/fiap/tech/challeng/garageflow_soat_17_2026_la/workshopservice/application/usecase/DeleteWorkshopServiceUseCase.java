package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.repository.WorkshopServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteWorkshopServiceUseCase {

    @Autowired
    private WorkshopServiceRepository repository;

    public boolean execute(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Service ID cannot be empty");
        }
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}

