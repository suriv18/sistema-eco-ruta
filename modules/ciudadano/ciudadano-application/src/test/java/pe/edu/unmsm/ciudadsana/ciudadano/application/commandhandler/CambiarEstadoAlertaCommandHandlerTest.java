package pe.edu.unmsm.ciudadsana.ciudadano.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.ciudadano.application.command.CambiarEstadoAlertaCommand;
import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.AlertaResponseDto;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.out.AlertasPersistencePort;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.out.CiudadanoEventPublisherPort;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.enums.EstadoAlerta;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.enums.FuenteAlerta;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.enums.NivelCriticidad;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.enums.VolumenEstimado;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.model.AlertaCiudadana;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.AlertaId;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.Coordenadas;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.DistritoExternoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CambiarEstadoAlertaCommandHandlerTest {

    @Mock
    AlertasPersistencePort alertasPersistencePort;

    @Mock
    CiudadanoEventPublisherPort eventPublisher;

    @InjectMocks
    CambiarEstadoAlertaCommandHandler handler;

    private static final UUID ALERTA_ID = UUID.randomUUID();
    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID DISTRITO_ID = UUID.randomUUID();
    private static final UUID USUARIO_ID = UUID.randomUUID();

    private AlertaCiudadana alerta_en_estado(EstadoAlerta estado) {
        return AlertaCiudadana.reconstitute(
                AlertaId.of(ALERTA_ID), TenantId.of(TENANT_ID), null,
                DistritoExternoId.of(DISTRITO_ID), null,
                "Contaminacion de rio", "Rio contaminado con residuos industriales",
                new Coordenadas(-12.1, -77.0),
                VolumenEstimado.ALTO, NivelCriticidad.CRITICA, FuenteAlerta.OEFA,
                estado, List.of(), List.of(), null,
                Instant.now(), null);
    }

    @Test
    void cambiar_estado_de_registrada_a_validada_retorna_dto_actualizado() {
        when(alertasPersistencePort.findByIdAndTenantId(any(), any()))
                .thenReturn(Optional.of(alerta_en_estado(EstadoAlerta.REGISTRADA)));
        when(alertasPersistencePort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        doNothing().when(eventPublisher).publishAll(any());

        CambiarEstadoAlertaCommand cmd = new CambiarEstadoAlertaCommand(
                ALERTA_ID, TENANT_ID, "VALIDADA", "Alerta validada correctamente", USUARIO_ID);

        Result<AlertaResponseDto> result = handler.cambiarEstado(cmd);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().estado()).isEqualTo("VALIDADA");
        assertThat(result.getValue().id()).isEqualTo(ALERTA_ID);
    }

    @Test
    void cambiar_estado_invoca_save_y_publish_en_transicion_valida() {
        when(alertasPersistencePort.findByIdAndTenantId(any(), any()))
                .thenReturn(Optional.of(alerta_en_estado(EstadoAlerta.REGISTRADA)));
        when(alertasPersistencePort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        doNothing().when(eventPublisher).publishAll(any());

        CambiarEstadoAlertaCommand cmd = new CambiarEstadoAlertaCommand(
                ALERTA_ID, TENANT_ID, "DESCARTADA", "Alerta duplicada", USUARIO_ID);

        handler.cambiarEstado(cmd);

        verify(alertasPersistencePort).save(any());
        verify(eventPublisher).publishAll(any());
    }

    @Test
    void cambiar_estado_retorna_error_cuando_alerta_no_existe() {
        when(alertasPersistencePort.findByIdAndTenantId(any(), any())).thenReturn(Optional.empty());

        CambiarEstadoAlertaCommand cmd = new CambiarEstadoAlertaCommand(
                ALERTA_ID, TENANT_ID, "VALIDADA", null, USUARIO_ID);

        Result<AlertaResponseDto> result = handler.cambiarEstado(cmd);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.ALERTA_NO_ENCONTRADA);
        verify(alertasPersistencePort, never()).save(any());
    }

    @Test
    void cambiar_estado_retorna_error_cuando_transicion_no_es_valida() {
        when(alertasPersistencePort.findByIdAndTenantId(any(), any()))
                .thenReturn(Optional.of(alerta_en_estado(EstadoAlerta.ATENDIDA)));

        CambiarEstadoAlertaCommand cmd = new CambiarEstadoAlertaCommand(
                ALERTA_ID, TENANT_ID, "REGISTRADA", null, USUARIO_ID);

        Result<AlertaResponseDto> result = handler.cambiarEstado(cmd);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.TRANSICION_ESTADO_INVALIDA);
        verify(alertasPersistencePort, never()).save(any());
        verify(eventPublisher, never()).publishAll(any());
    }
}
