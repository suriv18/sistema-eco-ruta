package pe.edu.unmsm.ciudadsana.telemetria.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
import pe.edu.unmsm.ciudadsana.telemetria.application.command.RegistrarDesvioRutaCommand;
import pe.edu.unmsm.ciudadsana.telemetria.application.dto.DesvioRutaResponseDto;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.DesvioRutaPersistencePort;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.DesvioRutaPersistencePort.DesvioView;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrarDesvioRutaCommandHandlerTest {

    @Mock
    DesvioRutaPersistencePort desvioRutaPersistencePort;

    @InjectMocks
    RegistrarDesvioRutaCommandHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID UNIDAD_ID = UUID.fromString("66666666-6666-6666-6666-666666666666");
    private static final UUID RUTA_ID = UUID.fromString("77777777-7777-7777-7777-777777777777");
    private static final UUID DESVIO_ID = UUID.fromString("88888888-8888-8888-8888-888888888888");

    @Test
    void registrar_datosValidos_retornaDto() {
        DesvioView savedView = new DesvioView(
                DESVIO_ID, TENANT_ID, UNIDAD_ID, RUTA_ID,
                -12.046374, -77.042793, 300.0,
                "ALTA", "ABIERTO", Instant.now(), null
        );
        when(desvioRutaPersistencePort.save(any())).thenReturn(savedView);

        RegistrarDesvioRutaCommand cmd = new RegistrarDesvioRutaCommand(
                TENANT_ID, UNIDAD_ID, RUTA_ID, -12.046374, -77.042793, 300.0, "ALTA"
        );

        Result<DesvioRutaResponseDto> result = handler.registrar(cmd);

        assertThat(result.isSuccess()).isTrue();
        DesvioRutaResponseDto dto = result.getValue();
        assertThat(dto.id()).isEqualTo(DESVIO_ID);
        assertThat(dto.tenantId()).isEqualTo(TENANT_ID);
        assertThat(dto.unidadExternoId()).isEqualTo(UNIDAD_ID);
        assertThat(dto.rutaExternoId()).isEqualTo(RUTA_ID);
        assertThat(dto.severidad()).isEqualTo("ALTA");
        assertThat(dto.estado()).isEqualTo("ABIERTO");
        assertThat(dto.distanciaDesvioM()).isEqualTo(300.0);
    }
}
