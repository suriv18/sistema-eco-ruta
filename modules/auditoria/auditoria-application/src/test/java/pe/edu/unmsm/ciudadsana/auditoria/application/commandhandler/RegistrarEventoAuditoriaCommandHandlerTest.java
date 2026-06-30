package pe.edu.unmsm.ciudadsana.auditoria.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.auditoria.application.command.RegistrarEventoAuditoriaCommand;
import pe.edu.unmsm.ciudadsana.auditoria.application.port.out.AuditoriaPersistencePort;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RegistrarEventoAuditoriaCommandHandlerTest {

    @Mock
    AuditoriaPersistencePort persistencePort;

    @InjectMocks
    RegistrarEventoAuditoriaCommandHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID USUARIO_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final UUID ENTIDAD_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");

    @Test
    void registrar_conDatosValidos_retornaSuccess() {
        RegistrarEventoAuditoriaCommand cmd = new RegistrarEventoAuditoriaCommand(
                TENANT_ID, USUARIO_ID, "telemetria", "CREAR",
                "DispositivoGps", ENTIDAD_ID, null, "{\"imei\":\"123\"}"
        );

        Result<Void> result = handler.registrar(cmd);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue()).isNull();
        verify(persistencePort).saveEventoAuditoria(any());
    }
}
