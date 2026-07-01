package pe.edu.unmsm.ciudadsana.auditoria.application.port.out;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface OutboxDispatcherPort {

    record OutboxEventView(
        UUID id,
        UUID tenantId,
        UUID aggregateId,
        String eventType,
        String payload
    ) {}

    List<OutboxEventView> findPendientes();

    void marcarPublicado(UUID id, Instant publicadoEn);

    void marcarError(UUID id, String errorMensaje);
}
