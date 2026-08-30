package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.InvalidFieldValueException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.RequiredFieldException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.RequiredObjectException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.model.WorkshopService;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.repository.WorkshopServiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@AllArgsConstructor
@Service
public class CreateWorkshopServiceUseCase {

    private WorkshopServiceRepository repository;

    public WorkshopService execute(WorkshopService service) {
        if (service == null) {
            throw new RequiredObjectException("Service");
        }
        if (service.getDescription() == null || service.getDescription().isBlank()) {
            throw new RequiredFieldException("description");
        }
        if (service.getPrice() == null || service.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidFieldValueException("price", "Price must be greater than zero.");
        }

        return repository.save(service);
    }
}

