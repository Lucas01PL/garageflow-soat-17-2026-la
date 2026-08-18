package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase.PartListToBuyUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model.Part;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model.PurchaseList;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model.PurchaseListItem;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model.PurchaseListStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.repository.PurchaseListRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class GeneratePurchaseListUseCase {

    private final PartListToBuyUseCase partListToBuyUseCase;
    private final PurchaseListRepository purchaseListRepository;

    public GeneratePurchaseListUseCase(PartListToBuyUseCase partListToBuyUseCase, PurchaseListRepository purchaseListRepository) {
        this.partListToBuyUseCase = partListToBuyUseCase;
        this.purchaseListRepository = purchaseListRepository;
    }

    public PurchaseList generate(Integer threshold) {
        Integer appliedThreshold = threshold != null ? threshold : PartListToBuyUseCase.DEFAULT_LOW_STOCK_THRESHOLD;
        List<Part> lowStockParts = partListToBuyUseCase.findPartsToBuy(appliedThreshold);
        int restockTarget = appliedThreshold * 2;

        List<PurchaseListItem> items = lowStockParts.stream()
                .map(part -> new PurchaseListItem(
                        part.getId(),
                        part.getName(),
                        part.getQuantity(),
                        Math.max(restockTarget - part.getQuantity(), 1),
                        part.getPrice()
                ))
                .toList();

        PurchaseList purchaseList = PurchaseList.builder()
                .generatedAt(LocalDateTime.now())
                .status(PurchaseListStatus.PENDING)
                .items(items)
                .build();

        log.debug("[DEBUG] - GENERATING PURCHASE LIST WITH {} ITEMS", items.size());
        return purchaseListRepository.save(purchaseList);
    }
}
