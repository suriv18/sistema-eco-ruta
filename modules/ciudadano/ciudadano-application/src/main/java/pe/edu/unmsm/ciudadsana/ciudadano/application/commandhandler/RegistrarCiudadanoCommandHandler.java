package pe.edu.unmsm.ciudadsana.ciudadano.application.commandhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.ciudadano.application.command.RegistrarCiudadanoCommand;
import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.CiudadanoResponseDto;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.in.RegistrarCiudadanoUseCase;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.out.CiudadanoEventPublisherPort;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.out.CiudadanosPersistencePort;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.model.Ciudadano;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.CiudadanoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.UUID;

@Component
public class RegistrarCiudadanoCommandHandler implements RegistrarCiudadanoUseCase {

    private final CiudadanosPersistencePort ciudadanosPersistencePort;
    private final CiudadanoEventPublisherPort eventPublisher;

    public RegistrarCiudadanoCommandHandler(CiudadanosPersistencePort ciudadanosPersistencePort,
                                             CiudadanoEventPublisherPort eventPublisher) {
        this.ciudadanosPersistencePort = ciudadanosPersistencePort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Result<CiudadanoResponseDto> registrar(RegistrarCiudadanoCommand cmd) {
        CiudadanoId id = CiudadanoId.of(UUID.randomUUID());
        TenantId tenantId = TenantId.of(cmd.tenantId());
        Ciudadano c = Ciudadano.create(id, tenantId, cmd.nombres(), cmd.apellidos(), cmd.email(), cmd.telefono(), cmd.documento(), Instant.now());
        ciudadanosPersistencePort.save(c);
        eventPublisher.publishAll(c.pullDomainEvents());
        return Result.success(toDto(c));
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
