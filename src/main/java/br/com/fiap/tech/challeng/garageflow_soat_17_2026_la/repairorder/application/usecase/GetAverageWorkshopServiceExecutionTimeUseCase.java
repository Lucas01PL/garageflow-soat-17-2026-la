package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.AverageWorkshopServiceExecutionTime;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderMonitoringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAverageWorkshopServiceExecutionTimeUseCase {

    private final RepairOrderMonitoringRepository repository;

    public List<AverageWorkshopServiceExecutionTime> execute() {
        return repository.findAverageWorkshopServiceExecutionTime();
    }
}