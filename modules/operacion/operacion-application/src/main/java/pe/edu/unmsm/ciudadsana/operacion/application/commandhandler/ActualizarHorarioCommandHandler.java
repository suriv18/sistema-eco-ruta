package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unmsm.ciudadsana.operacion.application.command.ActualizarHorarioCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.HorarioResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.ActualizarHorarioUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.HorariosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.HorarioRecoleccion;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.HorarioRecoleccionId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Component
public class ActualizarHorarioCommandHandler implements ActualizarHorarioUseCase {

    private final HorariosPersistencePort horariosPersistencePort;

    public ActualizarHorarioCommandHandler(HorariosPersistencePort horariosPersistencePort) {
        this.horariosPersistencePort = horariosPersistencePort;
    }

    @Override
    @Transactional
    public Result<HorarioResponseDto> actualizar(ActualizarHorarioCommand cmd) {
        if (!cmd.horaFin().isAfter(cmd.horaInicio())) {
            return Result.failure(ErrorCode.HORARIO_RANGO_INVALIDO);
        }
        TenantId tenantId = TenantId.of(cmd.tenantId());
        HorarioRecoleccionId horarioId = HorarioRecoleccionId.of(cmd.horarioId());
        return horariosPersistencePort.findById(horarioId, tenantId)
                .map(horario -> {
                    horario.actualizar(cmd.horaInicio(), cmd.horaFin(), cmd.observacion());
                    HorarioRecoleccion guardado = horariosPersistencePort.save(horario);
                    return Result.success(HorarioResponseDto.from(guardado));
                })
                .orElse(Result.failure(ErrorCode.HORARIO_NO_ENCONTRADO));
    }
}
