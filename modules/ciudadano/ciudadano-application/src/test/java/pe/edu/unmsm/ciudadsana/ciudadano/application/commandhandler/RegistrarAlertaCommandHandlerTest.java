package pe.edu.unmsm.ciudadsana.ciudadano.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.ciudadano.application.command.RegistrarAlertaCommand;
import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.AlertaResponseDto;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.out.AlertasPersistencePort;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.out.CiudadanoEventPublisherPort;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrarAlertaCommandHandlerTest {

    @Mock
    AlertasPersistencePort alertasPersistencePort;

    @Mock
    CiudadanoEventPublisherPort eventPublisher;

    @InjectMocks
    RegistrarAlertaCommandHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID DISTRITO_ID = UUID.randomUUID();
    private static final UUID ZONA_ID = UUID.randomUUID();
    private static final UUID CIUDADANO_ID = UUID.randomUUID();

    private RegistrarAlertaCommand comando_valido() {
        return new RegistrarAlertaCommand(
                TENANT_ID, CIUDADANO_ID, DISTRITO_ID, ZONA_ID,
                "Acumulacion de residuos", "Descripcion detallada",
                -12.046374, -77.042793,
                "MEDIO", "CRITICA", "APP"
        );
    }

    private RegistrarAlertaCommand comando_anonimo() {
        return new RegistrarAlertaCommand(
                TENANT_ID, null, DISTRITO_ID, null,
                "Basura en esquina", null,
                -12.05, -77.05,
                "BAJO", "NORMAL", "WEB"
        );
    }

    @Test
    void registrar_alerta_retorna_dto_con_datos_correctos() {
        when(alertasPersistencePort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        doNothing().when(eventPublisher).publishAll(any());

        Result<AlertaResponseDto> result = handler.registrar(comando_valido());

        assertThat(result.isSuccess()).isTrue();
        AlertaResponseDto dto = result.getValue();
        assertThat(dto.tenantId()).isEqualTo(TENANT_ID);
        assertThat(dto.distritoExternoId()).isEqualTo(DISTRITO_ID);
        assertThat(dto.zonaExternoId()).isEqualTo(ZONA_ID);
        assertThat(dto.titulo()).isEqualTo("Acumulacion de residuos");
        assertThat(dto.volumenEstimado()).isEqualTo("MEDIO");
        assertThat(dto.nivelCriticidad()).isEqualTo("CRITICA");
        assertThat(dto.fuente()).isEqualTo("APP");
        assertThat(dto.estado()).isEqualTo("REGISTRADA");
        assertThat(dto.id()).isNotNull();
    }

    @Test
    void registrar_alerta_invoca_save_y_publish() {
        when(alertasPersistencePort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        doNothing().when(eventPublisher).publishAll(any());

        handler.registrar(comando_valido());

        verify(alertasPersistencePort).save(any());
        verify(eventPublisher).publishAll(any());
    }

    @Test
    void registrar_alerta_anonima_sin_ciudadano_ni_zona_retorna_dto() {
        when(alertasPersistencePort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        doNothing().when(eventPublisher).publishAll(any());

        Result<AlertaResponseDto> result = handler.registrar(comando_anonimo());

        assertThat(result.isSuccess()).isTrue();
        AlertaResponseDto dto = result.getValue();
        assertThat(dto.ciudadanoId()).isNull();
        assertThat(dto.zonaExternoId()).isNull();
        assertThat(dto.descripcion()).isNull();
        assertThat(dto.fuente()).isEqualTo("WEB");
    }
}
