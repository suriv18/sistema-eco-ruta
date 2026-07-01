package pe.edu.unmsm.ciudadsana.operacion.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.ContenedorResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.ObtenerContenedorUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.ContenedoresPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ObtenerContenedorQuery;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ContenedorId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Component
public class ObtenerContenedorQueryHandler implements ObtenerContenedorUseCase {

    private final ContenedoresPersistencePort contenedoresPersistencePort;

    public ObtenerContenedorQueryHandler(ContenedoresPersistencePort contenedoresPersistencePort) {
        this.contenedoresPersistencePort = contenedoresPersistencePort;
    }

    @Override
    public Result<ContenedorResponseDto> obtener(ObtenerContenedorQuery query) {
        return contenedoresPersistencePort.findById(ContenedorId.of(query.id()), TenantId.of(query.tenantId()))
                .map(c -> Result.success(new ContenedorResponseDto(
                        c.getId().value(),
                        c.getTenantId().value(),
                        c.getZonaId().value(),
                        c.getCodigo(),
                        c.getCapacidad().value(),
                        c.getEstadoContenedor().name(),
                        c.getCreadoEn()
                )))
                .orElse(Result.failure(ErrorCode.CONTENEDOR_NO_ENCONTRADO));
    }
}
