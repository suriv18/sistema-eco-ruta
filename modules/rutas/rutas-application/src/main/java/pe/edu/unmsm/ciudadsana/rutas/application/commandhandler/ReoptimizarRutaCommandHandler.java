package pe.edu.unmsm.ciudadsana.rutas.application.commandhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.integracion.application.dto.RespuestaOptimizacionDto;
import pe.edu.unmsm.ciudadsana.integracion.application.dto.SolicitudOptimizacionDto;
import pe.edu.unmsm.ciudadsana.integracion.application.port.out.OptimizationClientPort;
import pe.edu.unmsm.ciudadsana.rutas.application.command.AgregarVersionRutaCommand;
import pe.edu.unmsm.ciudadsana.rutas.application.command.ReoptimizarRutaCommand;
import pe.edu.unmsm.ciudadsana.rutas.application.dto.MetricasRutaDto;
import pe.edu.unmsm.ciudadsana.rutas.application.dto.RutaParadaDto;
import pe.edu.unmsm.ciudadsana.rutas.application.dto.RutaResponseDto;
import pe.edu.unmsm.ciudadsana.rutas.application.dto.RutaVersionDto;
import pe.edu.unmsm.ciudadsana.rutas.application.port.in.AgregarVersionRutaUseCase;
import pe.edu.unmsm.ciudadsana.rutas.application.port.in.ReoptimizarRutaUseCase;
import pe.edu.unmsm.ciudadsana.rutas.application.port.out.RutasEventPublisherPort;
import pe.edu.unmsm.ciudadsana.rutas.application.port.out.RutasPersistencePort;
import pe.edu.unmsm.ciudadsana.rutas.domain.model.Ruta;
import pe.edu.unmsm.ciudadsana.rutas.domain.model.RutaParada;
import pe.edu.unmsm.ciudadsana.rutas.domain.model.RutaVersion;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.AlertaExternoId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.ContenedorExternoId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.MetricasRuta;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.RutaId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReoptimizarRutaCommandHandler implements ReoptimizarRutaUseCase {

    private final OptimizationClientPort optimizationPort;
    private final RutasPersistencePort rutasPersistencePort;
    private final AgregarVersionRutaUseCase agregarVersionUseCase;
    private final RutasEventPublisherPort eventPublisher;

    public ReoptimizarRutaCommandHandler(OptimizationClientPort optimizationPort,
                                         RutasPersistencePort rutasPersistencePort,
                                         AgregarVersionRutaUseCase agregarVersionUseCase,
                                         RutasEventPublisherPort eventPublisher) {
        this.optimizationPort = optimizationPort;
        this.rutasPersistencePort = rutasPersistencePort;
        this.agregarVersionUseCase = agregarVersionUseCase;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Result<RutaResponseDto> reoptimizar(ReoptimizarRutaCommand cmd) {
        // 1. Buscar ruta existente
        Ruta ruta = rutasPersistencePort.findByIdAndTenantId(
                RutaId.of(cmd.rutaId()), TenantId.of(cmd.tenantId())
        ).orElse(null);
        if (ruta == null) {
            return Result.failure(ErrorCode.RUTA_NO_ENCONTRADA);
        }

        // 2. Armar solicitud usando datos de la ruta existente
        SolicitudOptimizacionDto solicitud = new SolicitudOptimizacionDto(
                cmd.tenantId(),
                ruta.getDistritoId().value(),
                ruta.getFecha(),
                new SolicitudOptimizacionDto.UbicacionDto(0.0, 0.0),
                new SolicitudOptimizacionDto.UbicacionDto(0.0, 0.0),
                cmd.unidades(),
                cmd.zonas(),
                cmd.alertasCriticas() != null ? cmd.alertasCriticas() : List.of(),
                cmd.parametrosSolver()
        );

        // 3. Llamar optimizador
        Result<RespuestaOptimizacionDto> respResult = optimizationPort.optimizar(solicitud);
        if (respResult instanceof Result.Failure<RespuestaOptimizacionDto> f) {
            return Result.failure(f.error());
        }
        RespuestaOptimizacionDto respuesta = ((Result.Success<RespuestaOptimizacionDto>) respResult).value();

        // 4. Agregar nueva versión
        String motivo = cmd.motivo() != null ? cmd.motivo() : "RECALCULO";
        List<AgregarVersionRutaCommand.NuevaParadaDto> paradas = buildParadas(respuesta);
        double distanciaTotal = respuesta.distanciaTotalM();
        int duracionTotal = respuesta.duracionTotalS();
        double cargaTotal = respuesta.rutasPorUnidad().stream()
                .mapToDouble(RespuestaOptimizacionDto.RutaUnidadDto::cargaTotalKg).sum();

        AgregarVersionRutaCommand versionCmd = new AgregarVersionRutaCommand(
                cmd.tenantId(), cmd.rutaId(),
                motivo, null, "SISTEMA",
                distanciaTotal, duracionTotal, cargaTotal,
                paradas
        );
        return agregarVersionUseCase.agregarVersion(versionCmd);
    }

    private List<AgregarVersionRutaCommand.NuevaParadaDto> buildParadas(RespuestaOptimizacionDto respuesta) {
        int orden = 1;
        List<AgregarVersionRutaCommand.NuevaParadaDto> paradas = new ArrayList<>();
        for (RespuestaOptimizacionDto.RutaUnidadDto rutaUnidad : respuesta.rutasPorUnidad()) {
            for (RespuestaOptimizacionDto.ParadaDto parada : rutaUnidad.paradas()) {
                Instant eta = parseEta(parada.eta());
                paradas.add(new AgregarVersionRutaCommand.NuevaParadaDto(
                        parada.zonaId(), null, orden++, eta, parada.cargaAcumuladaKg()
                ));
            }
        }
        return paradas;
    }

    private Instant parseEta(String eta) {
        if (eta == null) return null;
        try {
            String[] parts = eta.split(":");
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);
            return java.time.LocalDate.now()
                    .atTime(hour, minute)
                    .toInstant(ZoneOffset.UTC);
        } catch (Exception e) {
            return null;
        }
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
