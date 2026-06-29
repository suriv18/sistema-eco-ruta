package pe.edu.unmsm.ciudadsana.auditoria.application.commandhandler;

import org.springframework.stereotype.Service;
import pe.edu.unmsm.ciudadsana.auditoria.application.command.RegistrarEventoAuditoriaCommand;
import pe.edu.unmsm.ciudadsana.auditoria.application.dto.EventoAuditoriaDto;
import pe.edu.unmsm.ciudadsana.auditoria.application.port.in.RegistrarEventoAuditoriaUseCase;
import pe.edu.unmsm.ciudadsana.auditoria.application.port.out.AuditoriaPersistencePort;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.UUID;

@Service
public class RegistrarEventoAuditoriaCommandHandler implements RegistrarEventoAuditoriaUseCase {

    private final AuditoriaPersistencePort persistencePort;

    public RegistrarEventoAuditoriaCommandHandler(AuditoriaPersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    @Override
    public Result<Void> registrar(RegistrarEventoAuditoriaCommand command) {
        EventoAuditoriaDto dto = new EventoAuditoriaDto(
                UUID.randomUUID(),
                command.tenantId(),
                command.usuarioId(),
                command.modulo(),
                command.accion(),
                command.entidad(),
                command.entidadId(),
                command.datosAntes(),
                command.datosDespues(),
                Instant.now()
        );
        persistencePort.saveEventoAuditoria(dto);
        return Result.success(null);
    }
}
