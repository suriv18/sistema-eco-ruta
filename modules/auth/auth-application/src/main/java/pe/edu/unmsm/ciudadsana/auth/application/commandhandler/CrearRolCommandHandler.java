package pe.edu.unmsm.ciudadsana.auth.application.commandhandler;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unmsm.ciudadsana.auth.application.command.CrearRolCommand;
import pe.edu.unmsm.ciudadsana.auth.application.dto.RolResponseDto;
import pe.edu.unmsm.ciudadsana.auth.application.port.in.CrearRolUseCase;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.EventPublisherPort;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.RolPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.domain.model.Rol;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.RolId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.event.DomainEvent;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.List;
import java.util.UUID;

@Component
public class CrearRolCommandHandler implements CrearRolUseCase {

    private final RolPersistencePort rolPort;
    private final EventPublisherPort eventPublisher;

    public CrearRolCommandHandler(RolPersistencePort rolPort, EventPublisherPort eventPublisher) {
        this.rolPort = rolPort;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    @Override
    public Result<RolResponseDto> crear(CrearRolCommand command) {
        if (rolPort.existsByCodigo(command.codigo())) {
            return Result.failure(ErrorCode.ROL_DUPLICADO);
        }
        Rol rol = Rol.create(RolId.of(UUID.randomUUID()), command.codigo(), command.nombre(), command.descripcion());
        List<DomainEvent> eventos = rol.pullDomainEvents();
        Rol saved = rolPort.save(rol);
        eventPublisher.publishAll(eventos);
        return Result.success(RolResponseDto.from(saved));
    }
}
