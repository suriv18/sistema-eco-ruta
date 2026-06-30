package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.operacion.application.command.RegistrarUnidadCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.UnidadResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.OperacionEventPublisherPort;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.UnidadesPersistencePort;
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
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrarUnidadCommandHandlerTest {

    @Mock
    private UnidadesPersistencePort unidadesPersistencePort;

    @Mock
    private OperacionEventPublisherPort eventPublisher;

    @InjectMocks
    private RegistrarUnidadCommandHandler handler;

    @Test
    void registrar_unidad_valida_retorna_success() {
        UUID tenantId = UUID.randomUUID();
        RegistrarUnidadCommand command = new RegistrarUnidadCommand(
            tenantId, "ABC-123", "UC-001", "COMPACTADOR",
            BigDecimal.valueOf(8.0), BigDecimal.valueOf(5000.0)
        );
        Unidad unidadGuardada = Unidad.reconstitute(
            UnidadId.of(UUID.randomUUID()),
            TenantId.of(tenantId),
            Placa.of("ABC-123"),
            "UC-001",
            TipoUnidad.COMPACTADOR,
            CapacidadM3.of(BigDecimal.valueOf(8.0)),
            CapacidadKg.of(BigDecimal.valueOf(5000.0)),
            EstadoOperativoUnidad.OPERATIVA,
            Instant.now()
        );
        when(unidadesPersistencePort.existsByPlaca(any(Placa.class), any(TenantId.class))).thenReturn(false);
        when(unidadesPersistencePort.save(any(Unidad.class))).thenReturn(unidadGuardada);
        doNothing().when(eventPublisher).publishAll(any(List.class));

        Result<UnidadResponseDto> result = handler.registrar(command);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().placa()).isEqualTo("ABC-123");
        assertThat(result.getValue().tipoUnidad()).isEqualTo("COMPACTADOR");
        assertThat(result.getValue().estadoOperativo()).isEqualTo("OPERATIVA");
    }

    @Test
    void registrar_unidad_placa_duplicada_retorna_validacion_error() {
        UUID tenantId = UUID.randomUUID();
        RegistrarUnidadCommand command = new RegistrarUnidadCommand(
            tenantId, "ABC-123", "UC-002", "BARANDA",
            BigDecimal.valueOf(6.0), BigDecimal.valueOf(4000.0)
        );
        when(unidadesPersistencePort.existsByPlaca(any(Placa.class), any(TenantId.class))).thenReturn(true);

        Result<UnidadResponseDto> result = handler.registrar(command);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.VALIDACION_ERROR);
    }

    @Test
    void registrar_unidad_placa_formato_invalido_retorna_validacion_error() {
        UUID tenantId = UUID.randomUUID();
        RegistrarUnidadCommand command = new RegistrarUnidadCommand(
            tenantId, "PLACA-INVALIDA", "UC-003", "VOLQUETE",
            BigDecimal.valueOf(10.0), BigDecimal.valueOf(8000.0)
        );

        Result<UnidadResponseDto> result = handler.registrar(command);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.VALIDACION_ERROR);
    }

    @Test
    void registrar_unidad_tipo_invalido_retorna_validacion_error() {
        UUID tenantId = UUID.randomUUID();
        RegistrarUnidadCommand command = new RegistrarUnidadCommand(
            tenantId, "DEF-456", "UC-004", "TIPO_INEXISTENTE",
            BigDecimal.valueOf(5.0), BigDecimal.valueOf(3000.0)
        );
        when(unidadesPersistencePort.existsByPlaca(any(Placa.class), any(TenantId.class))).thenReturn(false);

        Result<UnidadResponseDto> result = handler.registrar(command);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.VALIDACION_ERROR);
    }
}
