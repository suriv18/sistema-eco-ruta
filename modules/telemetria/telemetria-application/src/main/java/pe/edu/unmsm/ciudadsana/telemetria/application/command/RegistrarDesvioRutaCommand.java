package pe.edu.unmsm.ciudadsana.telemetria.application.command;

import java.util.UUID;

public record RegistrarDesvioRutaCommand(
        UUID tenantId,
        UUID unidadExternoId,
        UUID rutaExternoId,
        double latitud,
        double longitud,
        double distanciaDesvioM,
        String severidad
) {}
