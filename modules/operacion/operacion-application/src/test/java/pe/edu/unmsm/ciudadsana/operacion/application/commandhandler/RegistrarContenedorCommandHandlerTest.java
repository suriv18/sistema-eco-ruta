package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.operacion.application.command.RegistrarContenedorCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.ContenedorResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.ContenedoresPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.OperacionEventPublisherPort;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.ZonasPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoContenedor;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Contenedor;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.CapacidadM3;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ContenedorId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ZonaId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrarContenedorCommandHandlerTest {

    @Mock
    private ZonasPersistencePort zonasPersistencePort;

    @Mock
    private ContenedoresPersistencePort contenedoresPersistencePort;

    @Mock
    private OperacionEventPublisherPort eventPublisher;

    @InjectMocks
    private RegistrarContenedorCommandHandler handler;

    @Test
    void registrar_contenedor_valido_retorna_success() {
        UUID tenantId = UUID.randomUUID();
        UUID zonaId = UUID.randomUUID();
        RegistrarContenedorCommand command = new RegistrarContenedorCommand(
            tenantId, zonaId, "CTN-001", BigDecimal.valueOf(2.5)
        );
        pe.edu.unmsm.ciudadsana.operacion.domain.model.Zona zonaMock =
            mock(pe.edu.unmsm.ciudadsana.operacion.domain.model.Zona.class);
        Contenedor contenedorGuardado = Contenedor.reconstitute(
            ContenedorId.of(UUID.randomUUID()),
            TenantId.of(tenantId),
            ZonaId.of(zonaId),
            "CTN-001",
            CapacidadM3.of(BigDecimal.valueOf(2.5)),
            EstadoContenedor.VACIO,
            Instant.now()
        );
        when(zonasPersistencePort.findById(any(ZonaId.class), any(TenantId.class)))
            .thenReturn(Optional.of(zonaMock));
        when(contenedoresPersistencePort.existsByCodigo(eq("CTN-001"), any(TenantId.class))).thenReturn(false);
        when(contenedoresPersistencePort.save(any(Contenedor.class))).thenReturn(contenedorGuardado);
        doNothing().when(eventPublisher).publishAll(any(List.class));

        Result<ContenedorResponseDto> result = handler.registrar(command);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().codigo()).isEqualTo("CTN-001");
        assertThat(result.getValue().estadoContenedor()).isEqualTo("VACIO");
    }

    @Test
    void registrar_contenedor_zona_no_encontrada_retorna_zona_no_encontrada() {
        UUID tenantId = UUID.randomUUID();
        UUID zonaId = UUID.randomUUID();
        RegistrarContenedorCommand command = new RegistrarContenedorCommand(
            tenantId, zonaId, "CTN-002", BigDecimal.valueOf(1.0)
        );
        when(zonasPersistencePort.findById(any(ZonaId.class), any(TenantId.class)))
            .thenReturn(Optional.empty());

        Result<ContenedorResponseDto> result = handler.registrar(command);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.ZONA_NO_ENCONTRADA);
    }

    @Test
    void registrar_contenedor_codigo_duplicado_retorna_validacion_error() {
        UUID tenantId = UUID.randomUUID();
        UUID zonaId = UUID.randomUUID();
        RegistrarContenedorCommand command = new RegistrarContenedorCommand(
            tenantId, zonaId, "CTN-001", BigDecimal.valueOf(2.0)
        );
        pe.edu.unmsm.ciudadsana.operacion.domain.model.Zona zonaMock =
            mock(pe.edu.unmsm.ciudadsana.operacion.domain.model.Zona.class);
        when(zonasPersistencePort.findById(any(ZonaId.class), any(TenantId.class)))
            .thenReturn(Optional.of(zonaMock));
        when(contenedoresPersistencePort.existsByCodigo(eq("CTN-001"), any(TenantId.class))).thenReturn(true);

        Result<ContenedorResponseDto> result = handler.registrar(command);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.VALIDACION_ERROR);
    }
}
