package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase.PartStockControlUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model.PurchaseList;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.repository.PurchaseListRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PurchasePurchaseListUseCase {

    private final PurchaseListRepository purchaseListRepository;
    private final PartStockControlUseCase partStockControlUseCase;

    public PurchasePurchaseListUseCase(PurchaseListRepository purchaseListRepository, PartStockControlUseCase partStockControlUseCase) {
        this.purchaseListRepository = purchaseListRepository;
        this.partStockControlUseCase = partStockControlUseCase;
    }

    public PurchaseList execute(String id) {
        PurchaseList purchaseList = purchaseListRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseList", "id", id));

        purchaseList.markAsPurchased();

        purchaseList.getItems().forEach(item -> {
            partStockControlUseCase.addPartStock(item.getPartId(), item.getQuantityToBuy());
            log.debug("[DEBUG] - RESTOCKED PART {} WITH {} UNITS", item.getPartId(), item.getQuantityToBuy());
        });

        return purchaseListRepository.save(purchaseList);
    }
}
