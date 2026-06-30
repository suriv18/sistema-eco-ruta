package pe.edu.unmsm.ciudadsana.ciudadano.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.ciudadano.application.command.RegistrarCiudadanoCommand;
import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.CiudadanoResponseDto;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.out.CiudadanoEventPublisherPort;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.out.CiudadanosPersistencePort;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrarCiudadanoCommandHandlerTest {

    @Mock
    CiudadanosPersistencePort ciudadanosPersistencePort;

    @Mock
    CiudadanoEventPublisherPort eventPublisher;

    @InjectMocks
    RegistrarCiudadanoCommandHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    private RegistrarCiudadanoCommand comando_valido() {
        return new RegistrarCiudadanoCommand(TENANT_ID, "Juan", "Perez", "juan@example.com", "999888777", "12345678");
    }

    @Test
    void registrar_ciudadano_retorna_dto_con_datos_correctos() {
        when(ciudadanosPersistencePort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        doNothing().when(eventPublisher).publishAll(any());

        Result<CiudadanoResponseDto> result = handler.registrar(comando_valido());

        assertThat(result.isSuccess()).isTrue();
        CiudadanoResponseDto dto = result.getValue();
        assertThat(dto.tenantId()).isEqualTo(TENANT_ID);
        assertThat(dto.nombres()).isEqualTo("Juan");
        assertThat(dto.apellidos()).isEqualTo("Perez");
        assertThat(dto.email()).isEqualTo("juan@example.com");
        assertThat(dto.telefono()).isEqualTo("999888777");
        assertThat(dto.documento()).isEqualTo("12345678");
        assertThat(dto.estado()).isEqualTo("ACTIVO");
        assertThat(dto.id()).isNotNull();
    }

    @Test
    void registrar_ciudadano_invoca_save_y_publish() {
        when(ciudadanosPersistencePort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        doNothing().when(eventPublisher).publishAll(any());

        handler.registrar(comando_valido());

        verify(ciudadanosPersistencePort).save(any());
        verify(eventPublisher).publishAll(any());
    }

    @Test
    void registrar_ciudadano_con_campos_opcionales_nulos_retorna_dto() {
        RegistrarCiudadanoCommand cmd = new RegistrarCiudadanoCommand(TENANT_ID, null, null, null, null, null);
        when(ciudadanosPersistencePort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        doNothing().when(eventPublisher).publishAll(any());

        Result<CiudadanoResponseDto> result = handler.registrar(cmd);

        assertThat(result.isSuccess()).isTrue();
        CiudadanoResponseDto dto = result.getValue();
        assertThat(dto.nombres()).isNull();
        assertThat(dto.email()).isNull();
        assertThat(dto.estado()).isEqualTo("ACTIVO");
    }
}
