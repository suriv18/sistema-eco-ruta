package pe.edu.unmsm.ciudadsana.telemetria.application.command;

import java.time.Instant;
import java.util.UUID;

public record ProcesarPingGpsCommand(
        UUID tenantId,
        UUID dispositivoId,
        UUID unidadExternoId,
        UUID rutaExternoId,
        Instant ts,
        double latitud,
        double longitud,
        Double velocidadKmh,
        Double rumboGrados,
        Double precisionM,
        String origen
) {}
