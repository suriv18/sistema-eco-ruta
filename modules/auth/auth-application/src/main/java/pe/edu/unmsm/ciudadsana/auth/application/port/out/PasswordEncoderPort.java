package pe.edu.unmsm.ciudadsana.auth.application.port.out;

import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.PasswordHash;

public interface PasswordEncoderPort {
    PasswordHash encode(String rawPassword);
    boolean matches(String rawPassword, PasswordHash hash);
}
