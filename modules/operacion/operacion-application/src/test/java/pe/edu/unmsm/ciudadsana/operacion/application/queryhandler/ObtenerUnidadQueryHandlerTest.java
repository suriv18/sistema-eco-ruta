package pe.edu.unmsm.ciudadsana.operacion.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.UnidadResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.UnidadesPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ObtenerUnidadQuery;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoOperativoUnidad;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.TipoUnidad;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Unidad;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.CapacidadKg;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.CapacidadM3;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.Placa;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.UnidadId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ObtenerUnidadQueryHandlerTest {

    @Mock
    private UnidadesPersistencePort unidadesPersistencePort;

    @InjectMocks
    private ObtenerUnidadQueryHandler handler;

    @Test
    void obtener_unidad_existente_retorna_dto() {
        UUID tenantUuid = UUID.randomUUID();
        UUID unidadUuid = UUID.randomUUID();
        Instant ahora = Instant.now();

        Unidad unidad = mock(Unidad.class);
        Placa placa = Placa.of("XYZ-789");
        CapacidadM3 capacidadM3 = CapacidadM3.of(new BigDecimal("10.0"));
        CapacidadKg capacidadKg = CapacidadKg.of(new BigDecimal("5000.0"));

        when(unidad.getId()).thenReturn(UnidadId.of(unidadUuid));
        when(unidad.getTenantId()).thenReturn(TenantId.of(tenantUuid));
        when(unidad.getPlaca()).thenReturn(placa);
        when(unidad.getCodigoInterno()).thenReturn("U-100");
        when(unidad.getTipo()).thenReturn(TipoUnidad.VOLQUETE);
        when(unidad.getCapacidadM3()).thenReturn(capacidadM3);
        when(unidad.getCapacidadKg()).thenReturn(capacidadKg);
        when(unidad.getEstadoOperativo()).thenReturn(EstadoOperativoUnidad.OPERATIVA);
        when(unidad.getCreadoEn()).thenReturn(ahora);

        when(unidadesPersistencePort.findById(any(UnidadId.class), any(TenantId.class)))
            .thenReturn(Optional.of(unidad));

        ObtenerUnidadQuery query = new ObtenerUnidadQuery(unidadUuid, tenantUuid);
        Result<UnidadResponseDto> resultado = handler.obtener(query);

        assertThat(resultado.isSuccess()).isTrue();
        UnidadResponseDto dto = resultado.getValue();
        assertThat(dto.id()).isEqualTo(unidadUuid);
        assertThat(dto.tenantId()).isEqualTo(tenantUuid);
        assertThat(dto.placa()).isEqualTo("XYZ-789");
        assertThat(dto.codigoInterno()).isEqualTo("U-100");
        assertThat(dto.tipoUnidad()).isEqualTo("VOLQUETE");
        assertThat(dto.capacidadM3()).isEqualByComparingTo(new BigDecimal("10.0"));
        assertThat(dto.capacidadKg()).isEqualByComparingTo(new BigDecimal("5000.0"));
        assertThat(dto.estadoOperativo()).isEqualTo("OPERATIVA");
    }

    @Test
    void obtener_unidad_no_encontrada_retorna_error_not_found() {
        UUID tenantUuid = UUID.randomUUID();
        UUID unidadUuid = UUID.randomUUID();

        when(unidadesPersistencePort.findById(any(UnidadId.class), any(TenantId.class)))
            .thenReturn(Optional.empty());

        ObtenerUnidadQuery query = new ObtenerUnidadQuery(unidadUuid, tenantUuid);
        Result<UnidadResponseDto> resultado = handler.obtener(query);

        assertThat(resultado.isFailure()).isTrue();
        assertThat(resultado.getError().code()).isEqualTo(ErrorCode.UNIDAD_NO_ENCONTRADA);
    }
}
