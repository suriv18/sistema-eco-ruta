package pe.edu.unmsm.ciudadsana.integracion.infrastructure.external.optimization;

import com.fasterxml.jackson.annotation.JsonProperty;
import pe.edu.unmsm.ciudadsana.integracion.application.dto.SolicitudOptimizacionDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

record OptimizationRequest(
        @JsonProperty("tenant_id") UUID tenantId,
        @JsonProperty("distrito_id") UUID distritoId,
        @JsonProperty("fecha_operacion") LocalDate fechaOperacion,
        @JsonProperty("deposito_inicio") UbicacionJson depositoInicio,
        @JsonProperty("deposito_fin") UbicacionJson depositoFin,
        List<UnidadJson> unidades,
        List<ZonaJson> zonas,
        @JsonProperty("alertas_criticas") List<AlertaCriticaJson> alertasCriticas,
        @JsonProperty("parametros_solver") ParametrosSolverJson parametrosSolver
) {
    record UbicacionJson(double latitud, double longitud) {}

    record UnidadJson(
            @JsonProperty("unidad_id") UUID unidadId,
            @JsonProperty("capacidad_kg") double capacidadKg,
            @JsonProperty("inicio_disponibilidad") String inicioDisponibilidad,
            @JsonProperty("fin_disponibilidad") String finDisponibilidad
    ) {}

    record ZonaJson(
            @JsonProperty("zona_id") UUID zonaId,
            double latitud,
            double longitud,
            @JsonProperty("demanda_kg") double demandaKg,
            @JsonProperty("ventana_inicio") String ventanaInicio,
            @JsonProperty("ventana_fin") String ventanaFin,
            int prioridad
    ) {}

    record AlertaCriticaJson(
            @JsonProperty("alerta_id") UUID alertaId,
            @JsonProperty("zona_id") UUID zonaId,
            @JsonProperty("nivel_criticidad") String nivelCriticidad
    ) {}

    record ParametrosSolverJson(
            @JsonProperty("tiempo_limite_s") int tiempoLimiteS,
            String objetivo,
            @JsonProperty("penalta_critica") double penaltaCritica
    ) {}

    static OptimizationRequest from(SolicitudOptimizacionDto dto) {
        return new OptimizationRequest(
                dto.tenantId(),
                dto.distritoId(),
                dto.fechaOperacion(),
                new UbicacionJson(dto.depositoInicio().latitud(), dto.depositoInicio().longitud()),
                new UbicacionJson(dto.depositoFin().latitud(), dto.depositoFin().longitud()),
                dto.unidades().stream().map(u -> new UnidadJson(
                        u.unidadId(), u.capacidadKg(), u.inicioDisponibilidad(), u.finDisponibilidad()
                )).toList(),
                dto.zonas().stream().map(z -> new ZonaJson(
                        z.zonaId(), z.latitud(), z.longitud(), z.demandaKg(),
                        z.ventanaInicio(), z.ventanaFin(), z.prioridad()
                )).toList(),
                dto.alertasCriticas() != null
                        ? dto.alertasCriticas().stream().map(a -> new AlertaCriticaJson(
                                a.alertaId(), a.zonaId(), a.nivelCriticidad()
                          )).toList()
                        : List.of(),
                dto.parametrosSolver() != null
                        ? new ParametrosSolverJson(
                                dto.parametrosSolver().tiempoLimiteS(),
                                dto.parametrosSolver().objetivo(),
                                dto.parametrosSolver().penaltaCritica()
                          )
                        : new ParametrosSolverJson(30, "DISTANCIA", 1000.0)
        );
    }
}
