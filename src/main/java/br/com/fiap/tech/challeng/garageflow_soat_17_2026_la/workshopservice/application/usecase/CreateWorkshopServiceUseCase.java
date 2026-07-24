package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.model.WorkshopService;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.repository.WorkshopServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CreateWorkshopServiceUseCase {

    @Autowired
    private WorkshopServiceRepository repository;

    public WorkshopService execute(WorkshopService service) {
        if (service == null) {
            throw new IllegalArgumentException("Service cannot be null");
        }
        if (service.getDescription() == null || service.getDescription().isBlank()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }
        if (service.getValue() == null || service.getValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Value must be greater than zero");
        }

        return repository.save(service);
    }
}

