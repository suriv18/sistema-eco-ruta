package pe.edu.unmsm.ciudadsana.telemetria.application.command;

import java.util.UUID;

public record AtenderDesvioCommand(
        UUID desvioId,
        UUID tenantId
) {}
