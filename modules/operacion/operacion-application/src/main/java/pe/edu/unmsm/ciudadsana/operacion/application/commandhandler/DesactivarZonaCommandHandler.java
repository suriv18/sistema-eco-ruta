package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.operacion.application.command.DesactivarZonaCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.DesactivarZonaUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.OperacionEventPublisherPort;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.ZonasPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Zona;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ZonaId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.Optional;

@Component
public class DesactivarZonaCommandHandler implements DesactivarZonaUseCase {

    private final ZonasPersistencePort zonasPersistencePort;
    private final OperacionEventPublisherPort eventPublisher;

    public DesactivarZonaCommandHandler(ZonasPersistencePort zonasPersistencePort, OperacionEventPublisherPort eventPublisher) {
        this.zonasPersistencePort = zonasPersistencePort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Result<Void> desactivar(DesactivarZonaCommand command) {
        TenantId tenantId = TenantId.of(command.tenantId());
        ZonaId zonaId = ZonaId.of(command.zonaId());
        Optional<Zona> zonaOpt = zonasPersistencePort.findById(zonaId, tenantId);
        if (zonaOpt.isEmpty()) return Result.failure(ErrorCode.ZONA_NO_ENCONTRADA);
        Zona zona = zonaOpt.get();
        try {
            zona.desactivar();
        } catch (IllegalStateException e) {
            return Result.failure(ErrorCode.OPERACION_NO_PERMITIDA, e.getMessage());
        }
        zonasPersistencePort.save(zona);
        eventPublisher.publishAll(zona.pullDomainEvents());
        return Result.success(null);
    }
}
