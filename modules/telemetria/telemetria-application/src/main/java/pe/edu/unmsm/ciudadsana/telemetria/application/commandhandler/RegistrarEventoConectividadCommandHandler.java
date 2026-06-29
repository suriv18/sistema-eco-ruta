package pe.edu.unmsm.ciudadsana.telemetria.application.commandhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.telemetria.application.command.RegistrarEventoConectividadCommand;
import pe.edu.unmsm.ciudadsana.telemetria.application.dto.EventoConectividadResponseDto;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.in.RegistrarEventoConectividadUseCase;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.EventoConectividadPersistencePort;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.EventoConectividadPersistencePort.EventoConectividadView;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.UUID;

@Component
public class RegistrarEventoConectividadCommandHandler implements RegistrarEventoConectividadUseCase {

    private final EventoConectividadPersistencePort eventoConectividadPersistencePort;

    public RegistrarEventoConectividadCommandHandler(EventoConectividadPersistencePort eventoConectividadPersistencePort) {
        this.eventoConectividadPersistencePort = eventoConectividadPersistencePort;
    }

    @Override
    public Result<EventoConectividadResponseDto> registrar(RegistrarEventoConectividadCommand cmd) {
        TenantId tenantId = TenantId.of(cmd.tenantId());
        EventoConectividadView view = new EventoConectividadView(
                UUID.randomUUID(),
                tenantId.value(),
                cmd.unidadExternoId(),
                cmd.dispositivoId(),
                cmd.tipoEvento(),
                cmd.detalle(),
                Instant.now()
        );
        EventoConectividadView saved = eventoConectividadPersistencePort.save(view);
        return Result.success(toDto(saved));
    }

    private EventoConectividadResponseDto toDto(EventoConectividadView e) {
        return new EventoConectividadResponseDto(
                e.id(),
                e.tenantId(),
                e.unidadExternoId(),
                e.dispositivoId(),
                e.tipoEvento(),
                e.detalle(),
                e.detectadoEn()
        );
    }
}
