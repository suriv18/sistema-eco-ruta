package pe.edu.unmsm.ciudadsana.operacion.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.DepositoResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.DepositosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ObtenerDepositoQuery;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoDeposito;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.TipoDeposito;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Deposito;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DepositoId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DistritoId;
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
class ObtenerDepositoQueryHandlerTest {

    @Mock
    private DepositosPersistencePort depositosPersistencePort;

    @InjectMocks
    private ObtenerDepositoQueryHandler handler;

    // Municipalidad Metropolitana de Lima (tenant)
    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    // Estación de Transferencia de Residuos Sólidos - Lima Sur
    private static final UUID DEPOSITO_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    // Distrito de Chorrillos (Lima Sur - donde está la estación de transferencia Lurín)
    private static final UUID DISTRITO_CHORRILLOS_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");

    @Test
    void obtener_deposito_existente_retorna_dto() {
        Instant ahora = Instant.now();

        Deposito deposito = mock(Deposito.class);
        when(deposito.getId()).thenReturn(DepositoId.of(DEPOSITO_ID));
        when(deposito.getTenantId()).thenReturn(TenantId.of(TENANT_ID));
        when(deposito.getDistritoId()).thenReturn(DistritoId.of(DISTRITO_CHORRILLOS_ID));
        // Estación de Transferencia Sur - Lima (real infrastructure in Lima)
        when(deposito.getNombre()).thenReturn("Estacion de Transferencia Lima Sur");
        when(deposito.getTipo()).thenReturn(TipoDeposito.TRANSFERENCIA);
        when(deposito.getEstado()).thenReturn(EstadoDeposito.ACTIVO);
        when(deposito.getCreadoEn()).thenReturn(ahora);

        when(depositosPersistencePort.findByIdAndTenantId(any(DepositoId.class), any(TenantId.class)))
                .thenReturn(Optional.of(deposito));

        ObtenerDepositoQuery query = new ObtenerDepositoQuery(DEPOSITO_ID, TENANT_ID);
        Result<DepositoResponseDto> resultado = handler.obtener(query);

        assertThat(resultado.isSuccess()).isTrue();
        DepositoResponseDto dto = resultado.getValue();
        assertThat(dto.id()).isEqualTo(DEPOSITO_ID);
        assertThat(dto.tenantId()).isEqualTo(TENANT_ID);
        assertThat(dto.distritoId()).isEqualTo(DISTRITO_CHORRILLOS_ID);
        assertThat(dto.nombre()).isEqualTo("Estacion de Transferencia Lima Sur");
        assertThat(dto.tipo()).isEqualTo("TRANSFERENCIA");
        assertThat(dto.estado()).isEqualTo("ACTIVO");
        assertThat(dto.creadoEn()).isEqualTo(ahora);
    }

    @Test
    void obtener_deposito_no_encontrado_retorna_error() {
        when(depositosPersistencePort.findByIdAndTenantId(any(DepositoId.class), any(TenantId.class)))
                .thenReturn(Optional.empty());

        ObtenerDepositoQuery query = new ObtenerDepositoQuery(DEPOSITO_ID, TENANT_ID);
        Result<DepositoResponseDto> resultado = handler.obtener(query);

        assertThat(resultado.isFailure()).isTrue();
        assertThat(resultado.getError().code()).isEqualTo(ErrorCode.DEPOSITO_NO_ENCONTRADO);
    }
}
