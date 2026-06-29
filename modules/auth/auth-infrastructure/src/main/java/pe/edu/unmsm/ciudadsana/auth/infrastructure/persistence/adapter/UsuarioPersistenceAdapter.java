package pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.adapter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.UsuarioPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.domain.model.Usuario;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.Email;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.RolId;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.UsuarioId;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.Username;
import pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.entity.RolJpaEntity;
import pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.entity.UsuarioJpaEntity;
import pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.mapper.UsuarioEntityMapper;
import pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.repository.RolJpaRepository;
import pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.repository.UsuarioJpaRepository;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class UsuarioPersistenceAdapter implements UsuarioPersistencePort {

    private final UsuarioJpaRepository usuarioRepo;
    private final RolJpaRepository rolRepo;
    private final UsuarioEntityMapper mapper;

    public UsuarioPersistenceAdapter(
            UsuarioJpaRepository usuarioRepo,
            RolJpaRepository rolRepo,
            UsuarioEntityMapper mapper
    ) {
        this.usuarioRepo = usuarioRepo;
        this.rolRepo = rolRepo;
        this.mapper = mapper;
    }

    @Override
    public Optional<Usuario> findById(UsuarioId id) {
        return usuarioRepo.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    public Optional<Usuario> findByUsername(Username username, TenantId tenantId) {
        return usuarioRepo.findByUsernameAndTenantId(username.value(), tenantId.value())
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Usuario> findByEmail(Email email, TenantId tenantId) {
        return usuarioRepo.findByEmailAndTenantId(email.value(), tenantId.value())
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(Email email, TenantId tenantId) {
        return usuarioRepo.existsByEmailAndTenantId(email.value(), tenantId.value());
    }

    @Override
    public boolean existsByUsername(Username username, TenantId tenantId) {
        return usuarioRepo.existsByUsernameAndTenantId(username.value(), tenantId.value());
    }

    @Override
    public Usuario save(Usuario domain) {
        List<UUID> rolIds = domain.getRoles().stream()
                .map(RolId::value)
                .toList();
        Set<RolJpaEntity> rolEntities = rolIds.isEmpty()
                ? Set.of()
                : Set.copyOf(rolRepo.findAllByIdIn(rolIds));

        UsuarioJpaEntity entity = mapper.toEntity(domain, rolEntities);
        UsuarioJpaEntity saved = usuarioRepo.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public PageResult<Usuario> findAll(TenantId tenantId, int page, int size) {
        Page<UsuarioJpaEntity> jpaPage = usuarioRepo.findAllByTenantId(
                tenantId.value(), PageRequest.of(page, size));
        List<Usuario> content = jpaPage.getContent().stream()
                .map(mapper::toDomain)
                .toList();
        return PageResult.of(content, page, size, jpaPage.getTotalElements());
    }
}
