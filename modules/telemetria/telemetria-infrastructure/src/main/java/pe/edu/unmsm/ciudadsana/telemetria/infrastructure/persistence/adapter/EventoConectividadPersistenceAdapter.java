package pe.edu.unmsm.ciudadsana.telemetria.infrastructure.persistence.adapter;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.EventoConectividadPersistencePort;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.UnidadExternoId;
import pe.edu.unmsm.ciudadsana.telemetria.infrastructure.persistence.mapper.TelemetriaEntityMapper;
import pe.edu.unmsm.ciudadsana.telemetria.infrastructure.persistence.repository.EventoConectividadJpaRepository;

@Component
public class EventoConectividadPersistenceAdapter implements EventoConectividadPersistencePort {

    private final EventoConectividadJpaRepository repo;
    private final TelemetriaEntityMapper mapper;

    public EventoConectividadPersistenceAdapter(EventoConectividadJpaRepository repo, TelemetriaEntityMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public EventoConectividadView save(EventoConectividadView evento) {
        return mapper.toView(repo.save(mapper.toEntity(evento)));
    }

    @Override
    public PageResult<EventoConectividadView> findAllByUnidad(UnidadExternoId unidadExternoId, TenantId tenantId, int page, int size) {
        var p = repo.findAllByUnidadExternoIdAndTenantIdOrderByDetectadoEnDesc(
                unidadExternoId.value(), tenantId.value(), PageRequest.of(page, size));
        return PageResult.of(p.getContent().stream().map(mapper::toView).toList(), page, size, p.getTotalElements());
    }
}
