package pe.edu.unmsm.ciudadsana.auth.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailTest {

    @Test
    void crea_email_valido() {
        Email email = Email.of("operaciones@emilima.gob.pe");

        assertThat(email.value()).isEqualTo("operaciones@emilima.gob.pe");
    }

    @Test
    void normaliza_a_minusculas() {
        Email email = Email.of("CHOFER.CAM4892@MML.GOB.PE");

        assertThat(email.value()).isEqualTo("chofer.cam4892@mml.gob.pe");
    }

    @Test
    void elimina_espacios_en_blanco() {
        Email email = Email.of("  supervisora@emilima.gob.pe  ");

        assertThat(email.value()).isEqualTo("supervisora@emilima.gob.pe");
    }

    @Test
    void rechaza_email_nulo() {
        assertThatThrownBy(() -> Email.of(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nulo o vacío");
    }

    @Test
    void rechaza_email_vacio() {
        assertThatThrownBy(() -> Email.of(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nulo o vacío");
    }

    @Test
    void rechaza_email_sin_arroba() {
        assertThatThrownBy(() -> Email.of("chofermiraflores.gob.pe"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("@");
    }

    @Test
    void rechaza_email_mayor_a_150_caracteres() {
        // 145 letras + "@mml.pe" = 152 chars > 150
        String emailLargo = "a".repeat(145) + "@mml.pe";
        assertThatThrownBy(() -> Email.of(emailLargo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("150");
    }

    @Test
    void to_string_devuelve_el_valor() {
        Email email = Email.of("gerencia@emilima.gob.pe");

        assertThat(email.toString()).isEqualTo("gerencia@emilima.gob.pe");
    }

    @Test
    void igualdad_basada_en_valor() {
        Email a = Email.of("chofer@mml.gob.pe");
        Email b = Email.of("CHOFER@MML.GOB.PE");

        assertThat(a).isEqualTo(b);
    }
}
