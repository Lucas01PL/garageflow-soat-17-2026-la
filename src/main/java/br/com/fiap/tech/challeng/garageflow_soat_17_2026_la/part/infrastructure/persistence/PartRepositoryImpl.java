package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.infrastructure.persistence;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model.Part;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.repository.PartRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PartRepositoryImpl implements PartRepository {

    private final PartMongoRepository partMongoRepository;

    public PartRepositoryImpl(PartMongoRepository partMongoRepository) {
        this.partMongoRepository = partMongoRepository;
    }

    @Override
    public Part save(Part part) {
        PartDocument savedEntity = partMongoRepository.save(toEntity(part));
        return toPartDomain(savedEntity);
    }

    @Override
    public Optional<Part> findById(String id) {
        return partMongoRepository.findById(id).map(this::toPartDomain);
    }

    @Override
    public Optional<Part> findByCode(String code) {
        return partMongoRepository.findByCode(code).map(this::toPartDomain);
    }

    @Override
    public List<Part> findAll() {
        return partMongoRepository.findAll().
                stream()
                .map(this::toPartDomain)
                .toList();
    }

    @Override
    public void delete(String id) {
        partMongoRepository.deleteById(id);
    }

    @Override
    public boolean existsByCode(String code) {
        return partMongoRepository.existsByCode(code);
    }

    @Override
    public List<Part> findLowStock(Integer threshold) {
        return partMongoRepository.findByQuantityLessThanEqual(threshold)
                .stream()
                .map(this::toPartDomain)
                .toList();
    }

    private Part toPartDomain(PartDocument partDocument){
        return new Part(
                partDocument.getId(),
                partDocument.getCode(),
                partDocument.getName(),
                partDocument.getQuantity(),
                partDocument.getPrice()
        );
    }

    private PartDocument toEntity (Part part){
        PartDocument document = new PartDocument(
                part.getCode(),
                part.getName(),
                part.getQuantity(),
                part.getPrice()
        );
        document.setId(part.getId());
        return document;
    }

}
