package pe.edu.unmsm.ciudadsana.auth.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UsernameTest {

    @Test
    void crea_username_valido_minusculas() {
        Username u = Username.of("cam4892");

        assertThat(u.value()).isEqualTo("cam4892");
    }

    @Test
    void normaliza_mayusculas_a_minusculas() {
        Username u = Username.of("CAM-4892");

        assertThat(u.value()).isEqualTo("cam-4892");
    }

    @Test
    void acepta_guion_bajo_y_guion() {
        Username u = Username.of("chofer_miraflores-01");

        assertThat(u.value()).isEqualTo("chofer_miraflores-01");
    }

    @Test
    void rechaza_username_nulo() {
        assertThatThrownBy(() -> Username.of(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nulo o vacío");
    }

    @Test
    void rechaza_username_vacio() {
        assertThatThrownBy(() -> Username.of(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nulo o vacío");
    }

    @Test
    void rechaza_username_menor_a_3_caracteres() {
        assertThatThrownBy(() -> Username.of("ab"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("3 y 80");
    }

    @Test
    void rechaza_username_mayor_a_80_caracteres() {
        String largo = "a".repeat(81);
        assertThatThrownBy(() -> Username.of(largo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("3 y 80");
    }

    @Test
    void rechaza_espacios_en_medio() {
        assertThatThrownBy(() -> Username.of("chofer miraflores"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("guiones");
    }

    @Test
    void rechaza_arroba() {
        assertThatThrownBy(() -> Username.of("chofer@mml"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void to_string_devuelve_valor() {
        Username u = Username.of("supervisora_lima");

        assertThat(u.toString()).isEqualTo("supervisora_lima");
    }

    @Test
    void igualdad_basada_en_valor() {
        Username a = Username.of("RIJ0234");
        Username b = Username.of("rij0234");

        assertThat(a).isEqualTo(b);
    }
}
