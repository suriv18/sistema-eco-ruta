package pe.edu.unmsm.ciudadsana.rutas.application.query;

import java.util.UUID;

public record ObtenerRutaDetalleQuery(
        UUID rutaId,
        UUID tenantId
) {}
