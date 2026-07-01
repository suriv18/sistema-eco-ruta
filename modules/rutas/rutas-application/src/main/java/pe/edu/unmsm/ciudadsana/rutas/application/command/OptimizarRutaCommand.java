package pe.edu.unmsm.ciudadsana.rutas.application.command;

import pe.edu.unmsm.ciudadsana.integracion.application.dto.SolicitudOptimizacionDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record OptimizarRutaCommand(
        UUID tenantId,
        UUID turnoId,
        UUID distritoId,
        UUID depositoOrigenId,
        UUID depositoDestinoId,
        LocalDate fecha,
        String tipoRuta,
        List<SolicitudOptimizacionDto.UnidadSolicitudDto> unidades,
        List<SolicitudOptimizacionDto.ZonaSolicitudDto> zonas,
        List<SolicitudOptimizacionDto.AlertaCriticaSolicitudDto> alertasCriticas,
        SolicitudOptimizacionDto.ParametrosSolverDto parametrosSolver
) {}
