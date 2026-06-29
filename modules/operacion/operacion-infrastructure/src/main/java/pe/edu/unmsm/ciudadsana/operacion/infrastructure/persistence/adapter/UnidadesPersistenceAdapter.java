package pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.adapter;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.UnidadesPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Unidad;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.Placa;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.UnidadId;
import pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.mapper.OperacionEntityMapper;
import pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.repository.UnidadJpaRepository;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;

import java.util.List;
import java.util.Optional;

@Component
public class UnidadesPersistenceAdapter implements UnidadesPersistencePort {

    private final UnidadJpaRepository repo;
    private final OperacionEntityMapper mapper;

    public UnidadesPersistenceAdapter(UnidadJpaRepository repo, OperacionEntityMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public Optional<Unidad> findById(UnidadId id, TenantId tenantId) {
        return repo.findByIdAndTenantId(id.value(), tenantId.value()).map(mapper::toDomain);
    }

    @Override
    public boolean existsByPlaca(Placa placa, TenantId tenantId) {
        return repo.existsByPlacaAndTenantId(placa.value(), tenantId.value());
    }

    @Override
    public Unidad save(Unidad unidad) {
        return mapper.toDomain(repo.save(mapper.toEntity(unidad)));
    }

    @Override
    public PageResult<Unidad> findAll(TenantId tenantId, int page, int size) {
        var p = repo.findAllByTenantId(tenantId.value(), PageRequest.of(page, size));
        return PageResult.of(p.getContent().stream().map(mapper::toDomain).toList(), page, size, p.getTotalElements());
    }

    @Override
    public List<Unidad> findDisponibles(TenantId tenantId) {
        return repo.findAllByTenantIdAndEstadoOperativo(tenantId.value(), "OPERATIVA")
                .stream().map(mapper::toDomain).toList();
    }
}
