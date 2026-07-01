package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unmsm.ciudadsana.operacion.application.command.CancelarTurnoCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.CancelarTurnoUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.TurnosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.TurnoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Component
public class CancelarTurnoCommandHandler implements CancelarTurnoUseCase {

    private final TurnosPersistencePort turnosPersistencePort;

    public CancelarTurnoCommandHandler(TurnosPersistencePort turnosPersistencePort) {
        this.turnosPersistencePort = turnosPersistencePort;
    }

    @Override
    @Transactional
    public Result<Void> cancelar(CancelarTurnoCommand cmd) {
        return turnosPersistencePort.findById(TurnoId.of(cmd.id()), TenantId.of(cmd.tenantId()))
                .map(turno -> {
                    try {
                        turno.cancelar();
                    } catch (IllegalStateException e) {
                        return Result.<Void>failure(ErrorCode.TURNO_INVALIDO, e.getMessage());
                    }
                    turnosPersistencePort.save(turno);
                    return Result.<Void>success(null);
                })
                .orElse(Result.failure(ErrorCode.TURNO_NO_ENCONTRADO));
    }
}
