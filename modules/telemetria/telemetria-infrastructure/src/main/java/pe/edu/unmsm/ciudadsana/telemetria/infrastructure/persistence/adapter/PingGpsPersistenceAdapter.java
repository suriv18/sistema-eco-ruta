package pe.edu.unmsm.ciudadsana.telemetria.infrastructure.persistence.adapter;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.PingGpsPersistencePort;
import pe.edu.unmsm.ciudadsana.telemetria.domain.model.PingGps;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.UnidadExternoId;
import pe.edu.unmsm.ciudadsana.telemetria.infrastructure.persistence.mapper.TelemetriaEntityMapper;
import pe.edu.unmsm.ciudadsana.telemetria.infrastructure.persistence.repository.PingGpsJpaRepository;

@Component
public class PingGpsPersistenceAdapter implements PingGpsPersistencePort {

    private final PingGpsJpaRepository repo;
    private final TelemetriaEntityMapper mapper;

    public PingGpsPersistenceAdapter(PingGpsJpaRepository repo, TelemetriaEntityMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public PingGps save(PingGps ping) {
        return mapper.toDomain(repo.save(mapper.toEntity(ping)));
    }

    @Override
    public PageResult<PingGps> findAllByUnidad(UnidadExternoId unidadExternoId, TenantId tenantId, int page, int size) {
        var p = repo.findAllByUnidadExternoIdAndTenantIdOrderByTsDesc(
                unidadExternoId.value(), tenantId.value(), PageRequest.of(page, size));
        return PageResult.of(p.getContent().stream().map(mapper::toDomain).toList(), page, size, p.getTotalElements());
    }
}
