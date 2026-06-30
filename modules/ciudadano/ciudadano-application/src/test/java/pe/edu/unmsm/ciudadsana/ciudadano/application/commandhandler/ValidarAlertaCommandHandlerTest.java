package pe.edu.unmsm.ciudadsana.ciudadano.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.ciudadano.application.command.ValidarAlertaCommand;
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
class ValidarAlertaCommandHandlerTest {

    @Mock
    AlertasPersistencePort alertasPersistencePort;

    @Mock
    CiudadanoEventPublisherPort eventPublisher;

    @InjectMocks
    ValidarAlertaCommandHandler handler;

    private static final UUID ALERTA_ID = UUID.randomUUID();
    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID DISTRITO_ID = UUID.randomUUID();

    private AlertaCiudadana alerta_registrada() {
        return AlertaCiudadana.reconstitute(
                AlertaId.of(ALERTA_ID), TenantId.of(TENANT_ID), null,
                DistritoExternoId.of(DISTRITO_ID), null,
                "Vertimiento de aguas residuales", null,
                new Coordenadas(-12.04, -77.03),
                VolumenEstimado.ALTO, NivelCriticidad.CRITICA, FuenteAlerta.OPERADOR,
                EstadoAlerta.REGISTRADA, List.of(), List.of(), null,
                Instant.now(), null);
    }

    private ValidarAlertaCommand comando_aprobada() {
        return new ValidarAlertaCommand(ALERTA_ID, TENANT_ID, false, null, true, 0.1, "APROBADA");
    }

    @Test
    void validar_alerta_retorna_dto_con_validacion_registrada() {
        when(alertasPersistencePort.findByIdAndTenantId(any(), any()))
                .thenReturn(Optional.of(alerta_registrada()));
        when(alertasPersistencePort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        doNothing().when(eventPublisher).publishAll(any());

        Result<AlertaResponseDto> result = handler.validar(comando_aprobada());

        assertThat(result.isSuccess()).isTrue();
        AlertaResponseDto dto = result.getValue();
        assertThat(dto.id()).isEqualTo(ALERTA_ID);
        assertThat(dto.validacion()).isNotNull();
        assertThat(dto.validacion().resultado()).isEqualTo("APROBADA");
        assertThat(dto.validacion().esDuplicada()).isFalse();
        assertThat(dto.validacion().dentroGeocerca()).isTrue();
    }

    @Test
    void validar_alerta_invoca_save_y_publish() {
        when(alertasPersistencePort.findByIdAndTenantId(any(), any()))
                .thenReturn(Optional.of(alerta_registrada()));
        when(alertasPersistencePort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        doNothing().when(eventPublisher).publishAll(any());

        handler.validar(comando_aprobada());

        verify(alertasPersistencePort).save(any());
        verify(eventPublisher).publishAll(any());
    }

    @Test
    void validar_alerta_con_original_duplicada_registra_referencia() {
        UUID alertaOriginalId = UUID.randomUUID();
        ValidarAlertaCommand cmd = new ValidarAlertaCommand(
                ALERTA_ID, TENANT_ID, true, alertaOriginalId, true, 0.9, "RECHAZADA");
        when(alertasPersistencePort.findByIdAndTenantId(any(), any()))
                .thenReturn(Optional.of(alerta_registrada()));
        when(alertasPersistencePort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        doNothing().when(eventPublisher).publishAll(any());

        Result<AlertaResponseDto> result = handler.validar(cmd);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().validacion().esDuplicada()).isTrue();
        assertThat(result.getValue().validacion().alertaOriginalId()).isEqualTo(alertaOriginalId);
    }

    @Test
    void validar_alerta_retorna_error_cuando_alerta_no_existe() {
        when(alertasPersistencePort.findByIdAndTenantId(any(), any())).thenReturn(Optional.empty());

        Result<AlertaResponseDto> result = handler.validar(comando_aprobada());

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.ALERTA_NO_ENCONTRADA);
        verify(alertasPersistencePort, never()).save(any());
        verify(eventPublisher, never()).publishAll(any());
    }
}
