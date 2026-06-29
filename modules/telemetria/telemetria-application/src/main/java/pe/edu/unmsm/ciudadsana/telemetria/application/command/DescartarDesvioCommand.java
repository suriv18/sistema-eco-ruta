package pe.edu.unmsm.ciudadsana.telemetria.application.command;

import java.util.UUID;

public record DescartarDesvioCommand(
        UUID desvioId,
        UUID tenantId
) {}
