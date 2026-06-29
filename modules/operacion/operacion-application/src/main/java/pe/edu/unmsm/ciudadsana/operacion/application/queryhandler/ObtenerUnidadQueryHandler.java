package pe.edu.unmsm.ciudadsana.operacion.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.UnidadResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.ObtenerUnidadUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.UnidadesPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ObtenerUnidadQuery;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Unidad;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.UnidadId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.Optional;

@Component
public class ObtenerUnidadQueryHandler implements ObtenerUnidadUseCase {

    private final UnidadesPersistencePort unidadesPersistencePort;

    public ObtenerUnidadQueryHandler(UnidadesPersistencePort unidadesPersistencePort) {
        this.unidadesPersistencePort = unidadesPersistencePort;
    }

    @Override
    public Result<UnidadResponseDto> obtener(ObtenerUnidadQuery query) {
        Optional<Unidad> unidadOpt = unidadesPersistencePort.findById(UnidadId.of(query.unidadId()), TenantId.of(query.tenantId()));
        if (unidadOpt.isEmpty()) return Result.failure(ErrorCode.UNIDAD_NO_ENCONTRADA);
        Unidad unidad = unidadOpt.get();
        return Result.success(new UnidadResponseDto(
            unidad.getId().value(),
            unidad.getTenantId().value(),
            unidad.getPlaca().value(),
            unidad.getCodigoInterno(),
            unidad.getTipo().name(),
            unidad.getCapacidadM3().value(),
            unidad.getCapacidadKg().value(),
            unidad.getEstadoOperativo().name(),
            unidad.getCreadoEn()
        ));
    }
}
