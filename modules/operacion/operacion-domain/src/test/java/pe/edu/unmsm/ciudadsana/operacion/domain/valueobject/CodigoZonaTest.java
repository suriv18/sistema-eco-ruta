package pe.edu.unmsm.ciudadsana.operacion.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CodigoZonaTest {

    @Test
    void crea_codigo_zona_valido() {
        CodigoZona codigo = CodigoZona.of("ZM-01");

        assertThat(codigo.value()).isEqualTo("ZM-01");
    }

    @Test
    void normaliza_a_mayusculas() {
        CodigoZona codigo = CodigoZona.of("zm-miraflores");

        assertThat(codigo.value()).isEqualTo("ZM-MIRAFLORES");
    }

    @Test
    void acepta_codigo_con_guion_bajo() {
        CodigoZona codigo = CodigoZona.of("ZSQ_NORTE_01");

        assertThat(codigo.value()).isEqualTo("ZSQ_NORTE_01");
    }

    @Test
    void rechaza_codigo_nulo() {
        assertThatThrownBy(() -> CodigoZona.of(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nulo o vacío");
    }

    @Test
    void rechaza_codigo_vacio() {
        assertThatThrownBy(() -> CodigoZona.of(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nulo o vacío");
    }

    @Test
    void rechaza_codigo_de_un_caracter() {
        assertThatThrownBy(() -> CodigoZona.of("Z"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("2 y 50");
    }

    @Test
    void rechaza_codigo_mayor_a_50_caracteres() {
        String largo = "Z".repeat(51);
        assertThatThrownBy(() -> CodigoZona.of(largo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("2 y 50");
    }

    @Test
    void rechaza_espacios_en_medio() {
        assertThatThrownBy(() -> CodigoZona.of("ZM 01"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("inválido");
    }

    @Test
    void rechaza_caracteres_especiales() {
        assertThatThrownBy(() -> CodigoZona.of("ZM@01"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("inválido");
    }

    @Test
    void to_string_devuelve_valor() {
        assertThat(CodigoZona.of("ZB-BARRANCO").toString()).isEqualTo("ZB-BARRANCO");
    }

    @Test
    void igualdad_basada_en_valor() {
        CodigoZona a = CodigoZona.of("zm-01");
        CodigoZona b = CodigoZona.of("ZM-01");

        assertThat(a).isEqualTo(b);
    }
}
