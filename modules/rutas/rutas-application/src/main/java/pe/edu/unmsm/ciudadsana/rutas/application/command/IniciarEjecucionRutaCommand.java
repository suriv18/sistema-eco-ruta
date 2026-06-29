package pe.edu.unmsm.ciudadsana.rutas.application.command;

import java.util.UUID;

public record IniciarEjecucionRutaCommand(
        UUID rutaId,
        UUID tenantId
) {}
