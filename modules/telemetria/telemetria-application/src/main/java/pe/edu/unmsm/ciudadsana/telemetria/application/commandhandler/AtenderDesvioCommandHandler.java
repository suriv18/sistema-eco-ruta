package pe.edu.unmsm.ciudadsana.telemetria.application.commandhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.telemetria.application.command.AtenderDesvioCommand;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.in.AtenderDesvioUseCase;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.DesvioRutaPersistencePort;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.DesvioRutaPersistencePort.DesvioView;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.DesvioId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.Optional;

@Component
public class AtenderDesvioCommandHandler implements AtenderDesvioUseCase {

    private final DesvioRutaPersistencePort desvioRutaPersistencePort;

    public AtenderDesvioCommandHandler(DesvioRutaPersistencePort desvioRutaPersistencePort) {
        this.desvioRutaPersistencePort = desvioRutaPersistencePort;
    }

    @Override
    public Result<Void> atender(AtenderDesvioCommand cmd) {
        TenantId tenantId = TenantId.of(cmd.tenantId());
        Optional<DesvioView> desvioOpt = desvioRutaPersistencePort.findById(DesvioId.of(cmd.desvioId()), tenantId);
        if (desvioOpt.isEmpty()) {
            return Result.failure(ErrorCode.RECURSO_NO_ENCONTRADO);
        }
        DesvioView desvio = desvioOpt.get();
        if (!"ABIERTO".equals(desvio.estado())) {
            return Result.failure(ErrorCode.OPERACION_NO_PERMITIDA, "Solo se pueden atender desvíos en estado ABIERTO");
        }
        DesvioView atendido = new DesvioView(
                desvio.id(), desvio.tenantId(), desvio.unidadExternoId(), desvio.rutaExternoId(),
                desvio.latitud(), desvio.longitud(), desvio.distanciaDesvioM(),
                desvio.severidad(), "ATENDIDO", desvio.detectadoEn(), Instant.now()
        );
        desvioRutaPersistencePort.save(atendido);
        return Result.<Void>success(null);
    }
}
