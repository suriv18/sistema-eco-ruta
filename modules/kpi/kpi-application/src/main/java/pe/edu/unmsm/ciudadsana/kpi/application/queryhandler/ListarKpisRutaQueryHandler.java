package pe.edu.unmsm.ciudadsana.kpi.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.kpi.application.dto.KpiRutaDto;
import pe.edu.unmsm.ciudadsana.kpi.application.port.in.ListarKpisRutaUseCase;
import pe.edu.unmsm.ciudadsana.kpi.application.port.out.KpiPersistencePort;
import pe.edu.unmsm.ciudadsana.kpi.application.query.ListarKpisRutaQuery;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Component
public class ListarKpisRutaQueryHandler implements ListarKpisRutaUseCase {

    private final KpiPersistencePort kpiPersistencePort;

    public ListarKpisRutaQueryHandler(KpiPersistencePort kpiPersistencePort) {
        this.kpiPersistencePort = kpiPersistencePort;
    }

    @Override
    public Result<PageResult<KpiRutaDto>> listar(ListarKpisRutaQuery query) {
        PageResult<KpiRutaDto> page = kpiPersistencePort.findKpisRuta(
                query.tenantId(), query.fechaDesde(), query.fechaHasta(), query.page(), query.size()
        );
        return Result.success(page);
    }
}
