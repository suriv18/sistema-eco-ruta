package pe.edu.unmsm.ciudadsana.rutas.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.integracion.application.dto.RespuestaOptimizacionDto;
import pe.edu.unmsm.ciudadsana.integracion.application.port.out.OptimizationClientPort;
import pe.edu.unmsm.ciudadsana.rutas.application.command.OptimizarRutaCommand;
import pe.edu.unmsm.ciudadsana.rutas.application.dto.MetricasRutaDto;
import pe.edu.unmsm.ciudadsana.rutas.application.dto.RutaResponseDto;
import pe.edu.unmsm.ciudadsana.rutas.application.port.in.AgregarVersionRutaUseCase;
import pe.edu.unmsm.ciudadsana.rutas.application.port.out.RutasEventPublisherPort;
import pe.edu.unmsm.ciudadsana.rutas.application.port.out.RutasPersistencePort;
import pe.edu.unmsm.ciudadsana.rutas.domain.model.Ruta;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OptimizarRutaCommandHandlerTest {

    @Mock
    OptimizationClientPort optimizationPort;

    @Mock
    RutasPersistencePort rutasPersistencePort;

    @Mock
    AgregarVersionRutaUseCase agregarVersionUseCase;

    @Mock
    RutasEventPublisherPort eventPublisher;

    @InjectMocks
    OptimizarRutaCommandHandler handler;

    // Municipalidad Metropolitana de Lima (tenant)
    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    // Distrito de San Isidro - zona de alta generación de residuos sólidos
    private static final UUID DISTRITO_SAN_ISIDRO_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    // Turno matutino (05:00 - 13:00) San Isidro
    private static final UUID TURNO_MANANA_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");
    // Estación de Transferencia Lima Centro (Av. Argentina, La Victoria)
    private static final UUID DEPOSITO_ORIGEN_ID = UUID.fromString("44444444-4444-4444-4444-444444444444");
    // Relleno Sanitario Portillo Grande, Lurín
    private static final UUID DEPOSITO_DESTINO_ID = UUID.fromString("55555555-5555-5555-5555-555555555555");

    private OptimizarRutaCommand buildCommand() {
        return new OptimizarRutaCommand(
                TENANT_ID,
                TURNO_MANANA_ID,
                DISTRITO_SAN_ISIDRO_ID,
                DEPOSITO_ORIGEN_ID,
                DEPOSITO_DESTINO_ID,
                // Fecha real de operación - día hábil Lima
                LocalDate.of(2026, 6, 29),
                "OPTIMIZADA",
                List.of(),
                List.of(),
                null,
                null
        );
    }

    private RutaResponseDto rutaDtoMock() {
        return new RutaResponseDto(
                UUID.randomUUID(),
                TENANT_ID,
                TURNO_MANANA_ID,
                DISTRITO_SAN_ISIDRO_ID,
                DEPOSITO_ORIGEN_ID,
                DEPOSITO_DESTINO_ID,
                LocalDate.of(2026, 6, 29),
                "OPTIMIZADA",
                "BORRADOR",
                // ~8.5 km recorrido, 1h de duración, 2.5 toneladas - valores típicos San Isidro
                new MetricasRutaDto(8500.0, 3600, 2500.0),
                null,
                Instant.now(),
                null
        );
    }

    @Test
    void optimizar_rutaSanIsidro_conRespuestaExitosa_delegaAgregarVersion() {
        // San Isidro genera ~100 ton/día; una ruta cubre ~8.5 km con 2.5 ton
        // estado="OPTIMO", mensaje="Ruta calculada para San Isidro", resueltoEnMs=320, distancia=8500m, duracion=3600s
        RespuestaOptimizacionDto respuesta = new RespuestaOptimizacionDto("OPTIMO", "Ruta calculada para San Isidro", 320L, 8500.0, 3600, List.of());
        when(optimizationPort.optimizar(any())).thenReturn(Result.success(respuesta));

        Ruta rutaGuardada = mock(Ruta.class);
        when(rutaGuardada.pullDomainEvents()).thenReturn(List.of());
        when(rutasPersistencePort.save(any(Ruta.class))).thenReturn(rutaGuardada);

        RutaResponseDto dto = rutaDtoMock();
        when(agregarVersionUseCase.agregarVersion(any())).thenReturn(Result.success(dto));

        Result<RutaResponseDto> result = handler.optimizar(buildCommand());

        assertThat(result.isSuccess()).isTrue();
        verify(optimizationPort).optimizar(any());
        verify(rutasPersistencePort).save(any(Ruta.class));
        verify(agregarVersionUseCase).agregarVersion(any());
    }

    @Test
    void optimizar_servidorOSRM_noDisponible_retornaFailure() {
        // Simula fallo cuando el servidor OSRM (enrutamiento Lima) no responde
        when(optimizationPort.optimizar(any())).thenReturn(Result.failure(ErrorCode.SOLUCION_NO_FACTIBLE));

        Result<RutaResponseDto> result = handler.optimizar(buildCommand());

        assertThat(result.isFailure()).isTrue();
        verify(rutasPersistencePort, never()).save(any(Ruta.class));
    }
}
