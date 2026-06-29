package pe.edu.unmsm.ciudadsana.auditoria.application.service;

import org.springframework.stereotype.Service;
import pe.edu.unmsm.ciudadsana.auditoria.application.command.RegistrarOutboxEventCommand;
import pe.edu.unmsm.ciudadsana.auditoria.application.port.in.RegistrarOutboxEventUseCase;
import pe.edu.unmsm.ciudadsana.auditoria.application.port.out.OutboxEventPort;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.UUID;

@Service
public class OutboxEventPortAdapter implements OutboxEventPort {

    private final RegistrarOutboxEventUseCase registrarUseCase;

    public OutboxEventPortAdapter(RegistrarOutboxEventUseCase registrarUseCase) {
        this.registrarUseCase = registrarUseCase;
    }

    @Override
    public Result<Void> publicar(
            UUID tenantId,
            String aggregateType,
            UUID aggregateId,
            String eventType,
            String payload
    ) {
        return registrarUseCase.registrar(new RegistrarOutboxEventCommand(
                tenantId, aggregateType, aggregateId, eventType, payload
        ));
    }
}
