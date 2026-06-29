package pe.edu.unmsm.ciudadsana.telemetria.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.telemetria.application.dto.EventoConectividadResponseDto;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.in.ListarEventosConectividadUseCase;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.EventoConectividadPersistencePort;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.EventoConectividadPersistencePort.EventoConectividadView;
import pe.edu.unmsm.ciudadsana.telemetria.application.query.ListarEventosConectividadQuery;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.UnidadExternoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Component
public class ListarEventosConectividadQueryHandler implements ListarEventosConectividadUseCase {

    private final EventoConectividadPersistencePort eventoConectividadPersistencePort;

    public ListarEventosConectividadQueryHandler(EventoConectividadPersistencePort eventoConectividadPersistencePort) {
        this.eventoConectividadPersistencePort = eventoConectividadPersistencePort;
    }

    @Override
    public Result<PageResult<EventoConectividadResponseDto>> listar(ListarEventosConectividadQuery query) {
        PageResult<EventoConectividadView> pageResult = eventoConectividadPersistencePort.findAllByUnidad(
                UnidadExternoId.of(query.unidadExternoId()),
                TenantId.of(query.tenantId()),
                query.page(),
                query.size()
        );
        return Result.success(pageResult.map(this::toDto));
    }

    private EventoConectividadResponseDto toDto(EventoConectividadView e) {
        return new EventoConectividadResponseDto(
                e.id(),
                e.tenantId(),
                e.unidadExternoId(),
                e.dispositivoId(),
                e.tipoEvento(),
                e.detalle(),
                e.detectadoEn()
        );
    }
}
