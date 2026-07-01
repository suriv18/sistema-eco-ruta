package pe.edu.unmsm.ciudadsana.operacion.domain.valueobject;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CapacidadKgTest {

    @Test
    void crea_capacidad_valida_cam4892() {
        // Camión compactador CAM-4892 tiene capacidad de 8000 kg
        CapacidadKg cap = CapacidadKg.of(new BigDecimal("8000"));

        assertThat(cap.value()).isEqualByComparingTo(new BigDecimal("8000"));
    }

    @Test
    void acepta_valor_minimo_positivo() {
        CapacidadKg cap = CapacidadKg.of(new BigDecimal("0.01"));

        assertThat(cap.value()).isEqualByComparingTo(new BigDecimal("0.01"));
    }

    @Test
    void rechaza_nulo() {
        assertThatThrownBy(() -> CapacidadKg.of(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nula");
    }

    @Test
    void rechaza_cero() {
        assertThatThrownBy(() -> CapacidadKg.of(BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("mayor a 0");
    }

    @Test
    void rechaza_valor_negativo() {
        assertThatThrownBy(() -> CapacidadKg.of(new BigDecimal("-100")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("mayor a 0");
    }

    @Test
    void to_string_devuelve_plain_string() {
        CapacidadKg cap = CapacidadKg.of(new BigDecimal("7500"));

        assertThat(cap.toString()).isEqualTo("7500");
    }

    @Test
    void igualdad_basada_en_valor() {
        CapacidadKg a = CapacidadKg.of(new BigDecimal("8000.00"));
        CapacidadKg b = CapacidadKg.of(new BigDecimal("8000.00"));

        assertThat(a).isEqualTo(b);
    }
}
