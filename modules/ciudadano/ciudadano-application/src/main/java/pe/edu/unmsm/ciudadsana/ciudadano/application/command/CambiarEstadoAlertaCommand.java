package pe.edu.unmsm.ciudadsana.ciudadano.application.command;

import java.util.UUID;

public record CambiarEstadoAlertaCommand(
    UUID alertaId,
    UUID tenantId,
    String nuevoEstado,
    String comentario,
    UUID cambiadoPorUsuarioId
) {}
