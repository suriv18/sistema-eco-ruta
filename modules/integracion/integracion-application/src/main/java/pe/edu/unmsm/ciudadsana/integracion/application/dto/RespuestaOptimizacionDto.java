package pe.edu.unmsm.ciudadsana.integracion.application.dto;

import java.util.List;
import java.util.UUID;

public record RespuestaOptimizacionDto(
        String estado,
        String mensaje,
        long resueltoEnMs,
        double distanciaTotalM,
        int duracionTotalS,
        List<RutaUnidadDto> rutasPorUnidad
) {

    public record RutaUnidadDto(
            UUID unidadId,
            double distanciaM,
            int duracionS,
            double cargaTotalKg,
            List<ParadaDto> paradas
    ) {}

    public record ParadaDto(
            UUID zonaId,
            int orden,
            String eta,
            double cargaAcumuladaKg
    ) {}
}
