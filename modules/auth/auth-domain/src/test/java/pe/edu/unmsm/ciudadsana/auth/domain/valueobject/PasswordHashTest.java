package pe.edu.unmsm.ciudadsana.auth.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PasswordHashTest {

    private static final String BCRYPT_HASH =
            "$2a$12$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";

    @Test
    void crea_hash_valido() {
        PasswordHash hash = PasswordHash.of(BCRYPT_HASH);

        assertThat(hash.value()).isEqualTo(BCRYPT_HASH);
    }

    @Test
    void rechaza_hash_nulo() {
        assertThatThrownBy(() -> PasswordHash.of(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nulo o vacío");
    }

    @Test
    void rechaza_hash_vacio() {
        assertThatThrownBy(() -> PasswordHash.of(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nulo o vacío");
    }

    @Test
    void rechaza_hash_solo_espacios() {
        assertThatThrownBy(() -> PasswordHash.of("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nulo o vacío");
    }

    @Test
    void to_string_oculta_el_valor() {
        PasswordHash hash = PasswordHash.of(BCRYPT_HASH);

        assertThat(hash.toString()).isEqualTo("[PROTECTED]");
        assertThat(hash.toString()).doesNotContain(BCRYPT_HASH);
    }

    @Test
    void igualdad_basada_en_valor() {
        PasswordHash a = PasswordHash.of(BCRYPT_HASH);
        PasswordHash b = PasswordHash.of(BCRYPT_HASH);

        assertThat(a).isEqualTo(b);
    }
}
