package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unmsm.ciudadsana.operacion.application.command.ActivarDistritoCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.ActivarDistritoUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.DistritosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DistritoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Component
public class ActivarDistritoCommandHandler implements ActivarDistritoUseCase {

    private final DistritosPersistencePort distritosPersistencePort;

    public ActivarDistritoCommandHandler(DistritosPersistencePort distritosPersistencePort) {
        this.distritosPersistencePort = distritosPersistencePort;
    }

    @Override
    @Transactional
    public Result<Void> activar(ActivarDistritoCommand cmd) {
        return distritosPersistencePort.findById(DistritoId.of(cmd.id()), TenantId.of(cmd.tenantId()))
                .map(distrito -> {
                    distrito.activar();
                    distritosPersistencePort.save(distrito);
                    return Result.<Void>success(null);
                })
                .orElse(Result.failure(ErrorCode.DISTRITO_NO_ENCONTRADO));
    }
}
