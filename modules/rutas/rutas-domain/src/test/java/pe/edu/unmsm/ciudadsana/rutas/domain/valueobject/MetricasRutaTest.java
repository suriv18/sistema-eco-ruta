package pe.edu.unmsm.ciudadsana.rutas.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MetricasRutaTest {

    @Test
    void crea_metricas_ruta_validas() {
        // Ruta CAM-4892: Miraflores → Surquillo → Barranco, 18 km, 65 min, 1270 kg
        MetricasRuta m = MetricasRuta.of(18_000.0, 3_900, 1_270.0);

        assertThat(m.distanciaM()).isEqualTo(18_000.0);
        assertThat(m.duracionS()).isEqualTo(3_900);
        assertThat(m.cargaKg()).isEqualTo(1_270.0);
    }

    @Test
    void acepta_valores_en_cero() {
        MetricasRuta m = MetricasRuta.of(0.0, 0, 0.0);

        assertThat(m.distanciaM()).isEqualTo(0.0);
        assertThat(m.duracionS()).isEqualTo(0);
        assertThat(m.cargaKg()).isEqualTo(0.0);
    }

    @Test
    void rechaza_distancia_negativa() {
        assertThatThrownBy(() -> MetricasRuta.of(-1.0, 3_900, 1_270.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("distanciaM");
    }

    @Test
    void rechaza_duracion_negativa() {
        assertThatThrownBy(() -> MetricasRuta.of(18_000.0, -1, 1_270.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("duracionS");
    }

    @Test
    void rechaza_carga_negativa() {
        assertThatThrownBy(() -> MetricasRuta.of(18_000.0, 3_900, -100.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("cargaKg");
    }

    @Test
    void igualdad_basada_en_valor() {
        MetricasRuta a = MetricasRuta.of(18_000.0, 3_900, 1_270.0);
        MetricasRuta b = MetricasRuta.of(18_000.0, 3_900, 1_270.0);

        assertThat(a).isEqualTo(b);
    }
}
