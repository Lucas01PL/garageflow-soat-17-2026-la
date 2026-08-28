package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.AverageWorkshopServiceExecutionTime;

import java.util.List;

public interface RepairOrderMonitoringRepository {

    List<AverageWorkshopServiceExecutionTime>
    findAverageWorkshopServiceExecutionTime();
}