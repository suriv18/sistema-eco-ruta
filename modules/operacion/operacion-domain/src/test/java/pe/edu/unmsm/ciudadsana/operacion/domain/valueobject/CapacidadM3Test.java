package pe.edu.unmsm.ciudadsana.operacion.domain.valueobject;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CapacidadM3Test {

    @Test
    void crea_capacidad_valida() {
        // Camión compactador estándar Lima: ~6 m³ de capacidad volumétrica
        CapacidadM3 cap = CapacidadM3.of(new BigDecimal("6.0"));

        assertThat(cap.value()).isEqualByComparingTo(new BigDecimal("6.0"));
    }

    @Test
    void acepta_valor_decimal_pequeno() {
        CapacidadM3 cap = CapacidadM3.of(new BigDecimal("0.5"));

        assertThat(cap.value()).isEqualByComparingTo(new BigDecimal("0.5"));
    }

    @Test
    void rechaza_nulo() {
        assertThatThrownBy(() -> CapacidadM3.of(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nula");
    }

    @Test
    void rechaza_cero() {
        assertThatThrownBy(() -> CapacidadM3.of(BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("mayor a 0");
    }

    @Test
    void rechaza_valor_negativo() {
        assertThatThrownBy(() -> CapacidadM3.of(new BigDecimal("-3.5")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("mayor a 0");
    }

    @Test
    void to_string_devuelve_plain_string() {
        CapacidadM3 cap = CapacidadM3.of(new BigDecimal("12.5"));

        assertThat(cap.toString()).isEqualTo("12.5");
    }

    @Test
    void igualdad_basada_en_valor() {
        CapacidadM3 a = CapacidadM3.of(new BigDecimal("6.00"));
        CapacidadM3 b = CapacidadM3.of(new BigDecimal("6.00"));

        assertThat(a).isEqualTo(b);
    }
}
