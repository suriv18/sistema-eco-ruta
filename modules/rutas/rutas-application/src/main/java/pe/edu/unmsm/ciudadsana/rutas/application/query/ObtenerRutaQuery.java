package pe.edu.unmsm.ciudadsana.rutas.application.query;

import java.util.UUID;

public record ObtenerRutaQuery(
        UUID rutaId,
        UUID tenantId
) {}
