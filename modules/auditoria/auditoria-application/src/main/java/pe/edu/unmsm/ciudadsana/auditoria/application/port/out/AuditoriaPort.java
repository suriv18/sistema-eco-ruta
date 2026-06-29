package pe.edu.unmsm.ciudadsana.auditoria.application.port.out;

import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.UUID;

public interface AuditoriaPort {

    Result<Void> registrar(
            UUID tenantId,
            UUID usuarioId,
            String modulo,
            String accion,
            String entidad,
            UUID entidadId,
            String datosAntes,
            String datosDespues
    );
}
