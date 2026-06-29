package pe.edu.unmsm.ciudadsana.auditoria.application.port.in;

import pe.edu.unmsm.ciudadsana.auditoria.application.dto.EventoAuditoriaDto;
import pe.edu.unmsm.ciudadsana.auditoria.application.query.ListarEventosAuditoriaQuery;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface ListarEventosAuditoriaUseCase {
    Result<PageResult<EventoAuditoriaDto>> listar(ListarEventosAuditoriaQuery query);
}
