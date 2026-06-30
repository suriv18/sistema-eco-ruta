package pe.edu.unmsm.ciudadsana.auditoria.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.auditoria.application.command.RegistrarOutboxEventCommand;
import pe.edu.unmsm.ciudadsana.auditoria.application.port.out.AuditoriaPersistencePort;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RegistrarOutboxEventCommandHandlerTest {

    @Mock
    AuditoriaPersistencePort persistencePort;

    @InjectMocks
    RegistrarOutboxEventCommandHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID AGGREGATE_ID = UUID.fromString("44444444-4444-4444-4444-444444444444");

    @Test
    void registrar_conDatosValidos_retornaSuccess() {
        RegistrarOutboxEventCommand cmd = new RegistrarOutboxEventCommand(
                TENANT_ID, "DispositivoGps", AGGREGATE_ID,
                "DISPOSITIVO_REGISTRADO", "{\"dispositivoId\":\"" + AGGREGATE_ID + "\"}"
        );

        Result<Void> result = handler.registrar(cmd);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue()).isNull();
        verify(persistencePort).saveOutboxEvent(any());
    }
}
