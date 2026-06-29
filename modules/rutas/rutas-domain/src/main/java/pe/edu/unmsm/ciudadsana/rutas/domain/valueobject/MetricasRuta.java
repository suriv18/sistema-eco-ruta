package pe.edu.unmsm.ciudadsana.rutas.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

public record MetricasRuta(double distanciaM, int duracionS, double cargaKg) implements ValueObject {
    public MetricasRuta {
        if (distanciaM < 0) {
            throw new IllegalArgumentException("distanciaM must be >= 0");
        }
        if (duracionS < 0) {
            throw new IllegalArgumentException("duracionS must be >= 0");
        }
        if (cargaKg < 0) {
            throw new IllegalArgumentException("cargaKg must be >= 0");
        }
    }

    public static MetricasRuta of(double distanciaM, int duracionS, double cargaKg) {
        return new MetricasRuta(distanciaM, duracionS, cargaKg);
    }
}
