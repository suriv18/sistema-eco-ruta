package pe.edu.unmsm.ciudadsana.ciudadano.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.AlertaResponseDto;
import pe.edu.unmsm.ciudadsana.ciudadano.application.mapper.AlertaDtoMapper;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.out.AlertasPersistencePort;
import pe.edu.unmsm.ciudadsana.ciudadano.application.query.ListarAlertasCriticasQuery;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarAlertasCriticasQueryHandlerTest {

    @Mock
    AlertasPersistencePort alertasPersistencePort;

    @Mock
    AlertaDtoMapper alertaDtoMapper;

    @InjectMocks
    ListarAlertasCriticasQueryHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID DISTRITO_ID = UUID.randomUUID();

    private AlertaCiudadana alertaCritica() {
        return AlertaCiudadana.reconstitute(
                AlertaId.of(UUID.randomUUID()), TenantId.of(TENANT_ID), null,
                DistritoExternoId.of(DISTRITO_ID), null,
                "Incendio en vertedero", null,
                new Coordenadas(-12.05, -77.05),
                VolumenEstimado.ALTO, NivelCriticidad.CRITICA, FuenteAlerta.APP,
                EstadoAlerta.REGISTRADA, List.of(), List.of(), null,
                Instant.now(), null);
    }

    private AlertaResponseDto alertaDto(UUID id) {
        return new AlertaResponseDto(id, TENANT_ID, null, DISTRITO_ID, null,
                "Incendio en vertedero", null, -12.05, -77.05,
                "ALTO", "CRITICA", "APP", "REGISTRADA",
                List.of(), List.of(), null, Instant.now(), null);
    }

    @Test
    void listar_conAlertasCriticas_retornaPaginaConDtos() {
        AlertaCiudadana a = alertaCritica();
        AlertaResponseDto dto = alertaDto(a.getId().value());
        PageResult<AlertaCiudadana> page = PageResult.of(List.of(a), 0, 20, 1L);

        when(alertasPersistencePort.findCriticasByTenant(any(), anyInt(), anyInt())).thenReturn(page);
        when(alertaDtoMapper.toDto(any())).thenReturn(dto);

        Result<PageResult<AlertaResponseDto>> result = handler.listar(
                new ListarAlertasCriticasQuery(TENANT_ID, 0, 20));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().content()).hasSize(1);
        assertThat(result.getValue().content().get(0).nivelCriticidad()).isEqualTo("CRITICA");
        assertThat(result.getValue().totalElements()).isEqualTo(1L);
    }

    @Test
    void listar_sinAlertasCriticas_retornaPaginaVacia() {
        PageResult<AlertaCiudadana> page = PageResult.empty(0, 20);
        when(alertasPersistencePort.findCriticasByTenant(any(), anyInt(), anyInt())).thenReturn(page);

        Result<PageResult<AlertaResponseDto>> result = handler.listar(
                new ListarAlertasCriticasQuery(TENANT_ID, 0, 20));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().content()).isEmpty();
    }
}
