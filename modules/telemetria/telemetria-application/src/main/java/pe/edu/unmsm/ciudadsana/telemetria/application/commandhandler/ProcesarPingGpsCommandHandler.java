package pe.edu.unmsm.ciudadsana.telemetria.application.commandhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.telemetria.application.command.ProcesarPingGpsCommand;
import pe.edu.unmsm.ciudadsana.telemetria.application.dto.PingGpsResponseDto;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.in.ProcesarPingGpsUseCase;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.DispositivosPersistencePort;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.EstadoUnidadPersistencePort;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.EstadoUnidadPersistencePort.EstadoUnidadView;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.PingGpsPersistencePort;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.TelemetriaEventPublisherPort;
import pe.edu.unmsm.ciudadsana.telemetria.domain.enums.EstadoMovimiento;
import pe.edu.unmsm.ciudadsana.telemetria.domain.enums.OrigenPing;
import pe.edu.unmsm.ciudadsana.telemetria.domain.model.DispositivoGps;
import pe.edu.unmsm.ciudadsana.telemetria.domain.model.PingGps;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.Coordenadas;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.DispositivoId;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.PingId;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.RutaExternoId;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.UnidadExternoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component
public class ProcesarPingGpsCommandHandler implements ProcesarPingGpsUseCase {

    private final DispositivosPersistencePort dispositivosPersistencePort;
    private final PingGpsPersistencePort pingGpsPersistencePort;
    private final EstadoUnidadPersistencePort estadoUnidadPersistencePort;
    private final TelemetriaEventPublisherPort eventPublisher;

    public ProcesarPingGpsCommandHandler(DispositivosPersistencePort dispositivosPersistencePort,
                                         PingGpsPersistencePort pingGpsPersistencePort,
                                         EstadoUnidadPersistencePort estadoUnidadPersistencePort,
                                         TelemetriaEventPublisherPort eventPublisher) {
        this.dispositivosPersistencePort = dispositivosPersistencePort;
        this.pingGpsPersistencePort = pingGpsPersistencePort;
        this.estadoUnidadPersistencePort = estadoUnidadPersistencePort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Result<PingGpsResponseDto> procesar(ProcesarPingGpsCommand cmd) {
        TenantId tenantId = TenantId.of(cmd.tenantId());
        DispositivoId dispositivoId = DispositivoId.of(cmd.dispositivoId());
        UnidadExternoId unidadExternoId = UnidadExternoId.of(cmd.unidadExternoId());

        Optional<DispositivoGps> dispositivoOpt = dispositivosPersistencePort.findByIdAndTenantId(dispositivoId, tenantId);
        if (dispositivoOpt.isEmpty()) {
            return Result.failure(ErrorCode.RECURSO_NO_ENCONTRADO, "Dispositivo no encontrado");
        }
        DispositivoGps dispositivo = dispositivoOpt.get();

        Optional<RutaExternoId> rutaExternoId = Optional.ofNullable(cmd.rutaExternoId()).map(RutaExternoId::of);
        Coordenadas posicion = Coordenadas.of(cmd.latitud(), cmd.longitud());

        PingGps ping = PingGps.create(
                PingId.of(UUID.randomUUID()),
                tenantId,
                dispositivoId,
                unidadExternoId,
                rutaExternoId,
                cmd.ts(),
                posicion,
                Optional.ofNullable(cmd.velocidadKmh()),
                Optional.ofNullable(cmd.rumboGrados()),
                Optional.ofNullable(cmd.precisionM()),
                OrigenPing.valueOf(cmd.origen()),
                Instant.now()
        );
        ping = pingGpsPersistencePort.save(ping);

        dispositivo.registrarPing(Instant.now());
        dispositivosPersistencePort.save(dispositivo);

        Instant ahora = Instant.now();
        EstadoMovimiento estadoMovimiento = (cmd.velocidadKmh() != null && cmd.velocidadKmh() > 5)
                ? EstadoMovimiento.EN_RUTA
                : EstadoMovimiento.DETENIDA;

        // Upsert estado unidad using the View-based port
        Optional<EstadoUnidadView> estadoExistente = estadoUnidadPersistencePort.findByUnidad(unidadExternoId, tenantId);
        UUID estadoId = estadoExistente.map(EstadoUnidadView::id).orElse(UUID.randomUUID());
        EstadoUnidadView nuevoEstado = new EstadoUnidadView(
                estadoId,
                tenantId.value(),
                unidadExternoId.value(),
                rutaExternoId.map(RutaExternoId::value).orElse(null),
                posicion.latitud(),
                posicion.longitud(),
                cmd.velocidadKmh(),
                ahora,
                estadoMovimiento.name(),
                ahora
        );
        estadoUnidadPersistencePort.upsert(nuevoEstado);

        eventPublisher.publishAll(ping.pullDomainEvents());
        return Result.success(toDto(ping));
    }

    private PingGpsResponseDto toDto(PingGps p) {
        return new PingGpsResponseDto(
                p.getId().value(),
                p.getTenantId().value(),
                p.getDispositivoId().value(),
                p.getUnidadExternoId().value(),
                p.getRutaExternoId().map(RutaExternoId::value).orElse(null),
                p.getTs(),
                p.getPosicion().latitud(),
                p.getPosicion().longitud(),
                p.getVelocidadKmh().orElse(null),
                p.getRumboGrados().orElse(null),
                p.getPrecisionM().orElse(null),
                p.getOrigen().name(),
                p.getRecibidoEn()
        );
    }
}
