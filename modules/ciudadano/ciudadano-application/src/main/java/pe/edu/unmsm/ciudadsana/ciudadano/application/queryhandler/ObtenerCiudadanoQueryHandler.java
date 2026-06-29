package pe.edu.unmsm.ciudadsana.ciudadano.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.CiudadanoResponseDto;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.in.ObtenerCiudadanoUseCase;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.out.CiudadanosPersistencePort;
import pe.edu.unmsm.ciudadsana.ciudadano.application.query.ObtenerCiudadanoQuery;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.model.Ciudadano;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.CiudadanoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Component
public class ObtenerCiudadanoQueryHandler implements ObtenerCiudadanoUseCase {

    private final CiudadanosPersistencePort ciudadanosPersistencePort;

    public ObtenerCiudadanoQueryHandler(CiudadanosPersistencePort ciudadanosPersistencePort) {
        this.ciudadanosPersistencePort = ciudadanosPersistencePort;
    }

    @Override
    public Result<CiudadanoResponseDto> obtener(ObtenerCiudadanoQuery query) {
        CiudadanoId ciudadanoId = CiudadanoId.of(query.ciudadanoId());
        TenantId tenantId = TenantId.of(query.tenantId());
        return ciudadanosPersistencePort.findByIdAndTenantId(ciudadanoId, tenantId)
            .map(c -> Result.success(toDto(c)))
            .orElse(Result.failure(ErrorCode.RECURSO_NO_ENCONTRADO));
    }

    private CiudadanoResponseDto toDto(Ciudadano c) {
        return new CiudadanoResponseDto(
            c.getId().value(), c.getTenantId().value(),
            c.getNombres().orElse(null), c.getApellidos().orElse(null),
            c.getEmail().orElse(null), c.getTelefono().orElse(null),
            c.getDocumento().orElse(null), c.getEstado().name(), c.getCreadoEn()
        );
    }
}
