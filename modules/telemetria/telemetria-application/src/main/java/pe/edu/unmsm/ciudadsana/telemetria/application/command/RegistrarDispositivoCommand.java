package pe.edu.unmsm.ciudadsana.telemetria.application.command;

import java.util.UUID;

public record RegistrarDispositivoCommand(
        UUID tenantId,
        UUID unidadExternoId,
        String imei,
        String proveedor
) {}
