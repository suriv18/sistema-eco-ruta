package pe.edu.unmsm.ciudadsana.telemetria.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
import pe.edu.unmsm.ciudadsana.telemetria.application.dto.EventoConectividadResponseDto;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.EventoConectividadPersistencePort;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.EventoConectividadPersistencePort.EventoConectividadView;
import pe.edu.unmsm.ciudadsana.telemetria.application.query.ListarEventosConectividadQuery;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarEventosConectividadQueryHandlerTest {

    @Mock
    EventoConectividadPersistencePort eventoConectividadPersistencePort;

    @InjectMocks
    ListarEventosConectividadQueryHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID UNIDAD_ID = UUID.randomUUID();

    private EventoConectividadView eventoView() {
        return new EventoConectividadView(UUID.randomUUID(), TENANT_ID, UNIDAD_ID,
                UUID.randomUUID(), "SIN_SENAL", "Sin cobertura GPS", Instant.now());
    }

    @Test
    void listar_conEventos_retornaPaginaConDtos() {
        PageResult<EventoConectividadView> page = PageResult.of(List.of(eventoView()), 0, 20, 1L);
        when(eventoConectividadPersistencePort.findAllByUnidad(any(), any(), anyInt(), anyInt())).thenReturn(page);

        Result<PageResult<EventoConectividadResponseDto>> result = handler.listar(
                new ListarEventosConectividadQuery(UNIDAD_ID, TENANT_ID, 0, 20));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().content()).hasSize(1);
        assertThat(result.getValue().content().get(0).unidadExternoId()).isEqualTo(UNIDAD_ID);
        assertThat(result.getValue().content().get(0).tipoEvento()).isEqualTo("SIN_SENAL");
    }

    @Test
    void listar_sinEventos_retornaPaginaVacia() {
        PageResult<EventoConectividadView> page = PageResult.empty(0, 20);
        when(eventoConectividadPersistencePort.findAllByUnidad(any(), any(), anyInt(), anyInt())).thenReturn(page);

        Result<PageResult<EventoConectividadResponseDto>> result = handler.listar(
                new ListarEventosConectividadQuery(UNIDAD_ID, TENANT_ID, 0, 20));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().content()).isEmpty();
    }
}
