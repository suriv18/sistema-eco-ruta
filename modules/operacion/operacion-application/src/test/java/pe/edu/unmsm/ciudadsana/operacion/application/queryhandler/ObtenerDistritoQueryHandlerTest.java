package pe.edu.unmsm.ciudadsana.operacion.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.DistritoResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.DistritosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ObtenerDistritoQuery;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoDistrito;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Distrito;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DistritoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ObtenerDistritoQueryHandlerTest {

    @Mock
    private DistritosPersistencePort distritosPersistencePort;

    @InjectMocks
    private ObtenerDistritoQueryHandler handler;

    @Test
    void obtener_distrito_existente_retorna_dto() {
        UUID tenantUuid = UUID.randomUUID();
        UUID distritoUuid = UUID.randomUUID();
        Instant ahora = Instant.now();

        Distrito distrito = Distrito.reconstitute(
            DistritoId.of(distritoUuid),
            TenantId.of(tenantUuid),
            "San Isidro",
            "150130",
            EstadoDistrito.ACTIVO,
            ahora
        );

        when(distritosPersistencePort.findById(any(DistritoId.class), any(TenantId.class)))
            .thenReturn(Optional.of(distrito));

        ObtenerDistritoQuery query = new ObtenerDistritoQuery(distritoUuid, tenantUuid);
        Result<DistritoResponseDto> resultado = handler.obtener(query);

        assertThat(resultado.isSuccess()).isTrue();
        DistritoResponseDto dto = resultado.getValue();
        assertThat(dto.id()).isEqualTo(distritoUuid);
        assertThat(dto.tenantId()).isEqualTo(tenantUuid);
        assertThat(dto.nombre()).isEqualTo("San Isidro");
        assertThat(dto.ubigeo()).isEqualTo("150130");
        assertThat(dto.estado()).isEqualTo("ACTIVO");
    }

    @Test
    void obtener_distrito_no_encontrado_retorna_error_not_found() {
        UUID tenantUuid = UUID.randomUUID();
        UUID distritoUuid = UUID.randomUUID();

        when(distritosPersistencePort.findById(any(DistritoId.class), any(TenantId.class)))
            .thenReturn(Optional.empty());

        ObtenerDistritoQuery query = new ObtenerDistritoQuery(distritoUuid, tenantUuid);
        Result<DistritoResponseDto> resultado = handler.obtener(query);

        assertThat(resultado.isFailure()).isTrue();
        assertThat(resultado.getError().code()).isEqualTo(ErrorCode.DISTRITO_NO_ENCONTRADO);
    }
}
