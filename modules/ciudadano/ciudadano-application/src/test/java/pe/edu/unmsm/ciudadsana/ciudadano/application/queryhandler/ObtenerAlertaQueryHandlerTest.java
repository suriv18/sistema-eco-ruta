package pe.edu.unmsm.ciudadsana.ciudadano.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.AlertaResponseDto;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.out.AlertasPersistencePort;
import pe.edu.unmsm.ciudadsana.ciudadano.application.query.ObtenerAlertaQuery;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ObtenerAlertaQueryHandlerTest {

    @Mock
    AlertasPersistencePort alertasPersistencePort;

    @InjectMocks
    ObtenerAlertaQueryHandler handler;

    private static final UUID ALERTA_ID = UUID.randomUUID();
    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID DISTRITO_ID = UUID.randomUUID();

    private AlertaCiudadana alertaRegistrada() {
        return AlertaCiudadana.reconstitute(
                AlertaId.of(ALERTA_ID), TenantId.of(TENANT_ID), null,
                DistritoExternoId.of(DISTRITO_ID), null,
                "Acumulacion de basura", "Descripcion de la alerta",
                new Coordenadas(-12.046374, -77.042793),
                VolumenEstimado.MEDIO, NivelCriticidad.CRITICA, FuenteAlerta.APP,
                EstadoAlerta.REGISTRADA, List.of(), List.of(), null,
                Instant.now(), null);
    }

    @Test
    void obtener_alertaExistente_retornaDto() {
        AlertaCiudadana alerta = alertaRegistrada();
        when(alertasPersistencePort.findByIdAndTenantId(any(), any())).thenReturn(Optional.of(alerta));

        Result<AlertaResponseDto> result = handler.obtener(new ObtenerAlertaQuery(ALERTA_ID, TENANT_ID));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().id()).isEqualTo(ALERTA_ID);
        assertThat(result.getValue().tenantId()).isEqualTo(TENANT_ID);
        assertThat(result.getValue().titulo()).isEqualTo("Acumulacion de basura");
        assertThat(result.getValue().nivelCriticidad()).isEqualTo("CRITICA");
        assertThat(result.getValue().estado()).isEqualTo("REGISTRADA");
    }

    @Test
    void obtener_alertaNoExiste_retornaNoEncontrada() {
        when(alertasPersistencePort.findByIdAndTenantId(any(), any())).thenReturn(Optional.empty());

        Result<AlertaResponseDto> result = handler.obtener(new ObtenerAlertaQuery(ALERTA_ID, TENANT_ID));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.ALERTA_NO_ENCONTRADA);
    }
}
