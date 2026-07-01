package pe.edu.unmsm.ciudadsana.operacion.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PlacaTest {

    @Test
    void acepta_placa_formato_antiguo() {
        Placa placa = Placa.of("CAM-489");

        assertThat(placa.value()).isEqualTo("CAM-489");
    }

    @Test
    void acepta_placa_formato_nuevo() {
        // Formato nuevo peruano: 1 letra + 4 dígitos + 1 letra (e.g. A1234B)
        Placa placa = Placa.of("A1234B");

        assertThat(placa.value()).isEqualTo("A1234B");
    }

    @Test
    void normaliza_a_mayusculas() {
        Placa placa = Placa.of("cam-489");

        assertThat(placa.value()).isEqualTo("CAM-489");
    }

    @Test
    void elimina_espacios_al_inicio_y_fin() {
        Placa placa = Placa.of("  RIJ-023  ");

        assertThat(placa.value()).isEqualTo("RIJ-023");
    }

    @Test
    void rechaza_placa_nula() {
        assertThatThrownBy(() -> Placa.of(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nula o vacía");
    }

    @Test
    void rechaza_placa_vacia() {
        assertThatThrownBy(() -> Placa.of(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nula o vacía");
    }

    @Test
    void rechaza_formato_invalido_solo_numeros() {
        assertThatThrownBy(() -> Placa.of("1234567"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("formato peruano inválido");
    }

    @Test
    void rechaza_formato_invalido_sin_guion() {
        assertThatThrownBy(() -> Placa.of("CAM489"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("formato peruano inválido");
    }

    @Test
    void to_string_devuelve_valor() {
        assertThat(Placa.of("CAM-489").toString()).isEqualTo("CAM-489");
    }

    @Test
    void igualdad_basada_en_valor() {
        Placa a = Placa.of("cam-489");
        Placa b = Placa.of("CAM-489");

        assertThat(a).isEqualTo(b);
    }
}
