package pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.adapter;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.DistritosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Distrito;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DistritoId;
import pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.mapper.OperacionEntityMapper;
import pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.repository.DistritoJpaRepository;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;

import java.util.Optional;

@Component
public class DistritosPersistenceAdapter implements DistritosPersistencePort {

    private final DistritoJpaRepository repo;
    private final OperacionEntityMapper mapper;

    public DistritosPersistenceAdapter(DistritoJpaRepository repo, OperacionEntityMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public Optional<Distrito> findById(DistritoId id, TenantId tenantId) {
        return repo.findByIdAndTenantId(id.value(), tenantId.value()).map(mapper::toDomain);
    }

    @Override
    public boolean existsByNombre(String nombre, TenantId tenantId) {
        return repo.existsByNombreAndTenantId(nombre, tenantId.value());
    }

    @Override
    public Distrito save(Distrito distrito) {
        return mapper.toDomain(repo.save(mapper.toEntity(distrito)));
    }

    @Override
    public PageResult<Distrito> findAll(TenantId tenantId, int page, int size) {
        var p = repo.findAllByTenantId(tenantId.value(), PageRequest.of(page, size));
        return PageResult.of(p.getContent().stream().map(mapper::toDomain).toList(), page, size, p.getTotalElements());
    }
}
