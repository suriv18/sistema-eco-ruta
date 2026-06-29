package pe.edu.unmsm.ciudadsana.operacion.application.port.out;

import pe.edu.unmsm.ciudadsana.operacion.domain.model.Turno;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ChoferId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.TurnoId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.UnidadId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public interface TurnosPersistencePort {
    Optional<Turno> findById(TurnoId id, TenantId tenantId);
    boolean existeSuperposicionUnidad(UnidadId unidadId, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, TenantId tenantId);
    boolean existeSuperposicionChofer(ChoferId choferId, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, TenantId tenantId);
    Turno save(Turno turno);
    PageResult<Turno> findAll(TenantId tenantId, int page, int size);
}
