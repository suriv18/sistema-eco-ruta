package pe.edu.unmsm.ciudadsana.operacion.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.ContenedorResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.ContenedoresPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ObtenerContenedorQuery;
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
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ObtenerContenedorQueryHandlerTest {

    @Mock
    private ContenedoresPersistencePort contenedoresPersistencePort;

    @InjectMocks
    private ObtenerContenedorQueryHandler handler;

    // Municipalidad Metropolitana de Lima (tenant)
    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    // Contenedor tipo iglú (1100L) en punto ecológico - Av. Benavides, Surquillo
    private static final UUID CONTENEDOR_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    // Zona de recolección ZSQ-01 - Surquillo
    private static final UUID ZONA_SURQUILLO_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");

    @Test
    void obtener_contenedor_igluSurquillo_existente_retorna_dto() {
        Instant ahora = Instant.now();

        Contenedor contenedor = mock(Contenedor.class);
        when(contenedor.getId()).thenReturn(ContenedorId.of(CONTENEDOR_ID));
        when(contenedor.getTenantId()).thenReturn(TenantId.of(TENANT_ID));
        when(contenedor.getZonaId()).thenReturn(ZonaId.of(ZONA_SURQUILLO_ID));
        // Código de contenedor tipo iglú instalado en Surquillo
        when(contenedor.getCodigo()).thenReturn("SQ-IGLU-014");
        // 1.1 m³ - capacidad estándar de contenedor iglú en Lima (Municipalidad Miraflores/Surquillo)
        when(contenedor.getCapacidad()).thenReturn(CapacidadM3.of(new BigDecimal("1.1")));
        when(contenedor.getEstadoContenedor()).thenReturn(EstadoContenedor.VACIO);
        when(contenedor.getCreadoEn()).thenReturn(ahora);

        when(contenedoresPersistencePort.findById(any(ContenedorId.class), any(TenantId.class)))
                .thenReturn(Optional.of(contenedor));

        ObtenerContenedorQuery query = new ObtenerContenedorQuery(CONTENEDOR_ID, TENANT_ID);
        Result<ContenedorResponseDto> resultado = handler.obtener(query);

        assertThat(resultado.isSuccess()).isTrue();
        ContenedorResponseDto dto = resultado.getValue();
        assertThat(dto.id()).isEqualTo(CONTENEDOR_ID);
        assertThat(dto.tenantId()).isEqualTo(TENANT_ID);
        assertThat(dto.zonaId()).isEqualTo(ZONA_SURQUILLO_ID);
        assertThat(dto.codigo()).isEqualTo("SQ-IGLU-014");
        assertThat(dto.capacidadM3()).isEqualByComparingTo(new BigDecimal("1.1"));
        assertThat(dto.estadoContenedor()).isEqualTo("VACIO");
        assertThat(dto.creadoEn()).isEqualTo(ahora);
    }

    @Test
    void obtener_contenedor_no_encontrado_retorna_error() {
        when(contenedoresPersistencePort.findById(any(ContenedorId.class), any(TenantId.class)))
                .thenReturn(Optional.empty());

        ObtenerContenedorQuery query = new ObtenerContenedorQuery(CONTENEDOR_ID, TENANT_ID);
        Result<ContenedorResponseDto> resultado = handler.obtener(query);

        assertThat(resultado.isFailure()).isTrue();
        assertThat(resultado.getError().code()).isEqualTo(ErrorCode.CONTENEDOR_NO_ENCONTRADO);
    }
}
