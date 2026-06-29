package pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

public record GeoPoint(double latitud, double longitud) implements ValueObject {

    public GeoPoint {
        if (latitud < -90 || latitud > 90) {
            throw new IllegalArgumentException("Latitud inválida: " + latitud + ". Debe estar entre -90 y 90");
        }
        if (longitud < -180 || longitud > 180) {
            throw new IllegalArgumentException("Longitud inválida: " + longitud + ". Debe estar entre -180 y 180");
        }
    }

    public static GeoPoint of(double latitud, double longitud) {
        return new GeoPoint(latitud, longitud);
    }

    public double distanciaHaversineKm(GeoPoint otro) {
        final double R = 6371.0;
        double dLat = Math.toRadians(otro.latitud - this.latitud);
        double dLon = Math.toRadians(otro.longitud - this.longitud);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(this.latitud)) * Math.cos(Math.toRadians(otro.latitud))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    @Override
    public String toString() {
        return "POINT(" + longitud + " " + latitud + ")";
    }
}
