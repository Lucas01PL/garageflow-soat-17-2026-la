package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.validator;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.exception.InvalidVehiclePlateException;

import java.util.regex.Pattern;

public final class PlateValidator {

    private static final Pattern OLD_FORMAT =
            Pattern.compile("^[A-Z]{3}[0-9]{4}$");

    private static final Pattern MERCOSUL_FORMAT =
            Pattern.compile("^[A-Z]{3}[0-9][A-Z][0-9]{2}$");

    private PlateValidator() {
    }

    public static boolean isValid(String plate) {
        if (plate == null || plate.isBlank()) {
            return false;
        }

        String normalizedPlate = normalize(plate);

        return OLD_FORMAT.matcher(normalizedPlate).matches()
                || MERCOSUL_FORMAT.matcher(normalizedPlate).matches();
    }

    public static String normalize(String plate) {
        if (plate == null) {
            return null;
        }

        return plate
                .replace("-", "")
                .replace(" ", "")
                .toUpperCase();
    }

    public static void validate(String plate) {
        if (!isValid(plate)) {
            throw new InvalidVehiclePlateException(plate);
        }
    }
}