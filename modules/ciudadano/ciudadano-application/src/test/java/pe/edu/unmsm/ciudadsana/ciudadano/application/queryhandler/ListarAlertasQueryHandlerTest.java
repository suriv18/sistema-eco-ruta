package pe.edu.unmsm.ciudadsana.ciudadano.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.AlertaResponseDto;
import pe.edu.unmsm.ciudadsana.ciudadano.application.mapper.AlertaDtoMapper;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.out.AlertasPersistencePort;
import pe.edu.unmsm.ciudadsana.ciudadano.application.query.ListarAlertasQuery;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.enums.EstadoAlerta;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.enums.FuenteAlerta;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.enums.NivelCriticidad;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.enums.VolumenEstimado;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.model.AlertaCiudadana;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.AlertaId;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.Coordenadas;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.DistritoExternoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarAlertasQueryHandlerTest {

    @Mock
    AlertasPersistencePort alertasPersistencePort;

    @Mock
    AlertaDtoMapper alertaDtoMapper;

    @InjectMocks
    ListarAlertasQueryHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID DISTRITO_ID = UUID.randomUUID();

    private AlertaCiudadana alerta() {
        return AlertaCiudadana.reconstitute(
                AlertaId.of(UUID.randomUUID()), TenantId.of(TENANT_ID), null,
                DistritoExternoId.of(DISTRITO_ID), null,
                "Residuos en via publica", null,
                new Coordenadas(-12.1, -77.1),
                VolumenEstimado.BAJO, NivelCriticidad.NORMAL, FuenteAlerta.APP,
                EstadoAlerta.REGISTRADA, List.of(), List.of(), null,
                Instant.now(), null);
    }

    private AlertaResponseDto alertaDto(UUID id) {
        return new AlertaResponseDto(id, TENANT_ID, null, DISTRITO_ID, null,
                "Residuos en via publica", null, -12.1, -77.1,
                "BAJO", "NORMAL", "APP", "REGISTRADA",
                List.of(), List.of(), null, Instant.now(), null);
    }

    @Test
    void listar_conAlertas_retornaPaginaConDtos() {
        AlertaCiudadana a = alerta();
        AlertaResponseDto dto = alertaDto(a.getId().value());
        PageResult<AlertaCiudadana> page = PageResult.of(List.of(a), 0, 20, 1L);

        when(alertasPersistencePort.findAllByTenantId(any(), isNull(), anyInt(), anyInt())).thenReturn(page);
        when(alertaDtoMapper.toDto(any())).thenReturn(dto);

        Result<PageResult<AlertaResponseDto>> result = handler.listar(
                new ListarAlertasQuery(TENANT_ID, null, 0, 20));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().content()).hasSize(1);
        assertThat(result.getValue().totalElements()).isEqualTo(1L);
    }

    @Test
    void listar_sinAlertas_retornaPaginaVacia() {
        PageResult<AlertaCiudadana> page = PageResult.empty(0, 20);
        when(alertasPersistencePort.findAllByTenantId(any(), isNull(), anyInt(), anyInt())).thenReturn(page);

        Result<PageResult<AlertaResponseDto>> result = handler.listar(
                new ListarAlertasQuery(TENANT_ID, null, 0, 20));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().content()).isEmpty();
    }
}
