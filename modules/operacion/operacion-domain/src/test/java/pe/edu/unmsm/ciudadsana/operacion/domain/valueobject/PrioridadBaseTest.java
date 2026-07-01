package pe.edu.unmsm.ciudadsana.operacion.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PrioridadBaseTest {

    @Test
    void crea_prioridad_uno_minima() {
        PrioridadBase p = PrioridadBase.of(1);

        assertThat(p.value()).isEqualTo(1);
    }

    @Test
    void crea_prioridad_maxima_alta() {
        // Zona de acumulación crítica en Puente Piedra recibe prioridad 5
        PrioridadBase p = PrioridadBase.of(5);

        assertThat(p.value()).isEqualTo(5);
    }

    @Test
    void crea_prioridad_intermedia() {
        PrioridadBase p = PrioridadBase.of(3);

        assertThat(p.value()).isEqualTo(3);
    }

    @Test
    void rechaza_cero() {
        assertThatThrownBy(() -> PrioridadBase.of(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(">= 1");
    }

    @Test
    void rechaza_negativo() {
        assertThatThrownBy(() -> PrioridadBase.of(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(">= 1");
    }

    @Test
    void to_string_devuelve_valor() {
        assertThat(PrioridadBase.of(3).toString()).isEqualTo("3");
    }

    @Test
    void igualdad_basada_en_valor() {
        PrioridadBase a = PrioridadBase.of(3);
        PrioridadBase b = PrioridadBase.of(3);

        assertThat(a).isEqualTo(b);
    }
}
