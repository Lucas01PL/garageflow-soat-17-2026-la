package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.repository;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model.PurchaseList;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model.PurchaseListStatus;

import java.util.List;
import java.util.Optional;

public interface PurchaseListRepository {

    PurchaseList save(PurchaseList purchaseList);

    Optional<PurchaseList> findById(String id);

    List<PurchaseList> findAll();

    List<PurchaseList> findByStatus(PurchaseListStatus status);
}
