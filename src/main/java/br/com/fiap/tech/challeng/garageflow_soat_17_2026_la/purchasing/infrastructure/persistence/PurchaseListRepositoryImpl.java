package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.infrastructure.persistence;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model.PurchaseList;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model.PurchaseListItem;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model.PurchaseListStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.repository.PurchaseListRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PurchaseListRepositoryImpl implements PurchaseListRepository {

    private final PurchaseListMongoRepository purchaseListMongoRepository;

    public PurchaseListRepositoryImpl(PurchaseListMongoRepository purchaseListMongoRepository) {
        this.purchaseListMongoRepository = purchaseListMongoRepository;
    }

    @Override
    public PurchaseList save(PurchaseList purchaseList) {
        PurchaseListDocument saved = purchaseListMongoRepository.save(toDocument(purchaseList));
        return toDomain(saved);
    }

    @Override
    public Optional<PurchaseList> findById(String id) {
        return purchaseListMongoRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<PurchaseList> findAll() {
        return purchaseListMongoRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public List<PurchaseList> findByStatus(PurchaseListStatus status) {
        return purchaseListMongoRepository.findByStatus(status).stream().map(this::toDomain).toList();
    }

    private PurchaseList toDomain(PurchaseListDocument document) {
        List<PurchaseListItem> items = document.getItems().stream()
                .map(item -> new PurchaseListItem(
                        item.getPartId(),
                        item.getPartName(),
                        item.getCurrentQuantity(),
                        item.getQuantityToBuy(),
                        item.getUnitPrice()
                ))
                .toList();

        return PurchaseList.builder()
                .id(document.getId())
                .generatedAt(document.getGeneratedAt())
                .status(document.getStatus())
                .items(items)
                .approvedBy(document.getApprovedBy())
                .approvedAt(document.getApprovedAt())
                .build();
    }

    private PurchaseListDocument toDocument(PurchaseList purchaseList) {
        PurchaseListDocument document = new PurchaseListDocument();
        document.setId(purchaseList.getId());
        document.setGeneratedAt(purchaseList.getGeneratedAt());
        document.setStatus(purchaseList.getStatus());
        document.setApprovedBy(purchaseList.getApprovedBy());
        document.setApprovedAt(purchaseList.getApprovedAt());
        document.setItems(purchaseList.getItems().stream()
                .map(item -> new PurchaseListItemDocument(
                        item.getPartId(),
                        item.getPartName(),
                        item.getCurrentQuantity(),
                        item.getQuantityToBuy(),
                        item.getUnitPrice()
                ))
                .toList());
        return document;
    }
}
