package pe.edu.unmsm.ciudadsana.kpi.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.kpi.application.dto.KpiZonaDto;
import pe.edu.unmsm.ciudadsana.kpi.application.port.in.ListarKpisZonaUseCase;
import pe.edu.unmsm.ciudadsana.kpi.application.port.out.KpiPersistencePort;
import pe.edu.unmsm.ciudadsana.kpi.application.query.ListarKpisZonaQuery;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Component
public class ListarKpisZonaQueryHandler implements ListarKpisZonaUseCase {

    private final KpiPersistencePort kpiPersistencePort;

    public ListarKpisZonaQueryHandler(KpiPersistencePort kpiPersistencePort) {
        this.kpiPersistencePort = kpiPersistencePort;
    }

    @Override
    public Result<PageResult<KpiZonaDto>> listar(ListarKpisZonaQuery query) {
        PageResult<KpiZonaDto> page = kpiPersistencePort.findKpisZona(
                query.tenantId(), query.zonaId(), query.fechaDesde(), query.fechaHasta(), query.page(), query.size()
        );
        return Result.success(page);
    }
}
