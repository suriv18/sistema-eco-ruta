package pe.edu.unmsm.ciudadsana.auth.infrastructure.external.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.PasswordEncoderPort;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.PasswordHash;

@Component
public class PasswordEncoderAdapter implements PasswordEncoderPort {

    private final PasswordEncoder passwordEncoder;

    public PasswordEncoderAdapter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public PasswordHash encode(String rawPassword) {
        return PasswordHash.of(passwordEncoder.encode(rawPassword));
    }

    @Override
    public boolean matches(String rawPassword, PasswordHash hash) {
        return passwordEncoder.matches(rawPassword, hash.value());
    }
}
