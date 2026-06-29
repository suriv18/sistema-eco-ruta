package pe.edu.unmsm.ciudadsana.telemetria.application.command;

import java.util.UUID;

public record RegistrarEventoConectividadCommand(
        UUID tenantId,
        UUID unidadExternoId,
        UUID dispositivoId,
        String tipoEvento,
        String detalle
) {}
