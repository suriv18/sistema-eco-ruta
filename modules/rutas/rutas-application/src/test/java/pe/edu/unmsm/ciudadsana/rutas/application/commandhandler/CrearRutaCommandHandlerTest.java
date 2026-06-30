package pe.edu.unmsm.ciudadsana.rutas.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.rutas.application.command.CrearRutaCommand;
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
class CrearRutaCommandHandlerTest {

    @Mock
    RutasPersistencePort rutasPersistencePort;

    @Mock
    RutasEventPublisherPort eventPublisher;

    @InjectMocks
    CrearRutaCommandHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID RUTA_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

    @Test
    void crear_commandValido_retornaDtoConExito() {
        Ruta rutaGuardada = rutaMock(RUTA_ID, TENANT_ID);
        when(rutasPersistencePort.save(any(Ruta.class))).thenReturn(rutaGuardada);
        when(rutaGuardada.pullDomainEvents()).thenReturn(List.of());

        CrearRutaCommand cmd = new CrearRutaCommand(
                TENANT_ID,
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDate.of(2026, 6, 29),
                "HISTORICA"
        );

        Result<RutaResponseDto> result = handler.crear(cmd);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().id()).isEqualTo(RUTA_ID);
        assertThat(result.getValue().tenantId()).isEqualTo(TENANT_ID);
        verify(rutasPersistencePort).save(any(Ruta.class));
        verify(eventPublisher).publishAll(any());
    }

    @Test
    void crear_commandValido_estadoEsBorrador() {
        Ruta rutaGuardada = rutaMock(RUTA_ID, TENANT_ID);
        when(rutasPersistencePort.save(any(Ruta.class))).thenReturn(rutaGuardada);
        when(rutaGuardada.pullDomainEvents()).thenReturn(List.of());

        CrearRutaCommand cmd = new CrearRutaCommand(
                TENANT_ID,
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDate.of(2026, 6, 29),
                "OPTIMIZADA"
        );

        Result<RutaResponseDto> result = handler.crear(cmd);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().estado()).isEqualTo("BORRADOR");
    }

    private Ruta rutaMock(UUID rutaId, UUID tenantId) {
        Ruta ruta = mock(Ruta.class);
        UUID turnoId = UUID.randomUUID();
        UUID distritoId = UUID.randomUUID();
        UUID depositoId = UUID.randomUUID();
        LocalDate fecha = LocalDate.of(2026, 6, 29);

        when(ruta.getId()).thenReturn(RutaId.of(rutaId));
        when(ruta.getTenantId()).thenReturn(TenantId.of(tenantId));
        when(ruta.getTurnoId()).thenReturn(TurnoExternoId.of(turnoId));
        when(ruta.getDistritoId()).thenReturn(DistritoExternoId.of(distritoId));
        when(ruta.getDepositoOrigenId()).thenReturn(DepositoExternoId.of(depositoId));
        when(ruta.getDepositoDestinoId()).thenReturn(DepositoExternoId.of(depositoId));
        when(ruta.getFecha()).thenReturn(fecha);
        when(ruta.getTipoRuta()).thenReturn(TipoRuta.HISTORICA);
        when(ruta.getEstado()).thenReturn(EstadoRuta.BORRADOR);
        when(ruta.getMetricas()).thenReturn(MetricasRuta.of(0, 0, 0));
        when(ruta.getVersionActual()).thenReturn(Optional.empty());
        when(ruta.getCreadoEn()).thenReturn(Instant.now());
        when(ruta.getActualizadoEn()).thenReturn(Optional.empty());
        return ruta;
    }
}
