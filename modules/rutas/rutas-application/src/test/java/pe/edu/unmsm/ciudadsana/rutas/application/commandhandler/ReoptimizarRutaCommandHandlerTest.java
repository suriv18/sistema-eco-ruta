package pe.edu.unmsm.ciudadsana.rutas.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.integracion.application.dto.RespuestaOptimizacionDto;
import pe.edu.unmsm.ciudadsana.integracion.application.port.out.OptimizationClientPort;
import pe.edu.unmsm.ciudadsana.rutas.application.command.ReoptimizarRutaCommand;
import pe.edu.unmsm.ciudadsana.rutas.application.dto.MetricasRutaDto;
import pe.edu.unmsm.ciudadsana.rutas.application.dto.RutaResponseDto;
import pe.edu.unmsm.ciudadsana.rutas.application.port.in.AgregarVersionRutaUseCase;
import pe.edu.unmsm.ciudadsana.rutas.application.port.out.RutasPersistencePort;
import pe.edu.unmsm.ciudadsana.rutas.domain.model.Ruta;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.DistritoExternoId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.RutaId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReoptimizarRutaCommandHandlerTest {

    @Mock
    OptimizationClientPort optimizationPort;

    @Mock
    RutasPersistencePort rutasPersistencePort;

    @Mock
    AgregarVersionRutaUseCase agregarVersionUseCase;

    @InjectMocks
    ReoptimizarRutaCommandHandler handler;

    // Municipalidad Metropolitana de Lima (tenant)
    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    // Ruta existente - turno nocturno Barranco/Surquillo
    private static final UUID RUTA_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    // Distrito de Barranco - zona costera sur de Lima
    private static final UUID DISTRITO_BARRANCO_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");

    private ReoptimizarRutaCommand buildCommand() {
        return new ReoptimizarRutaCommand(
                TENANT_ID,
                RUTA_ID,
                List.of(),
                List.of(),
                null,
                null,
                // Motivo real: alerta crítica reportada por ciudadano en Barranco
                "ALERTA_CRITICA_ZONA"
        );
    }

    private Ruta rutaMock() {
        Ruta ruta = mock(Ruta.class);
        when(ruta.getDistritoId()).thenReturn(DistritoExternoId.of(DISTRITO_BARRANCO_ID));
        // Fecha real de operación - lunes hábil Lima
        when(ruta.getFecha()).thenReturn(LocalDate.of(2026, 6, 29));
        return ruta;
    }

    private RutaResponseDto rutaDtoMock() {
        return new RutaResponseDto(
                RUTA_ID,
                TENANT_ID,
                UUID.randomUUID(),
                DISTRITO_BARRANCO_ID,
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDate.of(2026, 6, 29),
                "OPTIMIZADA",
                "BORRADOR",
                // Barranco: ~6.5 km, 50min, 1.8 ton - distrito compacto y denso
                new MetricasRutaDto(6500.0, 3000, 1800.0),
                null,
                Instant.now(),
                null
        );
    }

    @Test
    void reoptimizar_rutaExistente_delegaAgregarVersion() {
        Ruta ruta = rutaMock();
        when(rutasPersistencePort.findByIdAndTenantId(any(RutaId.class), any(TenantId.class)))
                .thenReturn(Optional.of(ruta));

        // Recálculo por alerta crítica en Barranco - ruta reoptimizada
        RespuestaOptimizacionDto respuesta = new RespuestaOptimizacionDto("OPTIMO", "Ruta reoptimizada Barranco", 250L, 6500.0, 3000, List.of());
        when(optimizationPort.optimizar(any())).thenReturn(Result.success(respuesta));

        RutaResponseDto dto = rutaDtoMock();
        when(agregarVersionUseCase.agregarVersion(any())).thenReturn(Result.success(dto));

        Result<RutaResponseDto> result = handler.reoptimizar(buildCommand());

        assertThat(result.isSuccess()).isTrue();
        verify(agregarVersionUseCase).agregarVersion(any());
    }

    @Test
    void reoptimizar_rutaNoEncontrada_retornaFailure() {
        when(rutasPersistencePort.findByIdAndTenantId(any(RutaId.class), any(TenantId.class)))
                .thenReturn(Optional.empty());

        Result<RutaResponseDto> result = handler.reoptimizar(buildCommand());

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.RUTA_NO_ENCONTRADA);
        verify(optimizationPort, never()).optimizar(any());
    }

    @Test
    void reoptimizar_optimizadorFalla_retornaFailure() {
        Ruta ruta = rutaMock();
        when(rutasPersistencePort.findByIdAndTenantId(any(RutaId.class), any(TenantId.class)))
                .thenReturn(Optional.of(ruta));

        when(optimizationPort.optimizar(any())).thenReturn(Result.failure(ErrorCode.SOLUCION_NO_FACTIBLE));

        Result<RutaResponseDto> result = handler.reoptimizar(buildCommand());

        assertThat(result.isFailure()).isTrue();
        verify(agregarVersionUseCase, never()).agregarVersion(any());
    }
}
