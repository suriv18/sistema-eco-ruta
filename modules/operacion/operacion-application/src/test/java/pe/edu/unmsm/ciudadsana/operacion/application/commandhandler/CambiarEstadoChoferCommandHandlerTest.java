package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.operacion.application.command.CambiarEstadoChoferCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.ChoferResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.ChoferesPersistencePort;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CambiarEstadoChoferCommandHandlerTest {

    @Mock
    private ChoferesPersistencePort choferesPersistencePort;

    @InjectMocks
    private CambiarEstadoChoferCommandHandler handler;

    // Municipalidad Metropolitana de Lima (tenant)
    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    // Chofer de camión recolector - operador EMILIMA Miraflores
    private static final UUID CHOFER_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

    @Test
    void cambiarEstado_aActivo_retornaDtoConEstadoActivo() {
        Instant ahora = Instant.now();

        Chofer chofer = mock(Chofer.class);
        when(chofer.getId()).thenReturn(ChoferId.of(CHOFER_ID));
        when(chofer.getTenantId()).thenReturn(TenantId.of(TENANT_ID));
        // Nombre real peruano - chofer de camión recolector en Lima
        when(chofer.getNombres()).thenReturn("Carlos Alberto");
        when(chofer.getApellidos()).thenReturn("Quispe Mamani");
        // DNI peruano válido (8 dígitos)
        when(chofer.getDni()).thenReturn(Optional.of("45678901"));
        // Licencia de conducir AIIb - habilitada para camiones de recolección > 3.5t
        when(chofer.getLicencia()).thenReturn(Optional.of("AIIb-2031"));
        when(chofer.getTelefono()).thenReturn(Optional.of("987654321"));
        when(chofer.getEstado()).thenReturn(EstadoChofer.ACTIVO);
        when(chofer.getCreadoEn()).thenReturn(ahora);

        when(choferesPersistencePort.findById(any(ChoferId.class), any(TenantId.class)))
                .thenReturn(Optional.of(chofer));
        when(choferesPersistencePort.save(chofer)).thenReturn(chofer);

        CambiarEstadoChoferCommand cmd = new CambiarEstadoChoferCommand(CHOFER_ID, TENANT_ID, EstadoChofer.ACTIVO);
        Result<ChoferResponseDto> resultado = handler.cambiarEstado(cmd);

        assertThat(resultado.isSuccess()).isTrue();
        ChoferResponseDto dto = resultado.getValue();
        assertThat(dto.id()).isEqualTo(CHOFER_ID);
        assertThat(dto.tenantId()).isEqualTo(TENANT_ID);
        assertThat(dto.nombres()).isEqualTo("Carlos Alberto");
        assertThat(dto.apellidos()).isEqualTo("Quispe Mamani");
        assertThat(dto.dni()).isEqualTo("45678901");
        assertThat(dto.estado()).isEqualTo("ACTIVO");
        verify(chofer).activar();
    }

    @Test
    void cambiarEstado_choferNoEncontrado_retornaFailure() {
        when(choferesPersistencePort.findById(any(ChoferId.class), any(TenantId.class)))
                .thenReturn(Optional.empty());

        CambiarEstadoChoferCommand cmd = new CambiarEstadoChoferCommand(CHOFER_ID, TENANT_ID, EstadoChofer.ACTIVO);
        Result<ChoferResponseDto> resultado = handler.cambiarEstado(cmd);

        assertThat(resultado.isFailure()).isTrue();
        assertThat(resultado.getError().code()).isEqualTo(ErrorCode.CHOFER_NO_ENCONTRADO);
    }

    @Test
    void cambiarEstado_transicionInvalida_retornaOperacionNoPermitida() {
        Chofer chofer = mock(Chofer.class);
        when(choferesPersistencePort.findById(any(ChoferId.class), any(TenantId.class)))
                .thenReturn(Optional.of(chofer));
        doThrow(new IllegalStateException("Transicion no permitida"))
                .when(chofer).suspender();

        CambiarEstadoChoferCommand cmd = new CambiarEstadoChoferCommand(CHOFER_ID, TENANT_ID, EstadoChofer.SUSPENDIDO);
        Result<ChoferResponseDto> resultado = handler.cambiarEstado(cmd);

        assertThat(resultado.isFailure()).isTrue();
        assertThat(resultado.getError().code()).isEqualTo(ErrorCode.OPERACION_NO_PERMITIDA);
    }
}
