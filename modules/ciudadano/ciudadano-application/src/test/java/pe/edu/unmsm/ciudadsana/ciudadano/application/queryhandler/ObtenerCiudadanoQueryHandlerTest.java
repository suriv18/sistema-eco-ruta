package pe.edu.unmsm.ciudadsana.ciudadano.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.CiudadanoResponseDto;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.out.CiudadanosPersistencePort;
import pe.edu.unmsm.ciudadsana.ciudadano.application.query.ObtenerCiudadanoQuery;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.enums.EstadoCiudadano;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.model.Ciudadano;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.CiudadanoId;
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
class ObtenerCiudadanoQueryHandlerTest {

    @Mock
    CiudadanosPersistencePort ciudadanosPersistencePort;

    @InjectMocks
    ObtenerCiudadanoQueryHandler handler;

    private static final UUID CIUDADANO_ID = UUID.randomUUID();
    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    private Ciudadano ciudadano_activo() {
        return Ciudadano.reconstitute(
                CiudadanoId.of(CIUDADANO_ID), TenantId.of(TENANT_ID),
                "Maria", "Lopez", "maria@example.com", "987654321", "87654321",
                EstadoCiudadano.ACTIVO, Instant.now());
    }

    @Test
    void obtener_ciudadano_existente_retorna_dto() {
        when(ciudadanosPersistencePort.findByIdAndTenantId(any(), any()))
                .thenReturn(Optional.of(ciudadano_activo()));

        Result<CiudadanoResponseDto> result = handler.obtener(new ObtenerCiudadanoQuery(CIUDADANO_ID, TENANT_ID));

        assertThat(result.isSuccess()).isTrue();
        CiudadanoResponseDto dto = result.getValue();
        assertThat(dto.id()).isEqualTo(CIUDADANO_ID);
        assertThat(dto.tenantId()).isEqualTo(TENANT_ID);
        assertThat(dto.nombres()).isEqualTo("Maria");
        assertThat(dto.apellidos()).isEqualTo("Lopez");
        assertThat(dto.email()).isEqualTo("maria@example.com");
        assertThat(dto.estado()).isEqualTo("ACTIVO");
    }

    @Test
    void obtener_ciudadano_no_existente_retorna_recurso_no_encontrado() {
        when(ciudadanosPersistencePort.findByIdAndTenantId(any(), any()))
                .thenReturn(Optional.empty());

        Result<CiudadanoResponseDto> result = handler.obtener(new ObtenerCiudadanoQuery(CIUDADANO_ID, TENANT_ID));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.RECURSO_NO_ENCONTRADO);
    }
}
