package pe.edu.unmsm.ciudadsana.ciudadano.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.ciudadano.application.command.AgregarFotoAlertaCommand;
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
class AgregarFotoAlertaCommandHandlerTest {

    @Mock
    AlertasPersistencePort alertasPersistencePort;

    @Mock
    CiudadanoEventPublisherPort eventPublisher;

    @InjectMocks
    AgregarFotoAlertaCommandHandler handler;

    private static final UUID ALERTA_ID = UUID.randomUUID();
    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID DISTRITO_ID = UUID.randomUUID();

    private AlertaCiudadana alerta_registrada() {
        return AlertaCiudadana.reconstitute(
                AlertaId.of(ALERTA_ID), TenantId.of(TENANT_ID), null,
                DistritoExternoId.of(DISTRITO_ID), null,
                "Residuos en calle", null,
                new Coordenadas(-12.046374, -77.042793),
                VolumenEstimado.BAJO, NivelCriticidad.NORMAL, FuenteAlerta.APP,
                EstadoAlerta.REGISTRADA, List.of(), List.of(), null,
                Instant.now(), null);
    }

    private AgregarFotoAlertaCommand comando_valido() {
        return new AgregarFotoAlertaCommand(
                ALERTA_ID, TENANT_ID,
                "https://storage.example.com/foto.jpg",
                "image/jpeg", 204800L
        );
    }

    @Test
    void agregar_foto_retorna_dto_con_foto_incluida() {
        AlertaCiudadana alerta = alerta_registrada();
        when(alertasPersistencePort.findByIdAndTenantId(any(), any())).thenReturn(Optional.of(alerta));
        when(alertasPersistencePort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        doNothing().when(eventPublisher).publishAll(any());

        Result<AlertaResponseDto> result = handler.agregarFoto(comando_valido());

        assertThat(result.isSuccess()).isTrue();
        AlertaResponseDto dto = result.getValue();
        assertThat(dto.id()).isEqualTo(ALERTA_ID);
        assertThat(dto.fotos()).hasSize(1);
        assertThat(dto.fotos().get(0).urlArchivo()).isEqualTo("https://storage.example.com/foto.jpg");
        assertThat(dto.fotos().get(0).tipoMime()).isEqualTo("image/jpeg");
        assertThat(dto.fotos().get(0).tamanioBytes()).isEqualTo(204800L);
    }

    @Test
    void agregar_foto_invoca_save_y_publish_cuando_alerta_existe() {
        when(alertasPersistencePort.findByIdAndTenantId(any(), any())).thenReturn(Optional.of(alerta_registrada()));
        when(alertasPersistencePort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        doNothing().when(eventPublisher).publishAll(any());

        handler.agregarFoto(comando_valido());

        verify(alertasPersistencePort).save(any());
        verify(eventPublisher).publishAll(any());
    }

    @Test
    void agregar_foto_retorna_error_cuando_alerta_no_existe() {
        when(alertasPersistencePort.findByIdAndTenantId(any(), any())).thenReturn(Optional.empty());

        Result<AlertaResponseDto> result = handler.agregarFoto(comando_valido());

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.ALERTA_NO_ENCONTRADA);
        verify(alertasPersistencePort, never()).save(any());
        verify(eventPublisher, never()).publishAll(any());
    }
}
