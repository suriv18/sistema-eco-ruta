package pe.edu.unmsm.ciudadsana.rutas.application.commandhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.rutas.application.command.RegistrarEventoRutaCommand;
import pe.edu.unmsm.ciudadsana.rutas.application.dto.MetricasRutaDto;
import pe.edu.unmsm.ciudadsana.rutas.application.dto.RutaParadaDto;
import pe.edu.unmsm.ciudadsana.rutas.application.dto.RutaResponseDto;
import pe.edu.unmsm.ciudadsana.rutas.application.dto.RutaVersionDto;
import pe.edu.unmsm.ciudadsana.rutas.application.port.in.RegistrarEventoRutaUseCase;
import pe.edu.unmsm.ciudadsana.rutas.application.port.out.RutasEventPublisherPort;
import pe.edu.unmsm.ciudadsana.rutas.application.port.out.RutasPersistencePort;
import pe.edu.unmsm.ciudadsana.rutas.domain.enums.TipoEventoRuta;
import pe.edu.unmsm.ciudadsana.rutas.domain.model.Ruta;
import pe.edu.unmsm.ciudadsana.rutas.domain.model.RutaEvento;
import pe.edu.unmsm.ciudadsana.rutas.domain.model.RutaParada;
import pe.edu.unmsm.ciudadsana.rutas.domain.model.RutaVersion;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.AlertaExternoId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.ContenedorExternoId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.MetricasRuta;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.RutaEventoId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.RutaId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.UUID;

@Component
public class RegistrarEventoRutaCommandHandler implements RegistrarEventoRutaUseCase {

    private final RutasPersistencePort rutasPersistencePort;
    private final RutasEventPublisherPort eventPublisher;

    public RegistrarEventoRutaCommandHandler(RutasPersistencePort rutasPersistencePort,
                                             RutasEventPublisherPort eventPublisher) {
        this.rutasPersistencePort = rutasPersistencePort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Result<RutaResponseDto> registrar(RegistrarEventoRutaCommand cmd) {
        RutaId rutaId = RutaId.of(cmd.rutaId());
        TenantId tenantId = TenantId.of(cmd.tenantId());
        Ruta ruta = rutasPersistencePort.findByIdAndTenantId(rutaId, tenantId).orElse(null);
        if (ruta == null) {
            return Result.failure(ErrorCode.RUTA_NO_ENCONTRADA);
        }
        TipoEventoRuta tipo = TipoEventoRuta.valueOf(cmd.tipoEvento());
        RutaEvento evento = RutaEvento.create(
                RutaEventoId.of(UUID.randomUUID()),
                ruta.getId(),
                tipo,
                cmd.descripcion(),
                cmd.datosJson(),
                Instant.now()
        );
        ruta.registrarEvento(evento);
        ruta = rutasPersistencePort.save(ruta);
        eventPublisher.publishAll(ruta.pullDomainEvents());
        return Result.success(toDto(ruta));
    }

    private MetricasRutaDto toMetricasDto(MetricasRuta m) {
        return new MetricasRutaDto(m.distanciaM(), m.duracionS(), m.cargaKg());
    }

    private RutaParadaDto toParadaDto(RutaParada p) {
        return new RutaParadaDto(
                p.getId().value(), p.getRutaVersionId().value(), p.getZonaId().value(),
                p.getContenedorId().map(ContenedorExternoId::value).orElse(null),
                p.getOrden(), p.getEta().orElse(null),
                p.getHoraLlegadaReal().orElse(null), p.getHoraSalidaReal().orElse(null),
                p.getDemandaEstimadaKg(), p.getCargaAcumuladaKg(),
                p.getEstado().name(), p.getCreadoEn()
        );
    }

    private RutaVersionDto toVersionDto(RutaVersion v) {
        return new RutaVersionDto(
                v.getId().value(), v.getRutaId().value(), v.getVersion(),
                v.getMotivo().name(),
                v.getAlertaIdExterno().map(AlertaExternoId::value).orElse(null),
                v.getGeneradoPor().name(), toMetricasDto(v.getMetricas()),
                v.getParadas().stream().map(this::toParadaDto).toList(),
                v.getCreadoEn()
        );
    }

    private RutaResponseDto toDto(Ruta r) {
        return new RutaResponseDto(
                r.getId().value(), r.getTenantId().value(),
                r.getTurnoId().value(), r.getDistritoId().value(),
                r.getDepositoOrigenId().value(), r.getDepositoDestinoId().value(),
                r.getFecha(), r.getTipoRuta().name(), r.getEstado().name(),
                toMetricasDto(r.getMetricas()),
                r.getVersionActual().map(this::toVersionDto).orElse(null),
                r.getCreadoEn(), r.getActualizadoEn().orElse(null)
        );
    }
}
