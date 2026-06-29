package pe.edu.unmsm.ciudadsana.auditoria.application.commandhandler;

import org.springframework.stereotype.Service;
import pe.edu.unmsm.ciudadsana.auditoria.application.command.RegistrarOutboxEventCommand;
import pe.edu.unmsm.ciudadsana.auditoria.application.dto.OutboxEventDto;
import pe.edu.unmsm.ciudadsana.auditoria.application.port.in.RegistrarOutboxEventUseCase;
import pe.edu.unmsm.ciudadsana.auditoria.application.port.out.AuditoriaPersistencePort;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.UUID;

@Service
public class RegistrarOutboxEventCommandHandler implements RegistrarOutboxEventUseCase {

    private final AuditoriaPersistencePort persistencePort;

    public RegistrarOutboxEventCommandHandler(AuditoriaPersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    @Override
    public Result<Void> registrar(RegistrarOutboxEventCommand command) {
        OutboxEventDto dto = new OutboxEventDto(
                UUID.randomUUID(),
                command.tenantId(),
                command.aggregateType(),
                command.aggregateId(),
                command.eventType(),
                command.payload(),
                "PENDIENTE",
                Instant.now(),
                null,
                null
        );
        persistencePort.saveOutboxEvent(dto);
        return Result.success(null);
    }
}
