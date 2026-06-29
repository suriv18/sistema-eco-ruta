package pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.adapter;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.HorariosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.HorarioRecoleccion;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.HorarioRecoleccionId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ZonaId;
import pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.mapper.OperacionEntityMapper;
import pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.repository.HorarioRecoleccionJpaRepository;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;

import java.time.LocalTime;
import java.util.Optional;

@Component
public class HorariosPersistenceAdapter implements HorariosPersistencePort {

    private final HorarioRecoleccionJpaRepository repo;
    private final OperacionEntityMapper mapper;

    public HorariosPersistenceAdapter(HorarioRecoleccionJpaRepository repo, OperacionEntityMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public Optional<HorarioRecoleccion> findById(HorarioRecoleccionId id, TenantId tenantId) {
        return repo.findByIdAndTenantId(id.value(), tenantId.value()).map(mapper::toDomain);
    }

    @Override
    public boolean existsByZonaAndDiaAndHorario(ZonaId zonaId, int diaSemana, LocalTime horaInicio, LocalTime horaFin, TenantId tenantId) {
        return repo.existsByZonaIdAndDiaSemanaAndHoraInicioAndHoraFinAndTenantId(zonaId.value(), diaSemana, horaInicio, horaFin, tenantId.value());
    }

    @Override
    public HorarioRecoleccion save(HorarioRecoleccion horario) {
        return mapper.toDomain(repo.save(mapper.toEntity(horario)));
    }

    @Override
    public PageResult<HorarioRecoleccion> findAllByZona(ZonaId zonaId, TenantId tenantId, int page, int size) {
        var p = repo.findAllByZonaIdAndTenantId(zonaId.value(), tenantId.value(), PageRequest.of(page, size));
        return PageResult.of(p.getContent().stream().map(mapper::toDomain).toList(), page, size, p.getTotalElements());
    }
}
