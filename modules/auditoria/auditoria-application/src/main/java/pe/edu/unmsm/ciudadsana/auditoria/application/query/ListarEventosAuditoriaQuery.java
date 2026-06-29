package pe.edu.unmsm.ciudadsana.auditoria.application.query;

import java.time.LocalDate;
import java.util.UUID;

public record ListarEventosAuditoriaQuery(
        UUID tenantId,
        String modulo,
        String entidad,
        UUID usuarioId,
        LocalDate fechaDesde,
        LocalDate fechaHasta,
        int page,
        int size
) {}
