package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unmsm.ciudadsana.operacion.application.command.ActualizarZonaCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.ZonaResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.ActualizarZonaUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.ZonasPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Zona;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.PrioridadBase;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ZonaId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Component
public class ActualizarZonaCommandHandler implements ActualizarZonaUseCase {

    private final ZonasPersistencePort zonasPersistencePort;

    public ActualizarZonaCommandHandler(ZonasPersistencePort zonasPersistencePort) {
        this.zonasPersistencePort = zonasPersistencePort;
    }

    @Override
    @Transactional
    public Result<ZonaResponseDto> actualizar(ActualizarZonaCommand cmd) {
        return zonasPersistencePort.findById(ZonaId.of(cmd.zonaId()), TenantId.of(cmd.tenantId()))
                .map(zona -> {
                    zona.actualizarPrioridad(new PrioridadBase(cmd.prioridad()));
                    Zona guardada = zonasPersistencePort.save(zona);
                    return Result.success(new ZonaResponseDto(
                            guardada.getId().value(),
                            guardada.getTenantId().value(),
                            guardada.getDistritoId().value(),
                            guardada.getCodigo().value(),
                            guardada.getNombre(),
                            guardada.getTipo().name(),
                            guardada.getPrioridad().value(),
                            guardada.getEstado().name(),
                            guardada.getCreadoEn()
                    ));
                })
                .orElse(Result.failure(ErrorCode.ZONA_NO_ENCONTRADA));
    }
}
