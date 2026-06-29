package pe.edu.unmsm.ciudadsana.shared.security.model;

import java.util.Set;
import java.util.UUID;

public record AuthenticatedUser(
        UUID usuarioId,
        UUID tenantId,
        String username,
        Set<String> roles
) {

    public AuthenticatedUser {
        roles = Set.copyOf(roles);
    }

    public boolean hasRole(String role) {
        return roles.contains(role);
    }

    public boolean hasAnyRole(String... requiredRoles) {
        for (String role : requiredRoles) {
            if (roles.contains(role)) return true;
        }
        return false;
    }
}
