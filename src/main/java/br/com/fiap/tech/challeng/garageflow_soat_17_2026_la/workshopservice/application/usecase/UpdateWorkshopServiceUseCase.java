package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.model.WorkshopService;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.repository.WorkshopServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UpdateWorkshopServiceUseCase {

    @Autowired
    private WorkshopServiceRepository repository;

    public Optional<WorkshopService> execute(String id, WorkshopService update) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Service ID cannot be empty");
        }
        Optional<WorkshopService> existing = repository.findById(id);
        if (existing.isEmpty()) {
            return Optional.empty();
        }
        WorkshopService svc = existing.get();
        if (update.getDescription() != null && !update.getDescription().isBlank()) {
            svc.setDescription(update.getDescription());
        }
        if (update.getValue() != null && update.getValue().compareTo(BigDecimal.ZERO) > 0) {
            svc.setValue(update.getValue());
        }
        return Optional.of(repository.save(svc));
    }
}

