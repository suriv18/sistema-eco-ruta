package pe.edu.unmsm.ciudadsana.auth.infrastructure.external.jwt;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.JwtTokenPort;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.UsuarioId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.security.jwt.JwtTokenProvider;
import pe.edu.unmsm.ciudadsana.shared.security.model.AuthenticatedUser;

import java.util.Set;
import java.util.UUID;

@Component
public class JwtTokenAdapter implements JwtTokenPort {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenAdapter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public String generateAccessToken(UsuarioId usuarioId, TenantId tenantId, String username, Set<String> roles) {
        AuthenticatedUser user = new AuthenticatedUser(usuarioId.value(), tenantId.value(), username, roles);
        return jwtTokenProvider.generateAccessToken(user);
    }

    @Override
    public String generateRefreshToken(UsuarioId usuarioId) {
        return jwtTokenProvider.generateRefreshToken(usuarioId.value());
    }

    @Override
    public boolean validateRefreshToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }

    @Override
    public UsuarioId extractUsuarioId(String token) {
        UUID id = jwtTokenProvider.extractUsuarioId(token);
        return UsuarioId.of(id);
    }
}
