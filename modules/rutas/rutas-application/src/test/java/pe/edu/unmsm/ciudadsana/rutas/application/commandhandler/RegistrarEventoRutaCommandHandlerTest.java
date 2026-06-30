package pe.edu.unmsm.ciudadsana.rutas.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.rutas.application.command.RegistrarEventoRutaCommand;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrarEventoRutaCommandHandlerTest {

    @Mock
    RutasPersistencePort rutasPersistencePort;

    @Mock
    RutasEventPublisherPort eventPublisher;

    @InjectMocks
    RegistrarEventoRutaCommandHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID RUTA_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

    @Test
    void registrar_rutaNoExiste_retornaRutaNoEncontrada() {
        when(rutasPersistencePort.findByIdAndTenantId(any(RutaId.class), any(TenantId.class)))
                .thenReturn(Optional.empty());

        RegistrarEventoRutaCommand cmd = new RegistrarEventoRutaCommand(
                RUTA_ID, TENANT_ID, "OBSERVACION", "Sin novedad", null
        );

        Result<RutaResponseDto> result = handler.registrar(cmd);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.RUTA_NO_ENCONTRADA);
    }

    @Test
    void registrar_rutaExistente_registraEventoYGuarda() {
        Ruta ruta = rutaMock();
        when(rutasPersistencePort.findByIdAndTenantId(any(RutaId.class), any(TenantId.class)))
                .thenReturn(Optional.of(ruta));
        when(rutasPersistencePort.save(ruta)).thenReturn(ruta);
        when(ruta.pullDomainEvents()).thenReturn(List.of());

        RegistrarEventoRutaCommand cmd = new RegistrarEventoRutaCommand(
                RUTA_ID, TENANT_ID, "AVERIA", "Falla mecánica", "{\"detalles\":\"motor\"}"
        );

        Result<RutaResponseDto> result = handler.registrar(cmd);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().id()).isEqualTo(RUTA_ID);
        verify(ruta).registrarEvento(any());
        verify(rutasPersistencePort).save(ruta);
        verify(eventPublisher).publishAll(any());
    }

    @Test
    void registrar_rutaExistente_retornaDtoConTenantCorrecto() {
        Ruta ruta = rutaMock();
        when(rutasPersistencePort.findByIdAndTenantId(any(RutaId.class), any(TenantId.class)))
                .thenReturn(Optional.of(ruta));
        when(rutasPersistencePort.save(ruta)).thenReturn(ruta);
        when(ruta.pullDomainEvents()).thenReturn(List.of());

        RegistrarEventoRutaCommand cmd = new RegistrarEventoRutaCommand(
                RUTA_ID, TENANT_ID, "INICIO", "Inicio de ruta", null
        );

        Result<RutaResponseDto> result = handler.registrar(cmd);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().tenantId()).isEqualTo(TENANT_ID);
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
        when(ruta.getEstado()).thenReturn(EstadoRuta.EN_EJECUCION);
        when(ruta.getMetricas()).thenReturn(MetricasRuta.of(0, 0, 0));
        when(ruta.getVersionActual()).thenReturn(Optional.empty());
        when(ruta.getCreadoEn()).thenReturn(Instant.now());
        when(ruta.getActualizadoEn()).thenReturn(Optional.empty());
        return ruta;
    }
}
