package pe.edu.unmsm.ciudadsana.operacion.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.DepositoResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.ListarDepositosUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.DepositosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ListarDepositosQuery;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.List;

@Component
public class ListarDepositosQueryHandler implements ListarDepositosUseCase {

    private final DepositosPersistencePort depositosPersistencePort;

    public ListarDepositosQueryHandler(DepositosPersistencePort depositosPersistencePort) {
        this.depositosPersistencePort = depositosPersistencePort;
    }

    @Override
    public Result<PageResult<DepositoResponseDto>> listar(ListarDepositosQuery query) {
        TenantId tenantId = TenantId.of(query.tenantId());
        long total = depositosPersistencePort.countByTenantId(tenantId);
        List<DepositoResponseDto> dtos = depositosPersistencePort.findAllByTenantId(tenantId).stream()
            .map(d -> new DepositoResponseDto(
                d.getId().value(),
                d.getTenantId().value(),
                d.getDistritoId().value(),
                d.getNombre(),
                d.getTipo().name(),
                d.getEstado().name(),
                d.getCreadoEn()
            ))
            .toList();
        return Result.success(PageResult.of(dtos, query.page(), query.size(), total));
    }
}
