package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.repository;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model.Part;

import java.util.List;
import java.util.Optional;

public interface PartRepository {

    Part save(Part part);

    Optional<Part> findById(String id);

    Optional<Part> findByCode(String codigo);

    List<Part> findAll();

    void delete(String id);

    boolean existsByCode(String code);

    List<Part> findLowStock(Integer threshold);
}
