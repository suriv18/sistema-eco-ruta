package pe.edu.unmsm.ciudadsana.ciudadano.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.CiudadanoResponseDto;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.out.CiudadanosPersistencePort;
import pe.edu.unmsm.ciudadsana.ciudadano.application.query.ListarCiudadanosQuery;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.enums.EstadoCiudadano;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.model.Ciudadano;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.CiudadanoId;
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
class ListarCiudadanosQueryHandlerTest {

    @Mock
    CiudadanosPersistencePort ciudadanosPersistencePort;

    @InjectMocks
    ListarCiudadanosQueryHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    private Ciudadano ciudadano(String nombres) {
        return Ciudadano.reconstitute(
                CiudadanoId.of(UUID.randomUUID()), TenantId.of(TENANT_ID),
                nombres, "Apellido", "email@example.com", "999000111", "11223344",
                EstadoCiudadano.ACTIVO, Instant.now());
    }

    @Test
    void listar_con_ciudadanos_retorna_pagina_con_dtos() {
        Ciudadano c1 = ciudadano("Ana");
        Ciudadano c2 = ciudadano("Carlos");
        PageResult<Ciudadano> page = PageResult.of(List.of(c1, c2), 0, 20, 2L);
        when(ciudadanosPersistencePort.findAllByTenantId(any(), anyInt(), anyInt())).thenReturn(page);

        Result<PageResult<CiudadanoResponseDto>> result = handler.listar(new ListarCiudadanosQuery(TENANT_ID, 0, 20));

        assertThat(result.isSuccess()).isTrue();
        PageResult<CiudadanoResponseDto> pagina = result.getValue();
        assertThat(pagina.content()).hasSize(2);
        assertThat(pagina.totalElements()).isEqualTo(2L);
        assertThat(pagina.content().get(0).nombres()).isEqualTo("Ana");
        assertThat(pagina.content().get(1).nombres()).isEqualTo("Carlos");
    }

    @Test
    void listar_sin_ciudadanos_retorna_pagina_vacia() {
        PageResult<Ciudadano> page = PageResult.empty(0, 20);
        when(ciudadanosPersistencePort.findAllByTenantId(any(), anyInt(), anyInt())).thenReturn(page);

        Result<PageResult<CiudadanoResponseDto>> result = handler.listar(new ListarCiudadanosQuery(TENANT_ID, 0, 20));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().content()).isEmpty();
        assertThat(result.getValue().totalElements()).isZero();
    }

    @Test
    void listar_mapea_estado_ciudadano_correctamente() {
        Ciudadano bloqueado = Ciudadano.reconstitute(
                CiudadanoId.of(UUID.randomUUID()), TenantId.of(TENANT_ID),
                "Pedro", "Rojas", null, null, null,
                EstadoCiudadano.BLOQUEADO, Instant.now());
        PageResult<Ciudadano> page = PageResult.of(List.of(bloqueado), 0, 20, 1L);
        when(ciudadanosPersistencePort.findAllByTenantId(any(), anyInt(), anyInt())).thenReturn(page);

        Result<PageResult<CiudadanoResponseDto>> result = handler.listar(new ListarCiudadanosQuery(TENANT_ID, 0, 20));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().content().get(0).estado()).isEqualTo("BLOQUEADO");
    }
}
