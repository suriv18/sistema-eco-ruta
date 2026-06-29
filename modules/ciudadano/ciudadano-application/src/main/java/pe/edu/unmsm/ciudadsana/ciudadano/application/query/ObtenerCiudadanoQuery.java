package pe.edu.unmsm.ciudadsana.ciudadano.application.query;

import java.util.UUID;

public record ObtenerCiudadanoQuery(
    UUID ciudadanoId,
    UUID tenantId
) {}
