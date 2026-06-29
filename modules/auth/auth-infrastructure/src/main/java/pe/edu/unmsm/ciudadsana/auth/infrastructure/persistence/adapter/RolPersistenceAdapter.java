package pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.adapter;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.RolPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.domain.model.Rol;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.RolId;
import pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.mapper.UsuarioEntityMapper;
import pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.repository.RolJpaRepository;

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
}
