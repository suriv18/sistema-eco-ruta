package pe.edu.unmsm.ciudadsana.ciudadano.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.AlertaResponseDto;
import pe.edu.unmsm.ciudadsana.ciudadano.application.mapper.AlertaDtoMapper;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.out.AlertasPersistencePort;
import pe.edu.unmsm.ciudadsana.ciudadano.application.query.ListarAlertasPorZonaQuery;
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
class ListarAlertasPorZonaQueryHandlerTest {

    @Mock
    AlertasPersistencePort alertasPersistencePort;

    @Mock
    AlertaDtoMapper alertaDtoMapper;

    @InjectMocks
    ListarAlertasPorZonaQueryHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID ZONA_ID = UUID.randomUUID();
    private static final UUID DISTRITO_ID = UUID.randomUUID();

    private AlertaCiudadana alerta() {
        return AlertaCiudadana.reconstitute(
                AlertaId.of(UUID.randomUUID()), TenantId.of(TENANT_ID), null,
                DistritoExternoId.of(DISTRITO_ID), null,
                "Basura en zona norte", null,
                new Coordenadas(-12.0, -77.0),
                VolumenEstimado.MEDIO, NivelCriticidad.NORMAL, FuenteAlerta.APP,
                EstadoAlerta.REGISTRADA, List.of(), List.of(), null,
                Instant.now(), null);
    }

    private AlertaResponseDto alertaDto(UUID id) {
        return new AlertaResponseDto(id, TENANT_ID, null, DISTRITO_ID, ZONA_ID,
                "Basura en zona norte", null, -12.0, -77.0,
                "MEDIO", "NORMAL", "APP", "REGISTRADA",
                List.of(), List.of(), null, Instant.now(), null);
    }

    @Test
    void listar_por_zona_con_alertas_retorna_pagina_con_dtos() {
        AlertaCiudadana a = alerta();
        AlertaResponseDto dto = alertaDto(a.getId().value());
        PageResult<AlertaCiudadana> page = PageResult.of(List.of(a), 0, 20, 1L);
        when(alertasPersistencePort.findAllByZonaAndTenantId(any(), any(), anyInt(), anyInt())).thenReturn(page);
        when(alertaDtoMapper.toDto(any())).thenReturn(dto);

        Result<PageResult<AlertaResponseDto>> result = handler.listarPorZona(
                new ListarAlertasPorZonaQuery(ZONA_ID, TENANT_ID, 0, 20));

        assertThat(result.isSuccess()).isTrue();
        PageResult<AlertaResponseDto> pagina = result.getValue();
        assertThat(pagina.content()).hasSize(1);
        assertThat(pagina.totalElements()).isEqualTo(1L);
        assertThat(pagina.content().get(0).titulo()).isEqualTo("Basura en zona norte");
    }

    @Test
    void listar_por_zona_sin_alertas_retorna_pagina_vacia() {
        PageResult<AlertaCiudadana> page = PageResult.empty(0, 20);
        when(alertasPersistencePort.findAllByZonaAndTenantId(any(), any(), anyInt(), anyInt())).thenReturn(page);

        Result<PageResult<AlertaResponseDto>> result = handler.listarPorZona(
                new ListarAlertasPorZonaQuery(ZONA_ID, TENANT_ID, 0, 20));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().content()).isEmpty();
        assertThat(result.getValue().totalElements()).isZero();
    }

    @Test
    void listar_por_zona_delega_mapeo_al_mapper() {
        AlertaCiudadana a = alerta();
        AlertaResponseDto dto = alertaDto(a.getId().value());
        PageResult<AlertaCiudadana> page = PageResult.of(List.of(a), 0, 10, 1L);
        when(alertasPersistencePort.findAllByZonaAndTenantId(any(), any(), anyInt(), anyInt())).thenReturn(page);
        when(alertaDtoMapper.toDto(a)).thenReturn(dto);

        Result<PageResult<AlertaResponseDto>> result = handler.listarPorZona(
                new ListarAlertasPorZonaQuery(ZONA_ID, TENANT_ID, 0, 10));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().content().get(0)).isSameAs(dto);
    }
}
