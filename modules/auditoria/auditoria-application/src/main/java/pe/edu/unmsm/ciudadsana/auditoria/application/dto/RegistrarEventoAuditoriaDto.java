package pe.edu.unmsm.ciudadsana.auditoria.application.dto;

import java.util.UUID;

public record RegistrarEventoAuditoriaDto(
        UUID tenantId,
        UUID usuarioId,
        String modulo,
        String accion,
        String entidad,
        UUID entidadId,
        String datosAntes,
        String datosDespues
) {}
