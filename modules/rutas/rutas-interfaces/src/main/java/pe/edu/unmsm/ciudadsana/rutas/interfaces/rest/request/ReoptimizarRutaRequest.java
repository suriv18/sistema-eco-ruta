package pe.edu.unmsm.ciudadsana.rutas.interfaces.rest.request;

import jakarta.validation.constraints.NotEmpty;
import pe.edu.unmsm.ciudadsana.integracion.application.dto.SolicitudOptimizacionDto;

import java.util.List;

public record ReoptimizarRutaRequest(
        @NotEmpty List<SolicitudOptimizacionDto.UnidadSolicitudDto> unidades,
        @NotEmpty List<SolicitudOptimizacionDto.ZonaSolicitudDto> zonas,
        List<SolicitudOptimizacionDto.AlertaCriticaSolicitudDto> alertasCriticas,
        SolicitudOptimizacionDto.ParametrosSolverDto parametrosSolver,
        String motivo
) {}
