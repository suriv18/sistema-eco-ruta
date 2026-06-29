package pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GeoPointTest {

    @Test
    void crea_punto_valido() {
        GeoPoint punto = GeoPoint.of(-12.0464, -77.0428);

        assertThat(punto.latitud()).isEqualTo(-12.0464);
        assertThat(punto.longitud()).isEqualTo(-77.0428);
    }

    @Test
    void rechaza_latitud_invalida() {
        assertThatThrownBy(() -> GeoPoint.of(91.0, -77.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Latitud inválida");
    }

    @Test
    void rechaza_longitud_invalida() {
        assertThatThrownBy(() -> GeoPoint.of(-12.0, 181.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Longitud inválida");
    }

    @Test
    void calcula_distancia_haversine() {
        GeoPoint lima = GeoPoint.of(-12.0464, -77.0428);
        GeoPoint miraflores = GeoPoint.of(-12.1219, -77.0282);

        double distanciaKm = lima.distanciaHaversineKm(miraflores);

        assertThat(distanciaKm).isBetween(8.0, 10.0);
    }

    @Test
    void igualdad_basada_en_valor() {
        GeoPoint a = GeoPoint.of(-12.0, -77.0);
        GeoPoint b = GeoPoint.of(-12.0, -77.0);

        assertThat(a).isEqualTo(b);
    }
}
