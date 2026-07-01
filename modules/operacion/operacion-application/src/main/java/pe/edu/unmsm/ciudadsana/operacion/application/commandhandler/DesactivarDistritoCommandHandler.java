package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unmsm.ciudadsana.operacion.application.command.DesactivarDistritoCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.DesactivarDistritoUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.DistritosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DistritoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Component
public class DesactivarDistritoCommandHandler implements DesactivarDistritoUseCase {

    private final DistritosPersistencePort distritosPersistencePort;

    public DesactivarDistritoCommandHandler(DistritosPersistencePort distritosPersistencePort) {
        this.distritosPersistencePort = distritosPersistencePort;
    }

    @Override
    @Transactional
    public Result<Void> desactivar(DesactivarDistritoCommand cmd) {
        return distritosPersistencePort.findById(DistritoId.of(cmd.id()), TenantId.of(cmd.tenantId()))
                .map(distrito -> {
                    try {
                        distrito.desactivar();
                    } catch (IllegalStateException e) {
                        return Result.<Void>failure(ErrorCode.VALIDACION_ERROR, e.getMessage());
                    }
                    distritosPersistencePort.save(distrito);
                    return Result.<Void>success(null);
                })
                .orElse(Result.failure(ErrorCode.DISTRITO_NO_ENCONTRADO));
    }
}
