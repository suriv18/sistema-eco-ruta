package pe.edu.unmsm.ciudadsana.rutas.application.command;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record AgregarVersionRutaCommand(
        UUID tenantId,
        UUID rutaId,
        String motivo,
        UUID alertaIdExterno,
        String generadoPor,
        double distanciaM,
        int duracionS,
        double cargaKg,
        List<NuevaParadaDto> paradas
) {
    public record NuevaParadaDto(
            UUID zonaId,
            UUID contenedorId,
            int orden,
            Instant eta,
            double demandaEstimadaKg
    ) {}
}
