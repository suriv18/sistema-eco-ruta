package pe.edu.unmsm.ciudadsana.integracion.infrastructure.external.oefa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.integracion.application.port.out.OefaClientPort;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.UUID;

@Component
public class OefaMockClientAdapter implements OefaClientPort {

    private static final Logger log = LoggerFactory.getLogger(OefaMockClientAdapter.class);

    @Override
    public Result<Void> notificarAlertaCritica(UUID tenantId, UUID alertaId, String descripcion) {
        log.info("[OEFA MOCK] Notificando alerta crítica — tenantId={}, alertaId={}, descripcion={}",
                tenantId, alertaId, descripcion);
        return Result.success(null);
    }

    @Override
    public Result<Boolean> verificarDisponibilidad() {
        return Result.success(true);
    }
}
