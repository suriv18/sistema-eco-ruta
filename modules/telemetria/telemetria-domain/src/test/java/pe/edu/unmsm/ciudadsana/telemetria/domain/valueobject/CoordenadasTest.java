package pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CoordenadasTest {

    @Test
    void crea_coordenadas_lima_centro() {
        // Plaza Mayor de Lima: -12.0464, -77.0428
        Coordenadas c = Coordenadas.of(-12.0464, -77.0428);

        assertThat(c.latitud()).isEqualTo(-12.0464);
        assertThat(c.longitud()).isEqualTo(-77.0428);
    }

    @Test
    void acepta_latitud_extremo_sur() {
        Coordenadas c = Coordenadas.of(-90.0, -77.0428);

        assertThat(c.latitud()).isEqualTo(-90.0);
    }

    @Test
    void acepta_latitud_extremo_norte() {
        Coordenadas c = Coordenadas.of(90.0, -77.0428);

        assertThat(c.latitud()).isEqualTo(90.0);
    }

    @Test
    void acepta_longitud_extremo_oeste() {
        Coordenadas c = Coordenadas.of(-12.0464, -180.0);

        assertThat(c.longitud()).isEqualTo(-180.0);
    }

    @Test
    void acepta_longitud_extremo_este() {
        Coordenadas c = Coordenadas.of(-12.0464, 180.0);

        assertThat(c.longitud()).isEqualTo(180.0);
    }

    @Test
    void rechaza_latitud_mayor_a_90() {
        assertThatThrownBy(() -> Coordenadas.of(90.001, -77.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Latitud inválida");
    }

    @Test
    void rechaza_latitud_menor_a_menos_90() {
        assertThatThrownBy(() -> Coordenadas.of(-90.001, -77.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Latitud inválida");
    }

    @Test
    void rechaza_longitud_mayor_a_180() {
        assertThatThrownBy(() -> Coordenadas.of(-12.0, 180.001))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Longitud inválida");
    }

    @Test
    void rechaza_longitud_menor_a_menos_180() {
        assertThatThrownBy(() -> Coordenadas.of(-12.0, -180.001))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Longitud inválida");
    }

    @Test
    void to_string_incluye_latitud_y_longitud() {
        Coordenadas c = Coordenadas.of(-12.1179, -77.0330);

        assertThat(c.toString()).contains("-12.1179").contains("-77.033");
    }

    @Test
    void igualdad_basada_en_valor() {
        // Estación Miraflores: mismas coordenadas → mismos objetos
        Coordenadas a = Coordenadas.of(-12.1179, -77.0330);
        Coordenadas b = Coordenadas.of(-12.1179, -77.0330);

        assertThat(a).isEqualTo(b);
    }
}
