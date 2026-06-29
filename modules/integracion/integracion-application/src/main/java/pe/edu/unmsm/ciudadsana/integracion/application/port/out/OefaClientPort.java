package pe.edu.unmsm.ciudadsana.integracion.application.port.out;

import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.UUID;

public interface OefaClientPort {

    Result<Void> notificarAlertaCritica(UUID tenantId, UUID alertaId, String descripcion);

    Result<Boolean> verificarDisponibilidad();
}
