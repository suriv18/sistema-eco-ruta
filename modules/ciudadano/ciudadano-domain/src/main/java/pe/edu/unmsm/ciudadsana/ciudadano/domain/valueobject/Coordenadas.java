package pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

public record Coordenadas(double latitud, double longitud) implements ValueObject {

    public Coordenadas {
        if (latitud < -90.0 || latitud > 90.0) {
            throw new IllegalArgumentException(
                    "La latitud debe estar en el rango [-90, 90], valor recibido: " + latitud);
        }
        if (longitud < -180.0 || longitud > 180.0) {
            throw new IllegalArgumentException(
                    "La longitud debe estar en el rango [-180, 180], valor recibido: " + longitud);
        }
    }

    public static Coordenadas of(double latitud, double longitud) {
        return new Coordenadas(latitud, longitud);
    }

    @Override
    public String toString() {
        return "(" + latitud + ", " + longitud + ")";
    }
}
