package pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.adapter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.PermisoPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.domain.model.Permiso;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.PermisoId;
import pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.entity.PermisoJpaEntity;
import pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.mapper.UsuarioEntityMapper;
import pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.repository.PermisoJpaRepository;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;

import java.util.List;
import java.util.Optional;

@Component
public class PermisoPersistenceAdapter implements PermisoPersistencePort {

    private final PermisoJpaRepository permisoRepo;
    private final UsuarioEntityMapper mapper;

    public PermisoPersistenceAdapter(PermisoJpaRepository permisoRepo, UsuarioEntityMapper mapper) {
        this.permisoRepo = permisoRepo;
        this.mapper = mapper;
    }

    @Override
    public Optional<Permiso> findById(PermisoId id) {
        return permisoRepo.findById(id.value()).map(mapper::permisoDomain);
    }

    @Override
    public boolean existsByCodigo(String codigo) {
        return permisoRepo.existsByCodigo(codigo);
    }

    @Override
    public Permiso save(Permiso permiso) {
        PermisoJpaEntity entity = mapper.permisoToEntity(permiso);
        PermisoJpaEntity saved = permisoRepo.save(entity);
        return mapper.permisoDomain(saved);
    }

    @Override
    public PageResult<Permiso> findAll(int page, int size) {
        Page<PermisoJpaEntity> jpaPage = permisoRepo.findAll(PageRequest.of(page, size));
        List<Permiso> content = jpaPage.getContent().stream()
                .map(mapper::permisoDomain)
                .toList();
        return PageResult.of(content, page, size, jpaPage.getTotalElements());
    }
}
