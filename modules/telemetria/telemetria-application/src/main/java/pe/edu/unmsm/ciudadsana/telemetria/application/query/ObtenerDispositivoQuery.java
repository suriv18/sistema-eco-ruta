package pe.edu.unmsm.ciudadsana.telemetria.application.query;

import java.util.UUID;

public record ObtenerDispositivoQuery(
        UUID dispositivoId,
        UUID tenantId
) {}
