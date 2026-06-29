package pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.adapter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.RolPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.domain.model.Rol;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.RolId;
import pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.entity.RolJpaEntity;
import pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.mapper.UsuarioEntityMapper;
import pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.repository.RolJpaRepository;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;

import java.util.List;
import java.util.Optional;

@Component
public class RolPersistenceAdapter implements RolPersistencePort {

    private final RolJpaRepository rolRepo;
    private final UsuarioEntityMapper mapper;

    public RolPersistenceAdapter(RolJpaRepository rolRepo, UsuarioEntityMapper mapper) {
        this.rolRepo = rolRepo;
        this.mapper = mapper;
    }

    @Override
    public Optional<Rol> findById(RolId id) {
        return rolRepo.findById(id.value()).map(mapper::rolToDomain);
    }

    @Override
    public Optional<Rol> findByCodigo(String codigo) {
        return rolRepo.findByCodigo(codigo).map(mapper::rolToDomain);
    }

    @Override
    public List<Rol> findByIds(List<RolId> ids) {
        List<java.util.UUID> uuids = ids.stream().map(RolId::value).toList();
        return rolRepo.findAllByIdIn(uuids).stream().map(mapper::rolToDomain).toList();
    }

    @Override
    public Rol save(Rol rol) {
        RolJpaEntity entity = mapper.rolToEntity(rol);
        RolJpaEntity saved = rolRepo.save(entity);
        return mapper.rolToDomain(saved);
    }

    @Override
    public PageResult<Rol> findAll(int page, int size) {
        Page<RolJpaEntity> jpaPage = rolRepo.findAll(PageRequest.of(page, size));
        List<Rol> content = jpaPage.getContent().stream()
                .map(mapper::rolToDomain)
                .toList();
        return PageResult.of(content, page, size, jpaPage.getTotalElements());
    }

    @Override
    public boolean existsByCodigo(String codigo) {
        return rolRepo.existsByCodigo(codigo);
    }
}
