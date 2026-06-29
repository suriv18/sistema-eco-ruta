package pe.edu.unmsm.ciudadsana.rutas.application.command;

import java.util.UUID;

public record FinalizarRutaCommand(
        UUID rutaId,
        UUID tenantId
) {}
