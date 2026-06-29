package pe.edu.unmsm.ciudadsana.auth.application.port.out;

import pe.edu.unmsm.ciudadsana.auth.domain.model.Usuario;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.Email;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.UsuarioId;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.Username;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;

import java.util.Optional;

public interface UsuarioPersistencePort {
    Optional<Usuario> findById(UsuarioId id);
    Optional<Usuario> findByUsername(Username username, TenantId tenantId);
    Optional<Usuario> findByEmail(Email email, TenantId tenantId);
    boolean existsByEmail(Email email, TenantId tenantId);
    boolean existsByUsername(Username username, TenantId tenantId);
    Usuario save(Usuario usuario);
    PageResult<Usuario> findAll(TenantId tenantId, int page, int size);
}
