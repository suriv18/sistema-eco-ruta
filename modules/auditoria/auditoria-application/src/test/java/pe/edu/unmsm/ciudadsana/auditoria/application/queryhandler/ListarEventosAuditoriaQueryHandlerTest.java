package pe.edu.unmsm.ciudadsana.auditoria.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.auditoria.application.dto.EventoAuditoriaDto;
import pe.edu.unmsm.ciudadsana.auditoria.application.port.out.AuditoriaPersistencePort;
import pe.edu.unmsm.ciudadsana.auditoria.application.query.ListarEventosAuditoriaQuery;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarEventosAuditoriaQueryHandlerTest {

    @Mock
    AuditoriaPersistencePort persistencePort;

    @InjectMocks
    ListarEventosAuditoriaQueryHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID USUARIO_ID = UUID.randomUUID();

    private EventoAuditoriaDto eventoDto() {
        return new EventoAuditoriaDto(UUID.randomUUID(), TENANT_ID, USUARIO_ID,
                "ROLES", "CREAR", "Rol", UUID.randomUUID(),
                null, "{\"codigo\":\"AUDITOR\"}", Instant.now());
    }

    @Test
    void listar_conEventos_retornaPagina() {
        PageResult<EventoAuditoriaDto> page = PageResult.of(List.of(eventoDto()), 0, 20, 1L);
        when(persistencePort.findEventos(any(), isNull(), isNull(), isNull(), isNull(), isNull(), anyInt(), anyInt()))
                .thenReturn(page);

        Result<PageResult<EventoAuditoriaDto>> result = handler.listar(
                new ListarEventosAuditoriaQuery(TENANT_ID, null, null, null, null, null, 0, 20));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().content()).hasSize(1);
        assertThat(result.getValue().content().get(0).modulo()).isEqualTo("ROLES");
    }

    @Test
    void listar_conFiltros_retornaPaginaFiltrada() {
        PageResult<EventoAuditoriaDto> page = PageResult.of(List.of(eventoDto()), 0, 20, 1L);
        when(persistencePort.findEventos(any(), any(), any(), any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(page);

        LocalDate desde = LocalDate.of(2026, 6, 1);
        LocalDate hasta = LocalDate.of(2026, 6, 29);
        Result<PageResult<EventoAuditoriaDto>> result = handler.listar(
                new ListarEventosAuditoriaQuery(TENANT_ID, "ROLES", "Rol", USUARIO_ID, desde, hasta, 0, 20));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().totalElements()).isEqualTo(1L);
    }
}
