package pe.edu.unmsm.ciudadsana.ciudadano.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.AlertaResponseDto;
import pe.edu.unmsm.ciudadsana.ciudadano.application.mapper.AlertaDtoMapper;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.in.ListarAlertasPorZonaUseCase;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.out.AlertasPersistencePort;
import pe.edu.unmsm.ciudadsana.ciudadano.application.query.ListarAlertasPorZonaQuery;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.model.AlertaCiudadana;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.ZonaExternoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Component
public class ListarAlertasPorZonaQueryHandler implements ListarAlertasPorZonaUseCase {

    private final AlertasPersistencePort alertasPersistencePort;
    private final AlertaDtoMapper alertaDtoMapper;

    public ListarAlertasPorZonaQueryHandler(AlertasPersistencePort alertasPersistencePort, AlertaDtoMapper alertaDtoMapper) {
        this.alertasPersistencePort = alertasPersistencePort;
        this.alertaDtoMapper = alertaDtoMapper;
    }

    @Override
    public Result<PageResult<AlertaResponseDto>> listarPorZona(ListarAlertasPorZonaQuery query) {
        ZonaExternoId zonaId = ZonaExternoId.of(query.zonaExternoId());
        TenantId tenantId = TenantId.of(query.tenantId());
        PageResult<AlertaCiudadana> pageResult = alertasPersistencePort.findAllByZonaAndTenantId(zonaId, tenantId, query.page(), query.size());
        return Result.success(pageResult.map(alertaDtoMapper::toDto));
    }
}
