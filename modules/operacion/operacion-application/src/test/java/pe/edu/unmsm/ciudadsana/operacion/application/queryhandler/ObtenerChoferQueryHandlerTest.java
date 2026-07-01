package pe.edu.unmsm.ciudadsana.operacion.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.ChoferResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.ChoferesPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ObtenerChoferQuery;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoChofer;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Chofer;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ChoferId;
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
class ObtenerChoferQueryHandlerTest {

    @Mock
    private ChoferesPersistencePort choferesPersistencePort;

    @InjectMocks
    private ObtenerChoferQueryHandler handler;

    // Municipalidad Metropolitana de Lima (tenant)
    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    // Chofer de camión recolector - ruta nocturna San Isidro
    private static final UUID CHOFER_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

    @Test
    void obtener_chofer_existente_retorna_dto() {
        Instant ahora = Instant.now();

        Chofer chofer = mock(Chofer.class);
        when(chofer.getId()).thenReturn(ChoferId.of(CHOFER_ID));
        when(chofer.getTenantId()).thenReturn(TenantId.of(TENANT_ID));
        // Operador de camión compactador - ruta matutina Surquillo
        when(chofer.getNombres()).thenReturn("José Luis");
        when(chofer.getApellidos()).thenReturn("Flores Huanca");
        // DNI peruano válido (8 dígitos)
        when(chofer.getDni()).thenReturn(Optional.of("32156789"));
        // Licencia AIIb habilitada para vehículos de carga pesada (MTC Perú)
        when(chofer.getLicencia()).thenReturn(Optional.of("AIIb-2032"));
        when(chofer.getTelefono()).thenReturn(Optional.of("964123456"));
        when(chofer.getEstado()).thenReturn(EstadoChofer.ACTIVO);
        when(chofer.getCreadoEn()).thenReturn(ahora);

        when(choferesPersistencePort.findById(any(ChoferId.class), any(TenantId.class)))
                .thenReturn(Optional.of(chofer));

        ObtenerChoferQuery query = new ObtenerChoferQuery(CHOFER_ID, TENANT_ID);
        Result<ChoferResponseDto> resultado = handler.obtener(query);

        assertThat(resultado.isSuccess()).isTrue();
        ChoferResponseDto dto = resultado.getValue();
        assertThat(dto.id()).isEqualTo(CHOFER_ID);
        assertThat(dto.tenantId()).isEqualTo(TENANT_ID);
        assertThat(dto.nombres()).isEqualTo("José Luis");
        assertThat(dto.apellidos()).isEqualTo("Flores Huanca");
        assertThat(dto.dni()).isEqualTo("32156789");
        assertThat(dto.licencia()).isEqualTo("AIIb-2032");
        assertThat(dto.telefono()).isEqualTo("964123456");
        assertThat(dto.estado()).isEqualTo("ACTIVO");
        assertThat(dto.creadoEn()).isEqualTo(ahora);
    }

    @Test
    void obtener_chofer_no_encontrado_retorna_error() {
        when(choferesPersistencePort.findById(any(ChoferId.class), any(TenantId.class)))
                .thenReturn(Optional.empty());

        ObtenerChoferQuery query = new ObtenerChoferQuery(CHOFER_ID, TENANT_ID);
        Result<ChoferResponseDto> resultado = handler.obtener(query);

        assertThat(resultado.isFailure()).isTrue();
        assertThat(resultado.getError().code()).isEqualTo(ErrorCode.CHOFER_NO_ENCONTRADO);
    }
}
