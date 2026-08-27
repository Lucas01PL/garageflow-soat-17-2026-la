package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.RequiredFieldException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.model.WorkshopService;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.repository.WorkshopServiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UpdateWorkshopServiceUseCase {

    private WorkshopServiceRepository repository;

    public Optional<WorkshopService> execute(String id, WorkshopService update) {
        if (id == null || id.isBlank()) {
            throw new RequiredFieldException("id");
        }
        Optional<WorkshopService> existing = repository.findById(id);
        if (existing.isEmpty()) {
            return Optional.empty();
        }
        WorkshopService svc = existing.get();
        if (update.getDescription() != null && !update.getDescription().isBlank()) {
            svc.setDescription(update.getDescription());
        }
        if (update.getPrice() != null && update.getPrice().compareTo(BigDecimal.ZERO) > 0) {
            svc.setPrice(update.getPrice());
        }
        return Optional.of(repository.save(svc));
    }
}

