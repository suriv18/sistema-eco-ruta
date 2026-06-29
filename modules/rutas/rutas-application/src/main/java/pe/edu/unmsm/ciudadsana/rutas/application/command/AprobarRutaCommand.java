package pe.edu.unmsm.ciudadsana.rutas.application.command;

import java.util.UUID;

public record AprobarRutaCommand(
        UUID rutaId,
        UUID tenantId
) {}
