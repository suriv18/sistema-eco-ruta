package pe.edu.unmsm.ciudadsana.ciudadano.application.query;

import java.util.UUID;

public record ListarCiudadanosQuery(
    UUID tenantId,
    int page,
    int size
) {}
