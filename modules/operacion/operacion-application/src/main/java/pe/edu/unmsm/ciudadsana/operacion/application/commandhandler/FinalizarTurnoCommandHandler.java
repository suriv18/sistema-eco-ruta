package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.operacion.application.command.FinalizarTurnoCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.FinalizarTurnoUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.OperacionEventPublisherPort;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.TurnosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Turno;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.TurnoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.Optional;

@Component
public class FinalizarTurnoCommandHandler implements FinalizarTurnoUseCase {

    private final TurnosPersistencePort turnosPersistencePort;
    private final OperacionEventPublisherPort eventPublisher;

    public FinalizarTurnoCommandHandler(TurnosPersistencePort turnosPersistencePort, OperacionEventPublisherPort eventPublisher) {
        this.turnosPersistencePort = turnosPersistencePort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Result<Void> finalizar(FinalizarTurnoCommand command) {
        TenantId tenantId = TenantId.of(command.tenantId());
        TurnoId turnoId = TurnoId.of(command.turnoId());
        Optional<Turno> turnoOpt = turnosPersistencePort.findById(turnoId, tenantId);
        if (turnoOpt.isEmpty()) return Result.failure(ErrorCode.TURNO_NO_ENCONTRADO);
        Turno turno = turnoOpt.get();
        try {
            turno.finalizar(Instant.now());
        } catch (IllegalStateException e) {
            return Result.failure(ErrorCode.TURNO_INVALIDO, e.getMessage());
        }
        turnosPersistencePort.save(turno);
        eventPublisher.publishAll(turno.pullDomainEvents());
        return Result.success(null);
    }
}
