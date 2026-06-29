package pe.edu.unmsm.ciudadsana.integracion.infrastructure.external.optimization;

import com.fasterxml.jackson.annotation.JsonProperty;
import pe.edu.unmsm.ciudadsana.integracion.application.dto.RespuestaOptimizacionDto;

import java.util.List;
import java.util.UUID;

record OptimizationResponse(
        String estado,
        String mensaje,
        @JsonProperty("resuelto_en_ms") long resueltoEnMs,
        @JsonProperty("distancia_total_m") double distanciaTotalM,
        @JsonProperty("duracion_total_s") int duracionTotalS,
        @JsonProperty("rutas_por_unidad") List<RutaUnidadJson> rutasPorUnidad
) {
    record RutaUnidadJson(
            @JsonProperty("unidad_id") UUID unidadId,
            @JsonProperty("distancia_m") double distanciaM,
            @JsonProperty("duracion_s") int duracionS,
            @JsonProperty("carga_total_kg") double cargaTotalKg,
            List<ParadaJson> paradas
    ) {}

    record ParadaJson(
            @JsonProperty("zona_id") UUID zonaId,
            int orden,
            String eta,
            @JsonProperty("carga_acumulada_kg") double cargaAcumuladaKg
    ) {}

    RespuestaOptimizacionDto toDto() {
        List<RespuestaOptimizacionDto.RutaUnidadDto> rutasDto = rutasPorUnidad != null
                ? rutasPorUnidad.stream().map(r -> new RespuestaOptimizacionDto.RutaUnidadDto(
                        r.unidadId(), r.distanciaM(), r.duracionS(), r.cargaTotalKg(),
                        r.paradas() != null
                                ? r.paradas().stream().map(p -> new RespuestaOptimizacionDto.ParadaDto(
                                        p.zonaId(), p.orden(), p.eta(), p.cargaAcumuladaKg()
                                  )).toList()
                                : List.of()
                  )).toList()
                : List.of();
        return new RespuestaOptimizacionDto(
                estado, mensaje, resueltoEnMs, distanciaTotalM, duracionTotalS, rutasDto
        );
    }
}
