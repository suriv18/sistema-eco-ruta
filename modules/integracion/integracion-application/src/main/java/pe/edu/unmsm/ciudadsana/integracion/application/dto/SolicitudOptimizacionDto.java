package pe.edu.unmsm.ciudadsana.integracion.application.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record SolicitudOptimizacionDto(
        UUID tenantId,
        UUID distritoId,
        LocalDate fechaOperacion,
        UbicacionDto depositoInicio,
        UbicacionDto depositoFin,
        List<UnidadSolicitudDto> unidades,
        List<ZonaSolicitudDto> zonas,
        List<AlertaCriticaSolicitudDto> alertasCriticas,
        ParametrosSolverDto parametrosSolver
) {

    public record UbicacionDto(double latitud, double longitud) {}

    public record UnidadSolicitudDto(
            UUID unidadId,
            double capacidadKg,
            String inicioDisponibilidad,
            String finDisponibilidad
    ) {}

    public record ZonaSolicitudDto(
            UUID zonaId,
            double latitud,
            double longitud,
            double demandaKg,
            String ventanaInicio,
            String ventanaFin,
            int prioridad
    ) {}

    public record AlertaCriticaSolicitudDto(
            UUID alertaId,
            UUID zonaId,
            String nivelCriticidad
    ) {}

    public record ParametrosSolverDto(
            int tiempoLimiteS,
            String objetivo,
            double penaltaCritica
    ) {}
}
