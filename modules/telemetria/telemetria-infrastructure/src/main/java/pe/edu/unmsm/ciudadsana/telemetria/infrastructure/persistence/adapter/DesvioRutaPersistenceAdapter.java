package pe.edu.unmsm.ciudadsana.telemetria.infrastructure.persistence.adapter;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.DesvioRutaPersistencePort;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.DesvioId;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.RutaExternoId;
import pe.edu.unmsm.ciudadsana.telemetria.infrastructure.persistence.mapper.TelemetriaEntityMapper;
import pe.edu.unmsm.ciudadsana.telemetria.infrastructure.persistence.repository.DesvioRutaJpaRepository;

import java.util.Optional;

@Component
public class DesvioRutaPersistenceAdapter implements DesvioRutaPersistencePort {

    private final DesvioRutaJpaRepository repo;
    private final TelemetriaEntityMapper mapper;

    public DesvioRutaPersistenceAdapter(DesvioRutaJpaRepository repo, TelemetriaEntityMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public Optional<DesvioView> findById(DesvioId id, TenantId tenantId) {
        return repo.findByIdAndTenantId(id.value(), tenantId.value()).map(mapper::toView);
    }

    @Override
    public DesvioView save(DesvioView desvio) {
        return mapper.toView(repo.save(mapper.toEntity(desvio)));
    }

    @Override
    public PageResult<DesvioView> findActivosByRuta(RutaExternoId rutaExternoId, TenantId tenantId, int page, int size) {
        var p = repo.findAllByRutaExternoIdAndTenantIdAndEstado(
                rutaExternoId.value(), tenantId.value(), "ACTIVO", PageRequest.of(page, size));
        return PageResult.of(p.getContent().stream().map(mapper::toView).toList(), page, size, p.getTotalElements());
    }
}
