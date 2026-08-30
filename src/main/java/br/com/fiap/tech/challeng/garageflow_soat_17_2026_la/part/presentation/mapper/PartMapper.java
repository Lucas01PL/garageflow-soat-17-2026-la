package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.presentation.mapper;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model.Part;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.presentation.dto.PartRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.presentation.dto.PartResponse;
import org.springframework.stereotype.Component;

@Component
public class PartMapper {

    public Part requestToPart(PartRequest partRequest) {
        return new Part(
                partRequest.code(),
                partRequest.name(),
                partRequest.quantity(),
                partRequest.price()
        );
    }

    public PartResponse partToResponse(Part part) {
        return new PartResponse(
                part.getId(),
                part.getCode(),
                part.getName(),
                part.getQuantity(),
                part.getPrice()
        );
    }

}
