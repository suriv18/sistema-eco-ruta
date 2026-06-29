package pe.edu.unmsm.ciudadsana.auditoria.application.command;

import java.util.UUID;

public record RegistrarEventoAuditoriaCommand(
        UUID tenantId,
        UUID usuarioId,
        String modulo,
        String accion,
        String entidad,
        UUID entidadId,
        String datosAntes,
        String datosDespues
) {}
