package pe.edu.unmsm.ciudadsana.kpi.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.kpi.application.dto.ResumenOperativoDto;
import pe.edu.unmsm.ciudadsana.kpi.application.port.in.ObtenerResumenDiarioUseCase;
import pe.edu.unmsm.ciudadsana.kpi.application.port.out.KpiPersistencePort;
import pe.edu.unmsm.ciudadsana.kpi.application.query.ObtenerResumenDiarioQuery;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Component
public class ObtenerResumenDiarioQueryHandler implements ObtenerResumenDiarioUseCase {

    private final KpiPersistencePort kpiPersistencePort;

    public ObtenerResumenDiarioQueryHandler(KpiPersistencePort kpiPersistencePort) {
        this.kpiPersistencePort = kpiPersistencePort;
    }

    @Override
    public Result<ResumenOperativoDto> obtener(ObtenerResumenDiarioQuery query) {
        ResumenOperativoDto resumen = kpiPersistencePort
                .findResumenByDistritoAndFecha(query.tenantId(), query.distritoId(), query.fecha())
                .orElse(null);
        if (resumen == null) {
            return Result.failure(ErrorCode.RECURSO_NO_ENCONTRADO,
                    "No hay resumen operativo para el distrito y fecha indicados");
        }
        return Result.success(resumen);
    }
}
