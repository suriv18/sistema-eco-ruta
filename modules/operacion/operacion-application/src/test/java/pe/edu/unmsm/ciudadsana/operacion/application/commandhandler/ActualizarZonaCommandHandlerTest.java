package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.operacion.application.command.ActualizarZonaCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.ZonaResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.ZonasPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoZona;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.TipoZona;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Zona;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.CodigoZona;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DistritoId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.PrioridadBase;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ZonaId;
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
class ActualizarZonaCommandHandlerTest {

    @Mock
    private ZonasPersistencePort zonasPersistencePort;

    @InjectMocks
    private ActualizarZonaCommandHandler handler;

    // Municipalidad Metropolitana de Lima (tenant)
    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    // Zona de recolección ZM-03 - Miraflores Centro
    private static final UUID ZONA_MIRAFLORES_CENTRO_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    // Distrito de Miraflores (lat: -12.1179, lon: -77.0330)
    private static final UUID DISTRITO_MIRAFLORES_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");

    @Test
    void actualizar_zona_mirafloresCentro_retorna_dto_con_prioridad_actualizada() {
        Instant ahora = Instant.now();

        Zona zona = mock(Zona.class);
        when(zona.getId()).thenReturn(ZonaId.of(ZONA_MIRAFLORES_CENTRO_ID));
        when(zona.getTenantId()).thenReturn(TenantId.of(TENANT_ID));
        when(zona.getDistritoId()).thenReturn(DistritoId.of(DISTRITO_MIRAFLORES_ID));
        // Código de zona estilo SIRER - Zona Miraflores 03
        when(zona.getCodigo()).thenReturn(CodigoZona.of("ZM-03"));
        when(zona.getNombre()).thenReturn("Miraflores Centro");
        // Zona residencial de alta densidad - Miraflores
        when(zona.getTipo()).thenReturn(TipoZona.RESIDENCIAL);
        // Prioridad 3 - alta (genera >50 ton/día según INEI)
        when(zona.getPrioridad()).thenReturn(PrioridadBase.of(3));
        when(zona.getEstado()).thenReturn(EstadoZona.ACTIVA);
        when(zona.getCreadoEn()).thenReturn(ahora);

        when(zonasPersistencePort.findById(any(ZonaId.class), any(TenantId.class)))
                .thenReturn(Optional.of(zona));
        when(zonasPersistencePort.save(zona)).thenReturn(zona);

        ActualizarZonaCommand cmd = new ActualizarZonaCommand(TENANT_ID, ZONA_MIRAFLORES_CENTRO_ID, 3);
        Result<ZonaResponseDto> resultado = handler.actualizar(cmd);

        assertThat(resultado.isSuccess()).isTrue();
        ZonaResponseDto dto = resultado.getValue();
        assertThat(dto.id()).isEqualTo(ZONA_MIRAFLORES_CENTRO_ID);
        assertThat(dto.tenantId()).isEqualTo(TENANT_ID);
        assertThat(dto.distritoId()).isEqualTo(DISTRITO_MIRAFLORES_ID);
        assertThat(dto.codigo()).isEqualTo("ZM-03");
        assertThat(dto.nombre()).isEqualTo("Miraflores Centro");
        assertThat(dto.tipoZona()).isEqualTo("RESIDENCIAL");
        assertThat(dto.prioridad()).isEqualTo(3);
        assertThat(dto.estado()).isEqualTo("ACTIVA");
    }

    @Test
    void actualizar_zona_no_encontrada_retorna_failure() {
        when(zonasPersistencePort.findById(any(ZonaId.class), any(TenantId.class)))
                .thenReturn(Optional.empty());

        ActualizarZonaCommand cmd = new ActualizarZonaCommand(TENANT_ID, ZONA_MIRAFLORES_CENTRO_ID, 3);
        Result<ZonaResponseDto> resultado = handler.actualizar(cmd);

        assertThat(resultado.isFailure()).isTrue();
        assertThat(resultado.getError().code()).isEqualTo(ErrorCode.ZONA_NO_ENCONTRADA);
    }
}
