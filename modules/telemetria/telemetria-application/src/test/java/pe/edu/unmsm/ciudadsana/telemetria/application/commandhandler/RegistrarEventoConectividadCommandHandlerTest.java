package pe.edu.unmsm.ciudadsana.telemetria.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
import pe.edu.unmsm.ciudadsana.telemetria.application.command.RegistrarEventoConectividadCommand;
import pe.edu.unmsm.ciudadsana.telemetria.application.dto.EventoConectividadResponseDto;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.EventoConectividadPersistencePort;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.EventoConectividadPersistencePort.EventoConectividadView;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrarEventoConectividadCommandHandlerTest {

    @Mock
    EventoConectividadPersistencePort eventoConectividadPersistencePort;

    @InjectMocks
    RegistrarEventoConectividadCommandHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID UNIDAD_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final UUID DISPOSITIVO_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");
    private static final UUID EVENTO_ID = UUID.fromString("44444444-4444-4444-4444-444444444444");

    @Test
    void registrar_datosValidos_retornaDto() {
        EventoConectividadView savedView = new EventoConectividadView(
                EVENTO_ID, TENANT_ID, UNIDAD_ID, DISPOSITIVO_ID,
                "SIN_SENAL", "Perdida de señal GPS", Instant.now()
        );
        when(eventoConectividadPersistencePort.save(any())).thenReturn(savedView);

        RegistrarEventoConectividadCommand cmd = new RegistrarEventoConectividadCommand(
                TENANT_ID, UNIDAD_ID, DISPOSITIVO_ID, "SIN_SENAL", "Perdida de señal GPS"
        );

        Result<EventoConectividadResponseDto> result = handler.registrar(cmd);

        assertThat(result.isSuccess()).isTrue();
        EventoConectividadResponseDto dto = result.getValue();
        assertThat(dto.id()).isEqualTo(EVENTO_ID);
        assertThat(dto.tenantId()).isEqualTo(TENANT_ID);
        assertThat(dto.unidadExternoId()).isEqualTo(UNIDAD_ID);
        assertThat(dto.dispositivoId()).isEqualTo(DISPOSITIVO_ID);
        assertThat(dto.tipoEvento()).isEqualTo("SIN_SENAL");
        assertThat(dto.detalle()).isEqualTo("Perdida de señal GPS");
        assertThat(dto.detectadoEn()).isNotNull();
    }
}
