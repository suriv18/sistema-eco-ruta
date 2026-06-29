package pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.adapter;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.TurnosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Turno;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ChoferId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.TurnoId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.UnidadId;
import pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.mapper.OperacionEntityMapper;
import pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.repository.TurnoJpaRepository;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Component
public class TurnosPersistenceAdapter implements TurnosPersistencePort {

    private final TurnoJpaRepository repo;
    private final OperacionEntityMapper mapper;

    public TurnosPersistenceAdapter(TurnoJpaRepository repo, OperacionEntityMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public Optional<Turno> findById(TurnoId id, TenantId tenantId) {
        return repo.findByIdAndTenantId(id.value(), tenantId.value()).map(mapper::toDomain);
    }

    @Override
    public boolean existeSuperposicionUnidad(UnidadId unidadId, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, TenantId tenantId) {
        return repo.existsSuperposicionUnidad(tenantId.value(), unidadId.value(), fecha, horaInicio, horaFin);
    }

    @Override
    public boolean existeSuperposicionChofer(ChoferId choferId, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, TenantId tenantId) {
        return repo.existsSuperposicionChofer(tenantId.value(), choferId.value(), fecha, horaInicio, horaFin);
    }

    @Override
    public Turno save(Turno turno) {
        return mapper.toDomain(repo.save(mapper.toEntity(turno)));
    }

    @Override
    public PageResult<Turno> findAll(TenantId tenantId, int page, int size) {
        var p = repo.findAllByTenantId(tenantId.value(), PageRequest.of(page, size));
        return PageResult.of(p.getContent().stream().map(mapper::toDomain).toList(), page, size, p.getTotalElements());
    }
}
