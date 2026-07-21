package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.model.WorkshopService;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.repository.WorkshopServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetWorkshopServiceByIdUseCase {

    @Autowired
    private WorkshopServiceRepository repository;

    public Optional<WorkshopService> execute(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Service ID cannot be empty");
        }
        return repository.findById(id);
    }
}

