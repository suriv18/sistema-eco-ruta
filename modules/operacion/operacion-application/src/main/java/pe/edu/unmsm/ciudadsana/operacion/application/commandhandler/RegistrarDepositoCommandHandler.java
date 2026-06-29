package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.operacion.application.command.RegistrarDepositoCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.DepositoResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.RegistrarDepositoUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.DepositosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.DistritosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.TipoDeposito;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Deposito;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DepositoId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DistritoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.UUID;

@Component
public class RegistrarDepositoCommandHandler implements RegistrarDepositoUseCase {

    private final DistritosPersistencePort distritosPersistencePort;
    private final DepositosPersistencePort depositosPersistencePort;

    public RegistrarDepositoCommandHandler(DistritosPersistencePort distritosPersistencePort, DepositosPersistencePort depositosPersistencePort) {
        this.distritosPersistencePort = distritosPersistencePort;
        this.depositosPersistencePort = depositosPersistencePort;
    }

    @Override
    public Result<DepositoResponseDto> registrar(RegistrarDepositoCommand command) {
        TenantId tenantId = TenantId.of(command.tenantId());
        DistritoId distritoId = DistritoId.of(command.distritoId());
        if (distritosPersistencePort.findById(distritoId, tenantId).isEmpty()) {
            return Result.failure(ErrorCode.DISTRITO_NO_ENCONTRADO);
        }
        TipoDeposito tipoDeposito;
        try {
            tipoDeposito = TipoDeposito.valueOf(command.tipo());
        } catch (IllegalArgumentException e) {
            return Result.failure(ErrorCode.VALIDACION_ERROR, "TipoDeposito inválido: " + command.tipo());
        }
        Deposito deposito = Deposito.create(
            DepositoId.of(UUID.randomUUID()),
            tenantId,
            distritoId,
            command.nombre(),
            tipoDeposito,
            Instant.now()
        );
        depositosPersistencePort.save(deposito);
        return Result.success(new DepositoResponseDto(
            deposito.getId().value(),
            deposito.getTenantId().value(),
            deposito.getDistritoId().value(),
            deposito.getNombre(),
            deposito.getTipo().name(),
            deposito.getEstado().name(),
            deposito.getCreadoEn()
        ));
    }
}
