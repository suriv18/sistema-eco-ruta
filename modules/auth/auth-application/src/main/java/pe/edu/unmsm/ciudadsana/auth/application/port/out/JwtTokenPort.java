package pe.edu.unmsm.ciudadsana.auth.application.port.out;

import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.UsuarioId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;

import java.util.Set;

public interface JwtTokenPort {
    String generateAccessToken(UsuarioId usuarioId, TenantId tenantId, String username, Set<String> roles);
    String generateRefreshToken(UsuarioId usuarioId);
    boolean validateRefreshToken(String token);
    UsuarioId extractUsuarioId(String token);
}
