package pe.edu.unmsm.ciudadsana.rutas.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import pe.edu.unmsm.ciudadsana.rutas.application.command.AgregarVersionRutaCommand;
import pe.edu.unmsm.ciudadsana.rutas.application.dto.RutaResponseDto;
import pe.edu.unmsm.ciudadsana.rutas.application.port.out.RutasEventPublisherPort;
import pe.edu.unmsm.ciudadsana.rutas.application.port.out.RutasPersistencePort;
import pe.edu.unmsm.ciudadsana.rutas.domain.enums.EstadoRuta;
import pe.edu.unmsm.ciudadsana.rutas.domain.enums.TipoRuta;
import pe.edu.unmsm.ciudadsana.rutas.domain.model.Ruta;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.DepositoExternoId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.DistritoExternoId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.MetricasRuta;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.RutaId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.TurnoExternoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AgregarVersionRutaCommandHandlerTest {

    @Mock
    RutasPersistencePort rutasPersistencePort;

    @Mock
    RutasEventPublisherPort eventPublisher;

    @InjectMocks
    AgregarVersionRutaCommandHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID RUTA_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

    @Test
    void agregarVersion_rutaNoExiste_retornaRutaNoEncontrada() {
        when(rutasPersistencePort.findByIdAndTenantId(any(RutaId.class), any(TenantId.class)))
                .thenReturn(Optional.empty());

        AgregarVersionRutaCommand cmd = cmdConParadas(List.of(nuevaParada()));

        Result<RutaResponseDto> result = handler.agregarVersion(cmd);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.RUTA_NO_ENCONTRADA);
    }

    @Test
    void agregarVersion_paradasVacias_retornaRutaSinParadas() {
        Ruta ruta = rutaMock();
        when(rutasPersistencePort.findByIdAndTenantId(any(RutaId.class), any(TenantId.class)))
                .thenReturn(Optional.of(ruta));
        when(ruta.getHistorialVersiones()).thenReturn(List.of());

        AgregarVersionRutaCommand cmd = cmdConParadas(Collections.emptyList());

        Result<RutaResponseDto> result = handler.agregarVersion(cmd);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.RUTA_SIN_PARADAS);
    }

    @Test
    void agregarVersion_paradasNulas_retornaRutaSinParadas() {
        Ruta ruta = rutaMock();
        when(rutasPersistencePort.findByIdAndTenantId(any(RutaId.class), any(TenantId.class)))
                .thenReturn(Optional.of(ruta));
        when(ruta.getHistorialVersiones()).thenReturn(List.of());

        AgregarVersionRutaCommand cmd = cmdConParadas(null);

        Result<RutaResponseDto> result = handler.agregarVersion(cmd);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.RUTA_SIN_PARADAS);
    }

    @Test
    void agregarVersion_rutaExistenteConParadas_agregaVersionConExito() {
        Ruta ruta = rutaMock();
        when(rutasPersistencePort.findByIdAndTenantId(any(RutaId.class), any(TenantId.class)))
                .thenReturn(Optional.of(ruta));
        when(ruta.getHistorialVersiones()).thenReturn(List.of());
        when(ruta.getId()).thenReturn(RutaId.of(RUTA_ID));
        when(rutasPersistencePort.save(ruta)).thenReturn(ruta);
        when(ruta.pullDomainEvents()).thenReturn(List.of());

        AgregarVersionRutaCommand cmd = cmdConParadas(List.of(nuevaParada()));

        Result<RutaResponseDto> result = handler.agregarVersion(cmd);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().id()).isEqualTo(RUTA_ID);
        verify(ruta).agregarVersion(any());
        verify(rutasPersistencePort).save(ruta);
        verify(eventPublisher).publishAll(any());
    }

    private AgregarVersionRutaCommand cmdConParadas(List<AgregarVersionRutaCommand.NuevaParadaDto> paradas) {
        return new AgregarVersionRutaCommand(
                TENANT_ID,
                RUTA_ID,
                "INICIAL",
                null,
                "SISTEMA",
                1500.0,
                3600,
                200.0,
                paradas
        );
    }

    private AgregarVersionRutaCommand.NuevaParadaDto nuevaParada() {
        return new AgregarVersionRutaCommand.NuevaParadaDto(
                UUID.randomUUID(),
                UUID.randomUUID(),
                1,
                Instant.now(),
                50.0
        );
    }

    private Ruta rutaMock() {
        Ruta ruta = mock(Ruta.class);
        UUID turnoId = UUID.randomUUID();
        UUID distritoId = UUID.randomUUID();
        UUID depositoId = UUID.randomUUID();
        LocalDate fecha = LocalDate.of(2026, 6, 29);

        when(ruta.getId()).thenReturn(RutaId.of(RUTA_ID));
        when(ruta.getTenantId()).thenReturn(TenantId.of(TENANT_ID));
        when(ruta.getTurnoId()).thenReturn(TurnoExternoId.of(turnoId));
        when(ruta.getDistritoId()).thenReturn(DistritoExternoId.of(distritoId));
        when(ruta.getDepositoOrigenId()).thenReturn(DepositoExternoId.of(depositoId));
        when(ruta.getDepositoDestinoId()).thenReturn(DepositoExternoId.of(depositoId));
        when(ruta.getFecha()).thenReturn(fecha);
        when(ruta.getTipoRuta()).thenReturn(TipoRuta.HISTORICA);
        when(ruta.getEstado()).thenReturn(EstadoRuta.APROBADA);
        when(ruta.getMetricas()).thenReturn(MetricasRuta.of(0, 0, 0));
        when(ruta.getVersionActual()).thenReturn(Optional.empty());
        when(ruta.getCreadoEn()).thenReturn(Instant.now());
        when(ruta.getActualizadoEn()).thenReturn(Optional.empty());
        return ruta;
    }
}
