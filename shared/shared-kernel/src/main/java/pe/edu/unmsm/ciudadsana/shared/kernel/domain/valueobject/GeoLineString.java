package pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

import java.util.List;

public record GeoLineString(List<GeoPoint> puntos) implements ValueObject {

    public GeoLineString {
        if (puntos == null || puntos.size() < 2) {
            throw new IllegalArgumentException("GeoLineString requiere al menos 2 puntos");
        }
        puntos = List.copyOf(puntos);
    }

    public static GeoLineString of(List<GeoPoint> puntos) {
        return new GeoLineString(puntos);
    }

    public double longitudTotalKm() {
        double total = 0;
        for (int i = 0; i < puntos.size() - 1; i++) {
            total += puntos.get(i).distanciaHaversineKm(puntos.get(i + 1));
        }
        return total;
    }
}
