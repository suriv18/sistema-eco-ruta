package pe.edu.unmsm.ciudadsana.auditoria.application.queryhandler;

import org.springframework.stereotype.Service;
import pe.edu.unmsm.ciudadsana.auditoria.application.dto.OutboxEventDto;
import pe.edu.unmsm.ciudadsana.auditoria.application.port.in.ListarOutboxEventsUseCase;
import pe.edu.unmsm.ciudadsana.auditoria.application.port.out.AuditoriaPersistencePort;
import pe.edu.unmsm.ciudadsana.auditoria.application.query.ListarOutboxEventsQuery;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Service
public class ListarOutboxEventsQueryHandler implements ListarOutboxEventsUseCase {

    private final AuditoriaPersistencePort persistencePort;

    public ListarOutboxEventsQueryHandler(AuditoriaPersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    @Override
    public Result<PageResult<OutboxEventDto>> listar(ListarOutboxEventsQuery query) {
        PageResult<OutboxEventDto> page = persistencePort.findOutboxEvents(
                query.tenantId(),
                query.estado(),
                query.eventType(),
                query.page(),
                query.size()
        );
        return Result.success(page);
    }
}
