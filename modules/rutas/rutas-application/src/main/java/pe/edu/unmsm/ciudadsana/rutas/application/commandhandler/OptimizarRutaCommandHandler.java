package pe.edu.unmsm.ciudadsana.rutas.application.commandhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.integracion.application.dto.RespuestaOptimizacionDto;
import pe.edu.unmsm.ciudadsana.integracion.application.dto.SolicitudOptimizacionDto;
import pe.edu.unmsm.ciudadsana.integracion.application.port.out.OptimizationClientPort;
import pe.edu.unmsm.ciudadsana.rutas.application.command.AgregarVersionRutaCommand;
import pe.edu.unmsm.ciudadsana.rutas.application.command.OptimizarRutaCommand;
import pe.edu.unmsm.ciudadsana.rutas.application.dto.RutaResponseDto;
import pe.edu.unmsm.ciudadsana.rutas.application.port.in.AgregarVersionRutaUseCase;
import pe.edu.unmsm.ciudadsana.rutas.application.port.in.OptimizarRutaUseCase;
import pe.edu.unmsm.ciudadsana.rutas.application.port.out.RutasEventPublisherPort;
import pe.edu.unmsm.ciudadsana.rutas.application.port.out.RutasPersistencePort;
import pe.edu.unmsm.ciudadsana.rutas.domain.enums.TipoRuta;
import pe.edu.unmsm.ciudadsana.rutas.domain.model.Ruta;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.DepositoExternoId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.DistritoExternoId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.RutaId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.TurnoExternoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class OptimizarRutaCommandHandler implements OptimizarRutaUseCase {

    private final OptimizationClientPort optimizationPort;
    private final RutasPersistencePort rutasPersistencePort;
    private final AgregarVersionRutaUseCase agregarVersionUseCase;
    private final RutasEventPublisherPort eventPublisher;

    public OptimizarRutaCommandHandler(OptimizationClientPort optimizationPort,
                                       RutasPersistencePort rutasPersistencePort,
                                       AgregarVersionRutaUseCase agregarVersionUseCase,
                                       RutasEventPublisherPort eventPublisher) {
        this.optimizationPort = optimizationPort;
        this.rutasPersistencePort = rutasPersistencePort;
        this.agregarVersionUseCase = agregarVersionUseCase;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Result<RutaResponseDto> optimizar(OptimizarRutaCommand cmd) {
        // 1. Armar SolicitudOptimizacionDto
        SolicitudOptimizacionDto solicitud = new SolicitudOptimizacionDto(
                cmd.tenantId(),
                cmd.distritoId(),
                cmd.fecha(),
                new SolicitudOptimizacionDto.UbicacionDto(0.0, 0.0),
                new SolicitudOptimizacionDto.UbicacionDto(0.0, 0.0),
                cmd.unidades(),
                cmd.zonas(),
                cmd.alertasCriticas() != null ? cmd.alertasCriticas() : List.of(),
                cmd.parametrosSolver()
        );

        // 2. Llamar al optimizador
        Result<RespuestaOptimizacionDto> respResult = optimizationPort.optimizar(solicitud);
        if (respResult instanceof Result.Failure<RespuestaOptimizacionDto> f) {
            return Result.failure(f.error());
        }
        RespuestaOptimizacionDto respuesta = ((Result.Success<RespuestaOptimizacionDto>) respResult).value();

        // 3. Crear la Ruta (estado BORRADOR)
        RutaId rutaId = RutaId.of(UUID.randomUUID());
        TenantId tenantId = TenantId.of(cmd.tenantId());
        Ruta ruta = Ruta.create(
                rutaId,
                tenantId,
                TurnoExternoId.of(cmd.turnoId()),
                DistritoExternoId.of(cmd.distritoId()),
                DepositoExternoId.of(cmd.depositoOrigenId()),
                DepositoExternoId.of(cmd.depositoDestinoId()),
                cmd.fecha(),
                TipoRuta.valueOf(cmd.tipoRuta()),
                Instant.now()
        );
        ruta = rutasPersistencePort.save(ruta);
        eventPublisher.publishAll(ruta.pullDomainEvents());

        // 4. Agregar primera versión con las paradas del optimizador
        List<AgregarVersionRutaCommand.NuevaParadaDto> paradas = buildParadas(respuesta);
        double distanciaTotal = respuesta.distanciaTotalM();
        int duracionTotal = respuesta.duracionTotalS();
        double cargaTotal = respuesta.rutasPorUnidad().stream()
                .mapToDouble(RespuestaOptimizacionDto.RutaUnidadDto::cargaTotalKg).sum();

        AgregarVersionRutaCommand versionCmd = new AgregarVersionRutaCommand(
                cmd.tenantId(), rutaId.value(),
                "INICIAL", null, "SISTEMA",
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
}
