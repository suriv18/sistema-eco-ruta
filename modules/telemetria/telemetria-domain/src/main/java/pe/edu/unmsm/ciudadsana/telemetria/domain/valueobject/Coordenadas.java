package pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

public record Coordenadas(double latitud, double longitud) implements ValueObject {

    public Coordenadas {
        if (latitud < -90.0 || latitud > 90.0) {
            throw new IllegalArgumentException(
                    "Latitud inválida: " + latitud + ". Debe estar en el rango [-90, 90]");
        }
        if (longitud < -180.0 || longitud > 180.0) {
            throw new IllegalArgumentException(
                    "Longitud inválida: " + longitud + ". Debe estar en el rango [-180, 180]");
        }
    }

    public static Coordenadas of(double latitud, double longitud) {
        return new Coordenadas(latitud, longitud);
    }

    @Override
    public String toString() {
        return "Coordenadas{lat=" + latitud + ", lon=" + longitud + "}";
    }
}
