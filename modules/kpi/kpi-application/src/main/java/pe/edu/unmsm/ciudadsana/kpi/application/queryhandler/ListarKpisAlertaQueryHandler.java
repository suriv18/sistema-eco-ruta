package pe.edu.unmsm.ciudadsana.kpi.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.kpi.application.dto.KpiAlertaDto;
import pe.edu.unmsm.ciudadsana.kpi.application.port.in.ListarKpisAlertaUseCase;
import pe.edu.unmsm.ciudadsana.kpi.application.port.out.KpiPersistencePort;
import pe.edu.unmsm.ciudadsana.kpi.application.query.ListarKpisAlertaQuery;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Component
public class ListarKpisAlertaQueryHandler implements ListarKpisAlertaUseCase {

    private final KpiPersistencePort kpiPersistencePort;

    public ListarKpisAlertaQueryHandler(KpiPersistencePort kpiPersistencePort) {
        this.kpiPersistencePort = kpiPersistencePort;
    }

    @Override
    public Result<PageResult<KpiAlertaDto>> listar(ListarKpisAlertaQuery query) {
        PageResult<KpiAlertaDto> page = kpiPersistencePort.findKpisAlerta(
                query.tenantId(), query.zonaId(), query.fechaDesde(), query.fechaHasta(), query.page(), query.size()
        );
        return Result.success(page);
    }
}
