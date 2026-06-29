package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.operacion.application.command.CambiarEstadoContenedorCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.CambiarEstadoContenedorUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.ContenedoresPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.OperacionEventPublisherPort;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoContenedor;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Contenedor;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ContenedorId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.Optional;

@Component
public class CambiarEstadoContenedorCommandHandler implements CambiarEstadoContenedorUseCase {

    private final ContenedoresPersistencePort contenedoresPersistencePort;
    private final OperacionEventPublisherPort eventPublisher;

    public CambiarEstadoContenedorCommandHandler(ContenedoresPersistencePort contenedoresPersistencePort, OperacionEventPublisherPort eventPublisher) {
        this.contenedoresPersistencePort = contenedoresPersistencePort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Result<Void> cambiarEstado(CambiarEstadoContenedorCommand command) {
        TenantId tenantId = TenantId.of(command.tenantId());
        ContenedorId contenedorId = ContenedorId.of(command.contenedorId());
        Optional<Contenedor> contenedorOpt = contenedoresPersistencePort.findById(contenedorId, tenantId);
        if (contenedorOpt.isEmpty()) return Result.failure(ErrorCode.CONTENEDOR_NO_ENCONTRADO);
        Contenedor contenedor = contenedorOpt.get();
        EstadoContenedor nuevoEstado;
        try {
            nuevoEstado = EstadoContenedor.valueOf(command.nuevoEstado());
        } catch (IllegalArgumentException e) {
            return Result.failure(ErrorCode.VALIDACION_ERROR, "EstadoContenedor inválido: " + command.nuevoEstado());
        }
        try {
            contenedor.cambiarEstado(nuevoEstado);
        } catch (IllegalStateException e) {
            return Result.failure(ErrorCode.OPERACION_NO_PERMITIDA, e.getMessage());
        }
        contenedoresPersistencePort.save(contenedor);
        eventPublisher.publishAll(contenedor.pullDomainEvents());
        return Result.success(null);
    }
}
