package pe.edu.unmsm.ciudadsana.auditoria.application.port.out;

import pe.edu.unmsm.ciudadsana.auditoria.application.dto.EventoAuditoriaDto;
import pe.edu.unmsm.ciudadsana.auditoria.application.dto.OutboxEventDto;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;

import java.time.LocalDate;
import java.util.UUID;

public interface AuditoriaPersistencePort {

    void saveEventoAuditoria(EventoAuditoriaDto dto);

    PageResult<EventoAuditoriaDto> findEventos(
            UUID tenantId,
            String modulo,
            String entidad,
            UUID usuarioId,
            LocalDate fechaDesde,
            LocalDate fechaHasta,
            int page,
            int size
    );

    void saveOutboxEvent(OutboxEventDto dto);

    PageResult<OutboxEventDto> findOutboxEvents(
            UUID tenantId,
            String estado,
            String eventType,
            int page,
            int size
    );
}
