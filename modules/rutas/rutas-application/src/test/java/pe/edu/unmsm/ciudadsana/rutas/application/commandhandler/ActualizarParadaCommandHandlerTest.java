package pe.edu.unmsm.ciudadsana.rutas.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import pe.edu.unmsm.ciudadsana.rutas.application.command.ActualizarParadaCommand;
import pe.edu.unmsm.ciudadsana.rutas.application.dto.RutaResponseDto;
import pe.edu.unmsm.ciudadsana.rutas.application.port.out.RutasEventPublisherPort;
import pe.edu.unmsm.ciudadsana.rutas.application.port.out.RutasPersistencePort;
import pe.edu.unmsm.ciudadsana.rutas.domain.enums.EstadoParada;
import pe.edu.unmsm.ciudadsana.rutas.domain.enums.EstadoRuta;
import pe.edu.unmsm.ciudadsana.rutas.domain.enums.GeneradoPor;
import pe.edu.unmsm.ciudadsana.rutas.domain.enums.MotivoVersion;
import pe.edu.unmsm.ciudadsana.rutas.domain.enums.TipoRuta;
import pe.edu.unmsm.ciudadsana.rutas.domain.model.Ruta;
import pe.edu.unmsm.ciudadsana.rutas.domain.model.RutaParada;
import pe.edu.unmsm.ciudadsana.rutas.domain.model.RutaVersion;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.DepositoExternoId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.DistritoExternoId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.MetricasRuta;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.ParadaId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.RutaId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.RutaVersionId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.TurnoExternoId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.ZonaExternoId;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ActualizarParadaCommandHandlerTest {

    @Mock
    RutasPersistencePort rutasPersistencePort;

    @Mock
    RutasEventPublisherPort eventPublisher;

    @InjectMocks
    ActualizarParadaCommandHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID RUTA_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final UUID PARADA_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");
    private static final UUID VERSION_ID = UUID.fromString("44444444-4444-4444-4444-444444444444");

    @Test
    void actualizar_rutaNoExiste_retornaRutaNoEncontrada() {
        when(rutasPersistencePort.findByIdAndTenantId(any(RutaId.class), any(TenantId.class)))
                .thenReturn(Optional.empty());

        ActualizarParadaCommand cmd = new ActualizarParadaCommand(RUTA_ID, PARADA_ID, TENANT_ID, "EN_ATENCION", null, null);

        Result<RutaResponseDto> result = handler.actualizar(cmd);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.RUTA_NO_ENCONTRADA);
    }

    @Test
    void actualizar_sinVersionActual_retornaRutaEstadoInvalido() {
        Ruta ruta = rutaMockSinVersion();
        when(rutasPersistencePort.findByIdAndTenantId(any(RutaId.class), any(TenantId.class)))
                .thenReturn(Optional.of(ruta));

        ActualizarParadaCommand cmd = new ActualizarParadaCommand(RUTA_ID, PARADA_ID, TENANT_ID, "EN_ATENCION", null, null);

        Result<RutaResponseDto> result = handler.actualizar(cmd);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.RUTA_ESTADO_INVALIDO);
    }

    @Test
    void actualizar_paradaNoEncontradaEnVersion_retornaRecursoNoEncontrado() {
        RutaVersion version = versionMock(List.of());
        Ruta ruta = rutaMockConVersion(version);
        when(rutasPersistencePort.findByIdAndTenantId(any(RutaId.class), any(TenantId.class)))
                .thenReturn(Optional.of(ruta));

        ActualizarParadaCommand cmd = new ActualizarParadaCommand(RUTA_ID, PARADA_ID, TENANT_ID, "EN_ATENCION", null, null);

        Result<RutaResponseDto> result = handler.actualizar(cmd);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.RECURSO_NO_ENCONTRADO);
    }

    @Test
    void actualizar_estadoEnAtencion_llama_iniciarAtencion() {
        RutaParada parada = paradaMock(PARADA_ID);
        RutaVersion version = versionMock(List.of(parada));
        Ruta ruta = rutaMockConVersion(version);
        when(rutasPersistencePort.findByIdAndTenantId(any(RutaId.class), any(TenantId.class)))
                .thenReturn(Optional.of(ruta));
        when(rutasPersistencePort.save(ruta)).thenReturn(ruta);
        when(ruta.pullDomainEvents()).thenReturn(List.of());

        ActualizarParadaCommand cmd = new ActualizarParadaCommand(RUTA_ID, PARADA_ID, TENANT_ID, "EN_ATENCION", null, null);

        Result<RutaResponseDto> result = handler.actualizar(cmd);

        assertThat(result.isSuccess()).isTrue();
        verify(parada).iniciarAtencion();
        verify(rutasPersistencePort).save(ruta);
        verify(eventPublisher).publishAll(any());
    }

    @Test
    void actualizar_estadoAtendida_llama_marcarAtendida() {
        Instant llegada = Instant.parse("2026-06-29T08:00:00Z");
        Instant salida = Instant.parse("2026-06-29T08:30:00Z");
        RutaParada parada = paradaMock(PARADA_ID);
        RutaVersion version = versionMock(List.of(parada));
        Ruta ruta = rutaMockConVersion(version);
        when(rutasPersistencePort.findByIdAndTenantId(any(RutaId.class), any(TenantId.class)))
                .thenReturn(Optional.of(ruta));
        when(rutasPersistencePort.save(ruta)).thenReturn(ruta);
        when(ruta.pullDomainEvents()).thenReturn(List.of());

        ActualizarParadaCommand cmd = new ActualizarParadaCommand(RUTA_ID, PARADA_ID, TENANT_ID, "ATENDIDA", llegada, salida);

        Result<RutaResponseDto> result = handler.actualizar(cmd);

        assertThat(result.isSuccess()).isTrue();
        verify(parada).marcarAtendida(llegada, salida);
    }

    @Test
    void actualizar_estadoOmitida_llama_marcarOmitida() {
        RutaParada parada = paradaMock(PARADA_ID);
        RutaVersion version = versionMock(List.of(parada));
        Ruta ruta = rutaMockConVersion(version);
        when(rutasPersistencePort.findByIdAndTenantId(any(RutaId.class), any(TenantId.class)))
                .thenReturn(Optional.of(ruta));
        when(rutasPersistencePort.save(ruta)).thenReturn(ruta);
        when(ruta.pullDomainEvents()).thenReturn(List.of());

        ActualizarParadaCommand cmd = new ActualizarParadaCommand(RUTA_ID, PARADA_ID, TENANT_ID, "OMITIDA", null, null);

        Result<RutaResponseDto> result = handler.actualizar(cmd);

        assertThat(result.isSuccess()).isTrue();
        verify(parada).marcarOmitida();
    }

    @Test
    void actualizar_transicionInvalida_retornaTransicionEstadoInvalida() {
        RutaParada parada = paradaMock(PARADA_ID);
        RutaVersion version = versionMock(List.of(parada));
        Ruta ruta = rutaMockConVersion(version);
        when(rutasPersistencePort.findByIdAndTenantId(any(RutaId.class), any(TenantId.class)))
                .thenReturn(Optional.of(ruta));
        doThrow(new IllegalStateException("Transicion invalida")).when(parada).iniciarAtencion();

        ActualizarParadaCommand cmd = new ActualizarParadaCommand(RUTA_ID, PARADA_ID, TENANT_ID, "EN_ATENCION", null, null);

        Result<RutaResponseDto> result = handler.actualizar(cmd);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.TRANSICION_ESTADO_INVALIDA);
    }

    private RutaParada paradaMock(UUID paradaId) {
        RutaParada parada = mock(RutaParada.class);
        when(parada.getId()).thenReturn(ParadaId.of(paradaId));
        when(parada.getRutaVersionId()).thenReturn(RutaVersionId.of(VERSION_ID));
        when(parada.getZonaId()).thenReturn(ZonaExternoId.of(UUID.randomUUID()));
        when(parada.getContenedorId()).thenReturn(Optional.empty());
        when(parada.getOrden()).thenReturn(1);
        when(parada.getEta()).thenReturn(Optional.empty());
        when(parada.getHoraLlegadaReal()).thenReturn(Optional.empty());
        when(parada.getHoraSalidaReal()).thenReturn(Optional.empty());
        when(parada.getDemandaEstimadaKg()).thenReturn(50.0);
        when(parada.getCargaAcumuladaKg()).thenReturn(0.0);
        when(parada.getEstado()).thenReturn(EstadoParada.PENDIENTE);
        when(parada.getCreadoEn()).thenReturn(Instant.now());
        return parada;
    }

    private RutaVersion versionMock(List<RutaParada> paradas) {
        RutaVersion version = mock(RutaVersion.class);
        when(version.getId()).thenReturn(RutaVersionId.of(VERSION_ID));
        when(version.getRutaId()).thenReturn(RutaId.of(RUTA_ID));
        when(version.getVersion()).thenReturn(1);
        when(version.getMotivo()).thenReturn(MotivoVersion.INICIAL);
        when(version.getAlertaIdExterno()).thenReturn(Optional.empty());
        when(version.getGeneradoPor()).thenReturn(GeneradoPor.SISTEMA);
        when(version.getMetricas()).thenReturn(MetricasRuta.of(0, 0, 0));
        when(version.getParadas()).thenReturn(paradas);
        when(version.getCreadoEn()).thenReturn(Instant.now());
        return version;
    }

    private Ruta rutaMockSinVersion() {
        Ruta ruta = baseMock();
        when(ruta.getVersionActual()).thenReturn(Optional.empty());
        return ruta;
    }

    private Ruta rutaMockConVersion(RutaVersion version) {
        Ruta ruta = baseMock();
        when(ruta.getVersionActual()).thenReturn(Optional.of(version));
        return ruta;
    }

    private Ruta baseMock() {
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
        when(ruta.getEstado()).thenReturn(EstadoRuta.EN_EJECUCION);
        when(ruta.getMetricas()).thenReturn(MetricasRuta.of(0, 0, 0));
        when(ruta.getCreadoEn()).thenReturn(Instant.now());
        when(ruta.getActualizadoEn()).thenReturn(Optional.empty());
        return ruta;
    }
}