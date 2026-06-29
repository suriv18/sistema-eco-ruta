package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.operacion.application.command.CambiarEstadoUnidadCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.CambiarEstadoUnidadUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.OperacionEventPublisherPort;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.UnidadesPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoOperativoUnidad;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Unidad;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.UnidadId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.Optional;

@Component
public class CambiarEstadoUnidadCommandHandler implements CambiarEstadoUnidadUseCase {

    private final UnidadesPersistencePort unidadesPersistencePort;
    private final OperacionEventPublisherPort eventPublisher;

    public CambiarEstadoUnidadCommandHandler(UnidadesPersistencePort unidadesPersistencePort, OperacionEventPublisherPort eventPublisher) {
        this.unidadesPersistencePort = unidadesPersistencePort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Result<Void> cambiarEstado(CambiarEstadoUnidadCommand command) {
        TenantId tenantId = TenantId.of(command.tenantId());
        UnidadId unidadId = UnidadId.of(command.unidadId());
        Optional<Unidad> unidadOpt = unidadesPersistencePort.findById(unidadId, tenantId);
        if (unidadOpt.isEmpty()) return Result.failure(ErrorCode.UNIDAD_NO_ENCONTRADA);
        Unidad unidad = unidadOpt.get();
        EstadoOperativoUnidad nuevoEstado;
        try {
            nuevoEstado = EstadoOperativoUnidad.valueOf(command.nuevoEstado());
        } catch (IllegalArgumentException e) {
            return Result.failure(ErrorCode.VALIDACION_ERROR, "EstadoOperativoUnidad inválido: " + command.nuevoEstado());
        }
        try {
            unidad.cambiarEstadoOperativo(nuevoEstado);
        } catch (IllegalStateException e) {
            return Result.failure(ErrorCode.OPERACION_NO_PERMITIDA, e.getMessage());
        }
        unidadesPersistencePort.save(unidad);
        eventPublisher.publishAll(unidad.pullDomainEvents());
        return Result.success(null);
    }
}
