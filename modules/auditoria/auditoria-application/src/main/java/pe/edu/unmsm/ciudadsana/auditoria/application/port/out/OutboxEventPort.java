package pe.edu.unmsm.ciudadsana.auditoria.application.port.out;

import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.UUID;

public interface OutboxEventPort {

    Result<Void> publicar(
            UUID tenantId,
            String aggregateType,
            UUID aggregateId,
            String eventType,
            String payload
    );
}
