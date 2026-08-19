package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model.PurchaseList;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.repository.PurchaseListRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class GetPurchaseListByIdUseCase {

    private final PurchaseListRepository purchaseListRepository;

    public GetPurchaseListByIdUseCase(PurchaseListRepository purchaseListRepository) {
        this.purchaseListRepository = purchaseListRepository;
    }

    public PurchaseList execute(String id) {
        return purchaseListRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseList", "id", id));
    }
}
