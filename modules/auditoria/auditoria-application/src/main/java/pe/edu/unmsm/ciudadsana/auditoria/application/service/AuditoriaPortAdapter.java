package pe.edu.unmsm.ciudadsana.auditoria.application.service;

import org.springframework.stereotype.Service;
import pe.edu.unmsm.ciudadsana.auditoria.application.command.RegistrarEventoAuditoriaCommand;
import pe.edu.unmsm.ciudadsana.auditoria.application.port.in.RegistrarEventoAuditoriaUseCase;
import pe.edu.unmsm.ciudadsana.auditoria.application.port.out.AuditoriaPort;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.UUID;

@Service
public class AuditoriaPortAdapter implements AuditoriaPort {

    private final RegistrarEventoAuditoriaUseCase registrarUseCase;

    public AuditoriaPortAdapter(RegistrarEventoAuditoriaUseCase registrarUseCase) {
        this.registrarUseCase = registrarUseCase;
    }

    @Override
    public Result<Void> registrar(
            UUID tenantId,
            UUID usuarioId,
            String modulo,
            String accion,
            String entidad,
            UUID entidadId,
            String datosAntes,
            String datosDespues
    ) {
        return registrarUseCase.registrar(new RegistrarEventoAuditoriaCommand(
                tenantId, usuarioId, modulo, accion, entidad, entidadId, datosAntes, datosDespues
        ));
    }
}
