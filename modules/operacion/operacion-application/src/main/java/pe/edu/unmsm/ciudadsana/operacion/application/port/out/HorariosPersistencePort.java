package pe.edu.unmsm.ciudadsana.operacion.application.port.out;

import pe.edu.unmsm.ciudadsana.operacion.domain.model.HorarioRecoleccion;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.HorarioRecoleccionId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ZonaId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;

import java.time.LocalTime;
import java.util.Optional;

public interface HorariosPersistencePort {
    Optional<HorarioRecoleccion> findById(HorarioRecoleccionId id, TenantId tenantId);
    boolean existsByZonaAndDiaAndHorario(ZonaId zonaId, int diaSemana, LocalTime horaInicio, LocalTime horaFin, TenantId tenantId);
    HorarioRecoleccion save(HorarioRecoleccion horario);
    PageResult<HorarioRecoleccion> findAllByZona(ZonaId zonaId, TenantId tenantId, int page, int size);
}
