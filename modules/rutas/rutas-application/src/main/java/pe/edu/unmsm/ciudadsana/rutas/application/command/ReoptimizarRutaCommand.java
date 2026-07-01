package pe.edu.unmsm.ciudadsana.rutas.application.command;

import pe.edu.unmsm.ciudadsana.integracion.application.dto.SolicitudOptimizacionDto;

import java.util.List;
import java.util.UUID;

public record ReoptimizarRutaCommand(
        UUID tenantId,
        UUID rutaId,
        List<SolicitudOptimizacionDto.UnidadSolicitudDto> unidades,
        List<SolicitudOptimizacionDto.ZonaSolicitudDto> zonas,
        List<SolicitudOptimizacionDto.AlertaCriticaSolicitudDto> alertasCriticas,
        SolicitudOptimizacionDto.ParametrosSolverDto parametrosSolver,
        String motivo
) {}
