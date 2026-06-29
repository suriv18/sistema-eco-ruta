package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.operacion.application.command.RegistrarChoferCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.ChoferResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.RegistrarChoferUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.ChoferesPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.OperacionEventPublisherPort;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Chofer;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ChoferId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.UUID;

@Component
public class RegistrarChoferCommandHandler implements RegistrarChoferUseCase {

    private final ChoferesPersistencePort choferesPersistencePort;
    private final OperacionEventPublisherPort eventPublisher;

    public RegistrarChoferCommandHandler(ChoferesPersistencePort choferesPersistencePort, OperacionEventPublisherPort eventPublisher) {
        this.choferesPersistencePort = choferesPersistencePort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Result<ChoferResponseDto> registrar(RegistrarChoferCommand command) {
        TenantId tenantId = TenantId.of(command.tenantId());
        if (command.dni() != null && !command.dni().isBlank()) {
            if (choferesPersistencePort.existsByDni(command.dni(), tenantId)) {
                return Result.failure(ErrorCode.VALIDACION_ERROR, "Ya existe un chofer con el DNI: " + command.dni());
            }
        }
        Chofer chofer = Chofer.create(
            ChoferId.of(UUID.randomUUID()),
            tenantId,
            command.nombres(),
            command.apellidos(),
            command.dni(),
            command.licencia(),
            command.telefono(),
            Instant.now()
        );
        chofer = choferesPersistencePort.save(chofer);
        eventPublisher.publishAll(chofer.pullDomainEvents());
        return Result.success(new ChoferResponseDto(
            chofer.getId().value(),
            chofer.getTenantId().value(),
            chofer.getNombres(),
            chofer.getApellidos(),
            chofer.getDni().orElse(null),
            chofer.getLicencia().orElse(null),
            chofer.getTelefono().orElse(null),
            chofer.getEstado().name(),
            chofer.getCreadoEn()
        ));
    }
}
