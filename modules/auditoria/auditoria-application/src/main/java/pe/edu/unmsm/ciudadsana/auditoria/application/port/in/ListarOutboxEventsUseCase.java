package pe.edu.unmsm.ciudadsana.auditoria.application.port.in;

import pe.edu.unmsm.ciudadsana.auditoria.application.dto.OutboxEventDto;
import pe.edu.unmsm.ciudadsana.auditoria.application.query.ListarOutboxEventsQuery;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface ListarOutboxEventsUseCase {
    Result<PageResult<OutboxEventDto>> listar(ListarOutboxEventsQuery query);
}
