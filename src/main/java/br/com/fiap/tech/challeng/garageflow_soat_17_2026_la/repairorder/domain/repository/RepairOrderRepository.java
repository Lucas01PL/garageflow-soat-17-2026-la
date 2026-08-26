package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.RepairOrderStatus;

import java.util.List;
import java.util.Optional;

public interface RepairOrderRepository {

    RepairOrder save(RepairOrder repairOrder);

    boolean existsById(String id);

    void deleteById(String id);

    Optional<RepairOrder> findById(String id);

    List<RepairOrder> findAll();

    List<RepairOrder> findByStatusContainingIgnoreCase(String status);

    List<RepairOrder> findByCustomerId(String customerId);

    boolean existsByPartIdAndStatusIn(String partId, List<RepairOrderStatus> status);

}

