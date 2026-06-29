package pe.edu.unmsm.ciudadsana.rutas.application.command;

import java.util.UUID;

public record CancelarRutaCommand(
        UUID rutaId,
        UUID tenantId
) {}
