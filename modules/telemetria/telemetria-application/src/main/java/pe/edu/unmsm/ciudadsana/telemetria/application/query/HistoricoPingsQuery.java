package pe.edu.unmsm.ciudadsana.telemetria.application.query;

import java.time.Instant;
import java.util.UUID;

public record HistoricoPingsQuery(
        UUID tenantId,
        UUID unidadExternoId,  // nullable — si null, no filtrar por unidad
        Instant desde,          // nullable
        Instant hasta,          // nullable
        int page,
        int size
) {}
