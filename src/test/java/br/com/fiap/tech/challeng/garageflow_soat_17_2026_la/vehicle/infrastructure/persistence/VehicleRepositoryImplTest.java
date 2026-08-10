package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.infrastructure.persistence;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.model.Vehicle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleRepositoryImplTest {

    @Mock
    private VehicleMongoRepository vehicleMongoRepository;

    @InjectMocks
    private VehicleRepositoryImpl vehicleRepository;

    @Test
    void shouldSaveAndReturnDomain() {
        Vehicle vehicle = new Vehicle("id-1", "ABC1D23", "Volkswagen", "Gol", 2020);
        VehicleDocument savedDocument = new VehicleDocument("ABC1D23", "Volkswagen", "Gol", 2020);
        savedDocument.setId("id-1");

        when(vehicleMongoRepository.save(org.mockito.ArgumentMatchers.any())).thenReturn(savedDocument);

        Vehicle result = vehicleRepository.save(vehicle);

        assertEquals("id-1", result.getId());
        assertEquals("ABC1D23", result.getPlate());

        ArgumentCaptor<VehicleDocument> captor = ArgumentCaptor.forClass(VehicleDocument.class);
        verify(vehicleMongoRepository).save(captor.capture());
        assertEquals("id-1", captor.getValue().getId());
        assertEquals("ABC1D23", captor.getValue().getPlate());
    }

    @Test
    void shouldFindByIdAndMapToDomain() {
        VehicleDocument document = new VehicleDocument("ABC1D23", "Volkswagen", "Gol", 2020);
        document.setId("id-1");
        when(vehicleMongoRepository.findById("id-1")).thenReturn(Optional.of(document));

        Optional<Vehicle> result = vehicleRepository.findById("id-1");

        assertTrue(result.isPresent());
        assertEquals("id-1", result.get().getId());
    }

    @Test
    void shouldReturnEmptyWhenFindByIdNotFound() {
        when(vehicleMongoRepository.findById("missing")).thenReturn(Optional.empty());

        Optional<Vehicle> result = vehicleRepository.findById("missing");

        assertFalse(result.isPresent());
    }

    @Test
    void shouldFindByPlateAndMapToDomain() {
        VehicleDocument document = new VehicleDocument("ABC1D23", "Volkswagen", "Gol", 2020);
        document.setId("id-1");
        when(vehicleMongoRepository.findByPlate("ABC1D23")).thenReturn(Optional.of(document));

        Optional<Vehicle> result = vehicleRepository.findByPlate("ABC1D23");

        assertTrue(result.isPresent());
        assertEquals("ABC1D23", result.get().getPlate());
    }

    @Test
    void shouldReturnEmptyWhenFindByPlateNotFound() {
        when(vehicleMongoRepository.findByPlate("missing")).thenReturn(Optional.empty());

        Optional<Vehicle> result = vehicleRepository.findByPlate("missing");

        assertFalse(result.isPresent());
    }

    @Test
    void shouldFindAllAndMapToDomainList() {
        VehicleDocument document1 = new VehicleDocument("ABC1D23", "Volkswagen", "Gol", 2020);
        document1.setId("id-1");
        VehicleDocument document2 = new VehicleDocument("XYZ9E88", "Fiat", "Uno", 2015);
        document2.setId("id-2");
        when(vehicleMongoRepository.findAll()).thenReturn(List.of(document1, document2));

        List<Vehicle> result = vehicleRepository.findAll();

        assertEquals(2, result.size());
        assertEquals("id-1", result.get(0).getId());
        assertEquals("id-2", result.get(1).getId());
    }

    @Test
    void shouldDeleteById() {
        vehicleRepository.delete("id-1");

        verify(vehicleMongoRepository).deleteById("id-1");
    }

    @Test
    void shouldDelegateExistsByPlate() {
        when(vehicleMongoRepository.existsByPlate("ABC1D23")).thenReturn(true);

        assertTrue(vehicleRepository.existsByPlate("ABC1D23"));

        when(vehicleMongoRepository.existsByPlate("XYZ9E88")).thenReturn(false);

        assertFalse(vehicleRepository.existsByPlate("XYZ9E88"));
    }
}
