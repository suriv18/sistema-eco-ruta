package pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.adapter;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.ContenedoresPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Contenedor;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ContenedorId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ZonaId;
import pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.mapper.OperacionEntityMapper;
import pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.repository.ContenedorJpaRepository;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;

import java.util.List;
import java.util.Optional;

@Component
public class ContenedoresPersistenceAdapter implements ContenedoresPersistencePort {

    private final ContenedorJpaRepository repo;
    private final OperacionEntityMapper mapper;

    public ContenedoresPersistenceAdapter(ContenedorJpaRepository repo, OperacionEntityMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public Optional<Contenedor> findById(ContenedorId id, TenantId tenantId) {
        return repo.findByIdAndTenantId(id.value(), tenantId.value()).map(mapper::toDomain);
    }

    @Override
    public boolean existsByCodigo(String codigo, TenantId tenantId) {
        return repo.existsByCodigoAndTenantId(codigo, tenantId.value());
    }

    @Override
    public Contenedor save(Contenedor contenedor) {
        return mapper.toDomain(repo.save(mapper.toEntity(contenedor)));
    }

    @Override
    public PageResult<Contenedor> findAll(TenantId tenantId, int page, int size) {
        var p = repo.findAllByTenantId(tenantId.value(), PageRequest.of(page, size));
        return PageResult.of(p.getContent().stream().map(mapper::toDomain).toList(), page, size, p.getTotalElements());
    }

    @Override
    public List<Contenedor> findByZona(ZonaId zonaId, TenantId tenantId) {
        return repo.findAllByZonaIdAndTenantId(zonaId.value(), tenantId.value())
                .stream().map(mapper::toDomain).toList();
    }
}
