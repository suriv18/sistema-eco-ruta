package pe.edu.unmsm.ciudadsana.auth.application.port.out;

import pe.edu.unmsm.ciudadsana.auth.domain.model.RefreshToken;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.UsuarioId;

import java.util.Optional;

public interface RefreshTokenPersistencePort {
    RefreshToken save(RefreshToken token);
    Optional<RefreshToken> findByTokenHash(String tokenHash);
    void revokeAllByUsuarioId(UsuarioId usuarioId);
}
