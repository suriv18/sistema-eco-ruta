package pe.edu.unmsm.ciudadsana.telemetria.application.commandhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.telemetria.application.command.RegistrarDispositivoCommand;
import pe.edu.unmsm.ciudadsana.telemetria.application.dto.DispositivoResponseDto;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.in.RegistrarDispositivoUseCase;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.DispositivosPersistencePort;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.TelemetriaEventPublisherPort;
import pe.edu.unmsm.ciudadsana.telemetria.domain.model.DispositivoGps;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.DispositivoId;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.UnidadExternoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component
public class RegistrarDispositivoCommandHandler implements RegistrarDispositivoUseCase {

    private final DispositivosPersistencePort dispositivosPersistencePort;
    private final TelemetriaEventPublisherPort eventPublisher;

    public RegistrarDispositivoCommandHandler(DispositivosPersistencePort dispositivosPersistencePort,
                                              TelemetriaEventPublisherPort eventPublisher) {
        this.dispositivosPersistencePort = dispositivosPersistencePort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Result<DispositivoResponseDto> registrar(RegistrarDispositivoCommand cmd) {
        TenantId tenantId = TenantId.of(cmd.tenantId());
        UnidadExternoId unidadExternoId = UnidadExternoId.of(cmd.unidadExternoId());
        if (dispositivosPersistencePort.existsByUnidadExternoIdAndTenantId(unidadExternoId, tenantId)) {
            return Result.failure(ErrorCode.VALIDACION_ERROR, "Ya existe un dispositivo para esa unidad");
        }
        DispositivoGps dispositivo = DispositivoGps.create(
                DispositivoId.of(UUID.randomUUID()),
                tenantId,
                unidadExternoId,
                Optional.ofNullable(cmd.imei()),
                Optional.ofNullable(cmd.proveedor()),
                Instant.now()
        );
        dispositivo = dispositivosPersistencePort.save(dispositivo);
        eventPublisher.publishAll(dispositivo.pullDomainEvents());
        return Result.success(toDto(dispositivo));
    }

    private DispositivoResponseDto toDto(DispositivoGps d) {
        return new DispositivoResponseDto(
                d.getId().value(),
                d.getTenantId().value(),
                d.getUnidadExternoId().value(),
                d.getImei().orElse(null),
                d.getProveedor().orElse(null),
                d.getEstado().name(),
                d.getUltimoPingEn().orElse(null),
                d.getCreadoEn()
        );
    }
}
