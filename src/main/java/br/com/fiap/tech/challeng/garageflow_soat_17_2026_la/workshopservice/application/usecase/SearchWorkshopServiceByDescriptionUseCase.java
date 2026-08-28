package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.RequiredFieldException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.model.WorkshopService;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.repository.WorkshopServiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class SearchWorkshopServiceByDescriptionUseCase {

    private WorkshopServiceRepository repository;

    public List<WorkshopService> execute(String description) {
        if (description == null || description.isBlank()) {
            throw new RequiredFieldException("description");
        }
        return repository.findByDescriptionContainingIgnoreCase(description);
    }
}

