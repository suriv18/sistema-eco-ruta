package pe.edu.unmsm.ciudadsana.operacion.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.ChoferResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.ChoferesPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ListarChoferesQuery;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoChofer;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Chofer;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ChoferId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarChoferesQueryHandlerTest {

    @Mock
    private ChoferesPersistencePort choferesPersistencePort;

    @InjectMocks
    private ListarChoferesQueryHandler handler;

    @Test
    void listar_con_elementos_retorna_page_result_con_dtos() {
        UUID tenantUuid = UUID.randomUUID();
        UUID choferUuid = UUID.randomUUID();
        Instant ahora = Instant.now();

        Chofer chofer = mock(Chofer.class);
        ChoferId choferId = ChoferId.of(choferUuid);
        TenantId tenantId = TenantId.of(tenantUuid);

        when(chofer.getId()).thenReturn(choferId);
        when(chofer.getTenantId()).thenReturn(tenantId);
        when(chofer.getNombres()).thenReturn("Juan");
        when(chofer.getApellidos()).thenReturn("Perez");
        when(chofer.getDni()).thenReturn(Optional.of("12345678"));
        when(chofer.getLicencia()).thenReturn(Optional.of("A-I"));
        when(chofer.getTelefono()).thenReturn(Optional.of("999000111"));
        when(chofer.getEstado()).thenReturn(EstadoChofer.ACTIVO);
        when(chofer.getCreadoEn()).thenReturn(ahora);

        PageResult<Chofer> pageResult = PageResult.of(List.of(chofer), 0, 10, 1L);
        when(choferesPersistencePort.findAll(any(TenantId.class), eq(0), eq(10))).thenReturn(pageResult);

        ListarChoferesQuery query = new ListarChoferesQuery(tenantUuid, 0, 10);
        Result<PageResult<ChoferResponseDto>> resultado = handler.listar(query);

        assertThat(resultado.isSuccess()).isTrue();
        PageResult<ChoferResponseDto> page = resultado.getValue();
        assertThat(page.content()).hasSize(1);
        assertThat(page.totalElements()).isEqualTo(1L);
        ChoferResponseDto dto = page.content().getFirst();
        assertThat(dto.id()).isEqualTo(choferUuid);
        assertThat(dto.tenantId()).isEqualTo(tenantUuid);
        assertThat(dto.nombres()).isEqualTo("Juan");
        assertThat(dto.apellidos()).isEqualTo("Perez");
        assertThat(dto.dni()).isEqualTo("12345678");
        assertThat(dto.estado()).isEqualTo("ACTIVO");
    }

    @Test
    void listar_sin_elementos_retorna_page_result_vacio() {
        UUID tenantUuid = UUID.randomUUID();
        PageResult<Chofer> pageVacio = PageResult.empty(0, 10);
        when(choferesPersistencePort.findAll(any(TenantId.class), eq(0), eq(10))).thenReturn(pageVacio);

        ListarChoferesQuery query = new ListarChoferesQuery(tenantUuid, 0, 10);
        Result<PageResult<ChoferResponseDto>> resultado = handler.listar(query);

        assertThat(resultado.isSuccess()).isTrue();
        PageResult<ChoferResponseDto> page = resultado.getValue();
        assertThat(page.content()).isEmpty();
        assertThat(page.totalElements()).isZero();
    }
}
