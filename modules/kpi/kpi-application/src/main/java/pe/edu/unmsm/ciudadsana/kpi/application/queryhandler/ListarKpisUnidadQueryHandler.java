package pe.edu.unmsm.ciudadsana.kpi.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.kpi.application.dto.KpiUnidadDto;
import pe.edu.unmsm.ciudadsana.kpi.application.port.in.ListarKpisUnidadUseCase;
import pe.edu.unmsm.ciudadsana.kpi.application.port.out.KpiPersistencePort;
import pe.edu.unmsm.ciudadsana.kpi.application.query.ListarKpisUnidadQuery;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Component
public class ListarKpisUnidadQueryHandler implements ListarKpisUnidadUseCase {

    private final KpiPersistencePort kpiPersistencePort;

    public ListarKpisUnidadQueryHandler(KpiPersistencePort kpiPersistencePort) {
        this.kpiPersistencePort = kpiPersistencePort;
    }

    @Override
    public Result<PageResult<KpiUnidadDto>> listar(ListarKpisUnidadQuery query) {
        PageResult<KpiUnidadDto> page = kpiPersistencePort.findKpisUnidad(
                query.tenantId(), query.unidadId(), query.fechaDesde(), query.fechaHasta(), query.page(), query.size()
        );
        return Result.success(page);
    }
}
