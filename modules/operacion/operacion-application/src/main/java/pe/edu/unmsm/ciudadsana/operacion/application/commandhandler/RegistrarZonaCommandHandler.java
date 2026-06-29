package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.operacion.application.command.RegistrarZonaCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.ZonaResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.RegistrarZonaUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.DistritosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.OperacionEventPublisherPort;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.ZonasPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.TipoZona;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Zona;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.CodigoZona;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DistritoId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.PrioridadBase;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ZonaId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.UUID;

@Component
public class RegistrarZonaCommandHandler implements RegistrarZonaUseCase {

    private final DistritosPersistencePort distritosPersistencePort;
    private final ZonasPersistencePort zonasPersistencePort;
    private final OperacionEventPublisherPort eventPublisher;

    public RegistrarZonaCommandHandler(DistritosPersistencePort distritosPersistencePort, ZonasPersistencePort zonasPersistencePort, OperacionEventPublisherPort eventPublisher) {
        this.distritosPersistencePort = distritosPersistencePort;
        this.zonasPersistencePort = zonasPersistencePort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Result<ZonaResponseDto> registrar(RegistrarZonaCommand command) {
        TenantId tenantId = TenantId.of(command.tenantId());
        DistritoId distritoId = DistritoId.of(command.distritoId());
        if (distritosPersistencePort.findById(distritoId, tenantId).isEmpty()) {
            return Result.failure(ErrorCode.DISTRITO_NO_ENCONTRADO);
        }
        if (zonasPersistencePort.existsByCodigo(CodigoZona.of(command.codigo()), tenantId)) {
            return Result.failure(ErrorCode.VALIDACION_ERROR, "Ya existe una zona con el código: " + command.codigo());
        }
        TipoZona tipoZona;
        try {
            tipoZona = TipoZona.valueOf(command.tipoZona());
        } catch (IllegalArgumentException e) {
            return Result.failure(ErrorCode.VALIDACION_ERROR, "TipoZona inválido: " + command.tipoZona());
        }
        Zona zona = Zona.create(
            ZonaId.of(UUID.randomUUID()),
            tenantId,
            distritoId,
            CodigoZona.of(command.codigo()),
            command.nombre(),
            tipoZona,
            PrioridadBase.of(command.prioridad()),
            Instant.now()
        );
        zona = zonasPersistencePort.save(zona);
        eventPublisher.publishAll(zona.pullDomainEvents());
        return Result.success(new ZonaResponseDto(
            zona.getId().value(),
            zona.getTenantId().value(),
            zona.getDistritoId().value(),
            zona.getCodigo().value(),
            zona.getNombre(),
            zona.getTipo().name(),
            zona.getPrioridad().value(),
            zona.getEstado().name(),
            zona.getCreadoEn()
        ));
    }
}
