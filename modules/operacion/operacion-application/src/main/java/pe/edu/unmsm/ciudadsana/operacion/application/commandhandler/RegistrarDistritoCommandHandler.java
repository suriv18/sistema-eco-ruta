package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.operacion.application.command.RegistrarDistritoCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.DistritoResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.RegistrarDistritoUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.DistritosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.OperacionEventPublisherPort;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Distrito;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DistritoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.UUID;

@Component
public class RegistrarDistritoCommandHandler implements RegistrarDistritoUseCase {

    private final DistritosPersistencePort distritosPersistencePort;
    private final OperacionEventPublisherPort eventPublisher;

    public RegistrarDistritoCommandHandler(DistritosPersistencePort distritosPersistencePort, OperacionEventPublisherPort eventPublisher) {
        this.distritosPersistencePort = distritosPersistencePort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Result<DistritoResponseDto> registrar(RegistrarDistritoCommand command) {
        TenantId tenantId = TenantId.of(command.tenantId());
        if (distritosPersistencePort.existsByNombre(command.nombre(), tenantId)) {
            return Result.failure(ErrorCode.VALIDACION_ERROR, "Ya existe un distrito con el nombre: " + command.nombre());
        }
        Distrito distrito = Distrito.create(
            DistritoId.of(UUID.randomUUID()),
            tenantId,
            command.nombre(),
            command.ubigeo(),
            Instant.now()
        );
        distrito = distritosPersistencePort.save(distrito);
        eventPublisher.publishAll(distrito.pullDomainEvents());
        return Result.success(new DistritoResponseDto(
            distrito.getId().value(),
            distrito.getTenantId().value(),
            distrito.getNombre(),
            distrito.getUbigeo().orElse(null),
            distrito.getEstado().name(),
            distrito.getCreadoEn()
        ));
    }
}
