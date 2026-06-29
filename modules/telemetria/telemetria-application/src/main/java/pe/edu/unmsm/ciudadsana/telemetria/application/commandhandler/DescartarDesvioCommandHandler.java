package pe.edu.unmsm.ciudadsana.telemetria.application.commandhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.telemetria.application.command.DescartarDesvioCommand;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.in.DescartarDesvioUseCase;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.DesvioRutaPersistencePort;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.DesvioRutaPersistencePort.DesvioView;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.DesvioId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.Optional;

@Component
public class DescartarDesvioCommandHandler implements DescartarDesvioUseCase {

    private final DesvioRutaPersistencePort desvioRutaPersistencePort;

    public DescartarDesvioCommandHandler(DesvioRutaPersistencePort desvioRutaPersistencePort) {
        this.desvioRutaPersistencePort = desvioRutaPersistencePort;
    }

    @Override
    public Result<Void> descartar(DescartarDesvioCommand cmd) {
        TenantId tenantId = TenantId.of(cmd.tenantId());
        Optional<DesvioView> desvioOpt = desvioRutaPersistencePort.findById(DesvioId.of(cmd.desvioId()), tenantId);
        if (desvioOpt.isEmpty()) {
            return Result.failure(ErrorCode.RECURSO_NO_ENCONTRADO);
        }
        DesvioView desvio = desvioOpt.get();
        if (!"ABIERTO".equals(desvio.estado())) {
            return Result.failure(ErrorCode.OPERACION_NO_PERMITIDA, "Solo se pueden descartar desvíos en estado ABIERTO");
        }
        DesvioView descartado = new DesvioView(
                desvio.id(), desvio.tenantId(), desvio.unidadExternoId(), desvio.rutaExternoId(),
                desvio.latitud(), desvio.longitud(), desvio.distanciaDesvioM(),
                desvio.severidad(), "DESCARTADO", desvio.detectadoEn(), desvio.atendidoEn()
        );
        desvioRutaPersistencePort.save(descartado);
        return Result.<Void>success(null);
    }
}
