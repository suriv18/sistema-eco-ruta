package pe.edu.unmsm.ciudadsana.shared.security.context;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.shared.security.model.AuthenticatedUser;

import java.util.Optional;

@Component
public class CurrentUserProvider {

    public Optional<AuthenticatedUser> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        if (authentication.getPrincipal() instanceof AuthenticatedUser user) {
            return Optional.of(user);
        }
        return Optional.empty();
    }

    public AuthenticatedUser requireCurrentUser() {
        return getCurrentUser()
                .orElseThrow(() -> new IllegalStateException("No hay usuario autenticado en el contexto"));
    }
}
