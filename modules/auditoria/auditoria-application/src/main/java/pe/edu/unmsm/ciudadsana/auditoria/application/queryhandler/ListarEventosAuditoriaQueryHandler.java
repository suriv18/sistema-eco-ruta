package pe.edu.unmsm.ciudadsana.auditoria.application.queryhandler;

import org.springframework.stereotype.Service;
import pe.edu.unmsm.ciudadsana.auditoria.application.dto.EventoAuditoriaDto;
import pe.edu.unmsm.ciudadsana.auditoria.application.port.in.ListarEventosAuditoriaUseCase;
import pe.edu.unmsm.ciudadsana.auditoria.application.port.out.AuditoriaPersistencePort;
import pe.edu.unmsm.ciudadsana.auditoria.application.query.ListarEventosAuditoriaQuery;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Service
public class ListarEventosAuditoriaQueryHandler implements ListarEventosAuditoriaUseCase {

    private final AuditoriaPersistencePort persistencePort;

    public ListarEventosAuditoriaQueryHandler(AuditoriaPersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    @Override
    public Result<PageResult<EventoAuditoriaDto>> listar(ListarEventosAuditoriaQuery query) {
        PageResult<EventoAuditoriaDto> page = persistencePort.findEventos(
                query.tenantId(),
                query.modulo(),
                query.entidad(),
                query.usuarioId(),
                query.fechaDesde(),
                query.fechaHasta(),
                query.page(),
                query.size()
        );
        return Result.success(page);
    }
}
