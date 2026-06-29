package pe.edu.unmsm.ciudadsana.telemetria.infrastructure.persistence.adapter;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.DispositivosPersistencePort;
import pe.edu.unmsm.ciudadsana.telemetria.domain.model.DispositivoGps;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.DispositivoId;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.UnidadExternoId;
import pe.edu.unmsm.ciudadsana.telemetria.infrastructure.persistence.mapper.TelemetriaEntityMapper;
import pe.edu.unmsm.ciudadsana.telemetria.infrastructure.persistence.repository.DispositivoGpsJpaRepository;

import java.util.Optional;

@Component
public class DispositivoGpsPersistenceAdapter implements DispositivosPersistencePort {

    private final DispositivoGpsJpaRepository repo;
    private final TelemetriaEntityMapper mapper;

    public DispositivoGpsPersistenceAdapter(DispositivoGpsJpaRepository repo, TelemetriaEntityMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public boolean existsByUnidadExternoIdAndTenantId(UnidadExternoId id, TenantId tenantId) {
        return repo.existsByUnidadExternoIdAndTenantId(id.value(), tenantId.value());
    }

    @Override
    public Optional<DispositivoGps> findByIdAndTenantId(DispositivoId id, TenantId tenantId) {
        return repo.findByIdAndTenantId(id.value(), tenantId.value()).map(mapper::toDomain);
    }

    @Override
    public Optional<DispositivoGps> findByUnidadExternoIdAndTenantId(UnidadExternoId id, TenantId tenantId) {
        return repo.findByUnidadExternoIdAndTenantId(id.value(), tenantId.value()).map(mapper::toDomain);
    }

    @Override
    public PageResult<DispositivoGps> findAllByTenantId(TenantId tenantId, int page, int size) {
        var p = repo.findAllByTenantId(tenantId.value(), PageRequest.of(page, size));
        return PageResult.of(p.getContent().stream().map(mapper::toDomain).toList(), page, size, p.getTotalElements());
    }

    @Override
    public DispositivoGps save(DispositivoGps dispositivo) {
        return mapper.toDomain(repo.save(mapper.toEntity(dispositivo)));
    }
}
