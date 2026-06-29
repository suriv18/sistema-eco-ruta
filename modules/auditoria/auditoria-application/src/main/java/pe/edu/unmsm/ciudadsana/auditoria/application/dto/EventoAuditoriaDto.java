package pe.edu.unmsm.ciudadsana.auditoria.application.dto;

import java.time.Instant;
import java.util.UUID;

public record EventoAuditoriaDto(
        UUID eventoId,
        UUID tenantId,
        UUID usuarioId,
        String modulo,
        String accion,
        String entidad,
        UUID entidadId,
        String datosAntes,
        String datosDespues,
        Instant creadoEn
) {}
