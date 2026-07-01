package pe.edu.unmsm.ciudadsana.rutas.interfaces.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import pe.edu.unmsm.ciudadsana.integracion.application.dto.SolicitudOptimizacionDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record OptimizarRutaRequest(
        @NotNull UUID turnoId,
        @NotNull UUID distritoId,
        @NotNull UUID depositoOrigenId,
        @NotNull UUID depositoDestinoId,
        @NotNull LocalDate fecha,
        @NotBlank String tipoRuta,
        @NotEmpty List<SolicitudOptimizacionDto.UnidadSolicitudDto> unidades,
        @NotEmpty List<SolicitudOptimizacionDto.ZonaSolicitudDto> zonas,
        List<SolicitudOptimizacionDto.AlertaCriticaSolicitudDto> alertasCriticas,
        SolicitudOptimizacionDto.ParametrosSolverDto parametrosSolver
) {}
