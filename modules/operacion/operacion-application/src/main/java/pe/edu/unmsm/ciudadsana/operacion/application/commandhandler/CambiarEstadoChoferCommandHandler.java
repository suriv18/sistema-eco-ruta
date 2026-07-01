package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unmsm.ciudadsana.operacion.application.command.CambiarEstadoChoferCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.ChoferResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.CambiarEstadoChoferUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.ChoferesPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoChofer;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Chofer;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ChoferId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Component
public class CambiarEstadoChoferCommandHandler implements CambiarEstadoChoferUseCase {

    private final ChoferesPersistencePort choferesPersistencePort;

    public CambiarEstadoChoferCommandHandler(ChoferesPersistencePort choferesPersistencePort) {
        this.choferesPersistencePort = choferesPersistencePort;
    }

    @Override
    @Transactional
    public Result<ChoferResponseDto> cambiarEstado(CambiarEstadoChoferCommand cmd) {
        return choferesPersistencePort.findById(ChoferId.of(cmd.id()), TenantId.of(cmd.tenantId()))
                .map(chofer -> {
                    try {
                        switch (cmd.nuevoEstado()) {
                            case ACTIVO -> chofer.activar();
                            case SUSPENDIDO -> chofer.suspender();
                            case INACTIVO -> chofer.desactivar();
                        }
                    } catch (IllegalStateException e) {
                        return Result.<ChoferResponseDto>failure(ErrorCode.OPERACION_NO_PERMITIDA, e.getMessage());
                    }
                    Chofer guardado = choferesPersistencePort.save(chofer);
                    return Result.success(new ChoferResponseDto(
                            guardado.getId().value(),
                            guardado.getTenantId().value(),
                            guardado.getNombres(),
                            guardado.getApellidos(),
                            guardado.getDni().orElse(null),
                            guardado.getLicencia().orElse(null),
                            guardado.getTelefono().orElse(null),
                            guardado.getEstado().name(),
                            guardado.getCreadoEn()
                    ));
                })
                .orElse(Result.failure(ErrorCode.CHOFER_NO_ENCONTRADO));
    }
}
