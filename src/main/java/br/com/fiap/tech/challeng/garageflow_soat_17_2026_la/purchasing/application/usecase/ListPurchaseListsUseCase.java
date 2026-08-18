package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model.PurchaseList;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model.PurchaseListStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.repository.PurchaseListRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListPurchaseListsUseCase {

    private final PurchaseListRepository purchaseListRepository;

    public ListPurchaseListsUseCase(PurchaseListRepository purchaseListRepository) {
        this.purchaseListRepository = purchaseListRepository;
    }

    public List<PurchaseList> execute(PurchaseListStatus status) {
        return status != null ? purchaseListRepository.findByStatus(status) : purchaseListRepository.findAll();
    }
}
