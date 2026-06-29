package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.operacion.application.command.RegistrarContenedorCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.ContenedorResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.RegistrarContenedorUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.ContenedoresPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.OperacionEventPublisherPort;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.ZonasPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Contenedor;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.CapacidadM3;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ContenedorId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ZonaId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.UUID;

@Component
public class RegistrarContenedorCommandHandler implements RegistrarContenedorUseCase {

    private final ZonasPersistencePort zonasPersistencePort;
    private final ContenedoresPersistencePort contenedoresPersistencePort;
    private final OperacionEventPublisherPort eventPublisher;

    public RegistrarContenedorCommandHandler(ZonasPersistencePort zonasPersistencePort, ContenedoresPersistencePort contenedoresPersistencePort, OperacionEventPublisherPort eventPublisher) {
        this.zonasPersistencePort = zonasPersistencePort;
        this.contenedoresPersistencePort = contenedoresPersistencePort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Result<ContenedorResponseDto> registrar(RegistrarContenedorCommand command) {
        TenantId tenantId = TenantId.of(command.tenantId());
        ZonaId zonaId = ZonaId.of(command.zonaId());
        if (zonasPersistencePort.findById(zonaId, tenantId).isEmpty()) {
            return Result.failure(ErrorCode.ZONA_NO_ENCONTRADA);
        }
        if (contenedoresPersistencePort.existsByCodigo(command.codigo(), tenantId)) {
            return Result.failure(ErrorCode.VALIDACION_ERROR, "Ya existe un contenedor con el código: " + command.codigo());
        }
        Contenedor contenedor = Contenedor.create(
            ContenedorId.of(UUID.randomUUID()),
            tenantId,
            zonaId,
            command.codigo(),
            CapacidadM3.of(command.capacidadM3()),
            Instant.now()
        );
        contenedor = contenedoresPersistencePort.save(contenedor);
        eventPublisher.publishAll(contenedor.pullDomainEvents());
        return Result.success(new ContenedorResponseDto(
            contenedor.getId().value(),
            contenedor.getTenantId().value(),
            contenedor.getZonaId().value(),
            contenedor.getCodigo(),
            contenedor.getCapacidad().value(),
            contenedor.getEstadoContenedor().name(),
            contenedor.getCreadoEn()
        ));
    }
}
