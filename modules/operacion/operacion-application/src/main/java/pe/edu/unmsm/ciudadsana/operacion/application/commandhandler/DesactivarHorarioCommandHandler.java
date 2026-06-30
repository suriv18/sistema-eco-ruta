package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unmsm.ciudadsana.operacion.application.command.DesactivarHorarioCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.DesactivarHorarioUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.HorariosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.HorarioRecoleccionId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Component
public class DesactivarHorarioCommandHandler implements DesactivarHorarioUseCase {

    private final HorariosPersistencePort horariosPersistencePort;

    public DesactivarHorarioCommandHandler(HorariosPersistencePort horariosPersistencePort) {
        this.horariosPersistencePort = horariosPersistencePort;
    }

    @Override
    @Transactional
    public Result<Void> desactivar(DesactivarHorarioCommand cmd) {
        TenantId tenantId = TenantId.of(cmd.tenantId());
        HorarioRecoleccionId horarioId = HorarioRecoleccionId.of(cmd.horarioId());
        return horariosPersistencePort.findById(horarioId, tenantId)
                .map(horario -> {
                    horario.desactivar();
                    horariosPersistencePort.save(horario);
                    return Result.<Void>success(null);
                })
                .orElse(Result.failure(ErrorCode.HORARIO_NO_ENCONTRADO));
    }
}
