package pe.edu.unmsm.ciudadsana.rutas.application.commandhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.rutas.application.command.AgregarVersionRutaCommand;
import pe.edu.unmsm.ciudadsana.rutas.application.dto.MetricasRutaDto;
import pe.edu.unmsm.ciudadsana.rutas.application.dto.RutaParadaDto;
import pe.edu.unmsm.ciudadsana.rutas.application.dto.RutaResponseDto;
import pe.edu.unmsm.ciudadsana.rutas.application.dto.RutaVersionDto;
import pe.edu.unmsm.ciudadsana.rutas.application.port.in.AgregarVersionRutaUseCase;
import pe.edu.unmsm.ciudadsana.rutas.application.port.out.RutasEventPublisherPort;
import pe.edu.unmsm.ciudadsana.rutas.application.port.out.RutasPersistencePort;
import pe.edu.unmsm.ciudadsana.rutas.domain.enums.GeneradoPor;
import pe.edu.unmsm.ciudadsana.rutas.domain.enums.MotivoVersion;
import pe.edu.unmsm.ciudadsana.rutas.domain.model.Ruta;
import pe.edu.unmsm.ciudadsana.rutas.domain.model.RutaParada;
import pe.edu.unmsm.ciudadsana.rutas.domain.model.RutaVersion;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.AlertaExternoId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.ContenedorExternoId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.MetricasRuta;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.ParadaId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.RutaId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.RutaVersionId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.ZonaExternoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.UUID;

@Component
public class AgregarVersionRutaCommandHandler implements AgregarVersionRutaUseCase {

    private final RutasPersistencePort rutasPersistencePort;
    private final RutasEventPublisherPort eventPublisher;

    public AgregarVersionRutaCommandHandler(RutasPersistencePort rutasPersistencePort,
                                            RutasEventPublisherPort eventPublisher) {
        this.rutasPersistencePort = rutasPersistencePort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Result<RutaResponseDto> agregarVersion(AgregarVersionRutaCommand cmd) {
        RutaId rutaId = RutaId.of(cmd.rutaId());
        TenantId tenantId = TenantId.of(cmd.tenantId());
        Ruta ruta = rutasPersistencePort.findByIdAndTenantId(rutaId, tenantId).orElse(null);
        if (ruta == null) {
            return Result.failure(ErrorCode.RUTA_NO_ENCONTRADA);
        }
        if (cmd.paradas() == null || cmd.paradas().isEmpty()) {
            return Result.failure(ErrorCode.RUTA_SIN_PARADAS);
        }
        int nextVersion = ruta.getHistorialVersiones().size() + 1;
        MotivoVersion motivo = MotivoVersion.valueOf(cmd.motivo());
        GeneradoPor generadoPor = GeneradoPor.valueOf(cmd.generadoPor());
        AlertaExternoId alertaId = cmd.alertaIdExterno() != null
                ? AlertaExternoId.of(cmd.alertaIdExterno()) : null;
        MetricasRuta metricas = MetricasRuta.of(cmd.distanciaM(), cmd.duracionS(), cmd.cargaKg());
        RutaVersion version = RutaVersion.create(
                RutaVersionId.of(UUID.randomUUID()),
                ruta.getId(),
                nextVersion,
                motivo,
                alertaId,
                generadoPor,
                metricas,
                Instant.now()
        );
        for (AgregarVersionRutaCommand.NuevaParadaDto p : cmd.paradas()) {
            RutaParada parada = RutaParada.create(
                    ParadaId.of(UUID.randomUUID()),
                    version.getId(),
                    ZonaExternoId.of(p.zonaId()),
                    p.contenedorId() != null ? ContenedorExternoId.of(p.contenedorId()) : null,
                    p.orden(),
                    p.eta(),
                    p.demandaEstimadaKg(),
                    Instant.now()
            );
            version.agregarParada(parada);
        }
        ruta.agregarVersion(version);
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
