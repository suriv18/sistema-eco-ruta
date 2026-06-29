package pe.edu.unmsm.ciudadsana.telemetria.application.commandhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.telemetria.application.command.RegistrarDesvioRutaCommand;
import pe.edu.unmsm.ciudadsana.telemetria.application.dto.DesvioRutaResponseDto;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.in.RegistrarDesvioRutaUseCase;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.DesvioRutaPersistencePort;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.DesvioRutaPersistencePort.DesvioView;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.UUID;

@Component
public class RegistrarDesvioRutaCommandHandler implements RegistrarDesvioRutaUseCase {

    private final DesvioRutaPersistencePort desvioRutaPersistencePort;

    public RegistrarDesvioRutaCommandHandler(DesvioRutaPersistencePort desvioRutaPersistencePort) {
        this.desvioRutaPersistencePort = desvioRutaPersistencePort;
    }

    @Override
    public Result<DesvioRutaResponseDto> registrar(RegistrarDesvioRutaCommand cmd) {
        TenantId tenantId = TenantId.of(cmd.tenantId());
        DesvioView view = new DesvioView(
                UUID.randomUUID(),
                tenantId.value(),
                cmd.unidadExternoId(),
                cmd.rutaExternoId(),
                cmd.latitud(),
                cmd.longitud(),
                cmd.distanciaDesvioM(),
                cmd.severidad(),
                "ABIERTO",
                Instant.now(),
                null
        );
        DesvioView saved = desvioRutaPersistencePort.save(view);
        return Result.success(toDto(saved));
    }

    private DesvioRutaResponseDto toDto(DesvioView d) {
        return new DesvioRutaResponseDto(
                d.id(),
                d.tenantId(),
                d.unidadExternoId(),
                d.rutaExternoId(),
                d.latitud(),
                d.longitud(),
                d.distanciaDesvioM(),
                d.severidad(),
                d.estado(),
                d.detectadoEn(),
                d.atendidoEn()
        );
    }
}
