package pe.edu.unmsm.ciudadsana.operacion.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.ZonaResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.ZonasPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ObtenerZonaQuery;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoZona;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.TipoZona;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Zona;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.CodigoZona;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DistritoId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.PrioridadBase;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ZonaId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ObtenerZonaQueryHandlerTest {

    @Mock
    private ZonasPersistencePort zonasPersistencePort;

    @InjectMocks
    private ObtenerZonaQueryHandler handler;

    @Test
    void obtener_zona_existente_retorna_dto() {
        UUID tenantUuid = UUID.randomUUID();
        UUID zonaUuid = UUID.randomUUID();
        UUID distritoUuid = UUID.randomUUID();
        Instant ahora = Instant.now();

        Zona zona = mock(Zona.class);
        CodigoZona codigo = CodigoZona.of("ZS-02");
        PrioridadBase prioridad = PrioridadBase.of(2);

        when(zona.getId()).thenReturn(ZonaId.of(zonaUuid));
        when(zona.getTenantId()).thenReturn(TenantId.of(tenantUuid));
        when(zona.getDistritoId()).thenReturn(DistritoId.of(distritoUuid));
        when(zona.getCodigo()).thenReturn(codigo);
        when(zona.getNombre()).thenReturn("Zona Sur");
        when(zona.getTipo()).thenReturn(TipoZona.COMERCIAL);
        when(zona.getPrioridad()).thenReturn(prioridad);
        when(zona.getEstado()).thenReturn(EstadoZona.ACTIVA);
        when(zona.getCreadoEn()).thenReturn(ahora);

        when(zonasPersistencePort.findById(any(ZonaId.class), any(TenantId.class)))
            .thenReturn(Optional.of(zona));

        ObtenerZonaQuery query = new ObtenerZonaQuery(zonaUuid, tenantUuid);
        Result<ZonaResponseDto> resultado = handler.obtener(query);

        assertThat(resultado.isSuccess()).isTrue();
        ZonaResponseDto dto = resultado.getValue();
        assertThat(dto.id()).isEqualTo(zonaUuid);
        assertThat(dto.tenantId()).isEqualTo(tenantUuid);
        assertThat(dto.distritoId()).isEqualTo(distritoUuid);
        assertThat(dto.codigo()).isEqualTo("ZS-02");
        assertThat(dto.nombre()).isEqualTo("Zona Sur");
        assertThat(dto.tipoZona()).isEqualTo("COMERCIAL");
        assertThat(dto.prioridad()).isEqualTo(2);
        assertThat(dto.estado()).isEqualTo("ACTIVA");
    }

    @Test
    void obtener_zona_no_encontrada_retorna_error_not_found() {
        UUID tenantUuid = UUID.randomUUID();
        UUID zonaUuid = UUID.randomUUID();

        when(zonasPersistencePort.findById(any(ZonaId.class), any(TenantId.class)))
            .thenReturn(Optional.empty());

        ObtenerZonaQuery query = new ObtenerZonaQuery(zonaUuid, tenantUuid);
        Result<ZonaResponseDto> resultado = handler.obtener(query);

        assertThat(resultado.isFailure()).isTrue();
        assertThat(resultado.getError().code()).isEqualTo(ErrorCode.ZONA_NO_ENCONTRADA);
    }
}
