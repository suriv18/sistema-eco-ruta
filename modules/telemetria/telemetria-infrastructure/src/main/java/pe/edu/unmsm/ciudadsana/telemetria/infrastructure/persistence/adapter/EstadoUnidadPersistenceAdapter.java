package pe.edu.unmsm.ciudadsana.telemetria.infrastructure.persistence.adapter;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.EstadoUnidadPersistencePort;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.UnidadExternoId;
import pe.edu.unmsm.ciudadsana.telemetria.infrastructure.persistence.mapper.TelemetriaEntityMapper;
import pe.edu.unmsm.ciudadsana.telemetria.infrastructure.persistence.repository.EstadoUnidadJpaRepository;

import java.util.Optional;

@Component
public class EstadoUnidadPersistenceAdapter implements EstadoUnidadPersistencePort {

    private final EstadoUnidadJpaRepository repo;
    private final TelemetriaEntityMapper mapper;

    public EstadoUnidadPersistenceAdapter(EstadoUnidadJpaRepository repo, TelemetriaEntityMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public Optional<EstadoUnidadView> findByUnidad(UnidadExternoId unidadExternoId, TenantId tenantId) {
        return repo.findByUnidadExternoIdAndTenantId(unidadExternoId.value(), tenantId.value())
                .map(mapper::toView);
    }

    @Override
    public void upsert(EstadoUnidadView estadoUnidad) {
        repo.save(mapper.toEntity(estadoUnidad));
    }
}
