package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unmsm.ciudadsana.operacion.application.command.DesactivarDepositoCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.DesactivarDepositoUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.DepositosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DepositoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Component
public class DesactivarDepositoCommandHandler implements DesactivarDepositoUseCase {

    private final DepositosPersistencePort depositosPersistencePort;

    public DesactivarDepositoCommandHandler(DepositosPersistencePort depositosPersistencePort) {
        this.depositosPersistencePort = depositosPersistencePort;
    }

    @Override
    @Transactional
    public Result<Void> desactivar(DesactivarDepositoCommand cmd) {
        return depositosPersistencePort.findByIdAndTenantId(DepositoId.of(cmd.id()), TenantId.of(cmd.tenantId()))
                .map(deposito -> {
                    deposito.desactivar();
                    depositosPersistencePort.save(deposito);
                    return Result.<Void>success(null);
                })
                .orElse(Result.failure(ErrorCode.DEPOSITO_NO_ENCONTRADO));
    }
}
