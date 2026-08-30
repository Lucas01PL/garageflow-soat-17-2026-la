package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VehicleTest {

    @Test
    void shouldCreateVehicleWithoutId() {
        Vehicle vehicle = new Vehicle("ABC1D23", "Volkswagen", "Gol", 2020);

        assertNull(vehicle.getId());
        assertEquals("ABC1D23", vehicle.getPlate());
        assertEquals("Volkswagen", vehicle.getBrand());
        assertEquals("Gol", vehicle.getModel());
        assertEquals(2020, vehicle.getYear());
    }

    @Test
    void shouldCreateVehicleWithId() {
        Vehicle vehicle = new Vehicle("id-1", "ABC1D23", "Volkswagen", "Gol", 2020);

        assertEquals("id-1", vehicle.getId());
        assertEquals("ABC1D23", vehicle.getPlate());
    }

    @Test
    void shouldUpdateBrandModelAndYear() {
        Vehicle vehicle = new Vehicle("id-1", "ABC1D23", "Volkswagen", "Gol", 2020);

        vehicle.update("Fiat", "Uno", 2015);

        assertEquals("Fiat", vehicle.getBrand());
        assertEquals("Uno", vehicle.getModel());
        assertEquals(2015, vehicle.getYear());
        assertEquals("id-1", vehicle.getId());
        assertEquals("ABC1D23", vehicle.getPlate());
    }

    @Test
    void toStringShouldContainFieldValues() {
        Vehicle vehicle = new Vehicle("id-1", "ABC1D23", "Volkswagen", "Gol", 2020);

        assertTrue(vehicle.toString().contains("ABC1D23"));
        assertTrue(vehicle.toString().contains("Volkswagen"));
    }
}
