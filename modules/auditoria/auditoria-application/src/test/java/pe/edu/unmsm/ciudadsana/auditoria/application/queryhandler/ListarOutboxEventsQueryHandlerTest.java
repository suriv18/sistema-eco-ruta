package pe.edu.unmsm.ciudadsana.auditoria.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.auditoria.application.dto.OutboxEventDto;
import pe.edu.unmsm.ciudadsana.auditoria.application.port.out.AuditoriaPersistencePort;
import pe.edu.unmsm.ciudadsana.auditoria.application.query.ListarOutboxEventsQuery;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarOutboxEventsQueryHandlerTest {

    @Mock
    AuditoriaPersistencePort persistencePort;

    @InjectMocks
    ListarOutboxEventsQueryHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    private OutboxEventDto outboxDto() {
        return new OutboxEventDto(UUID.randomUUID(), TENANT_ID,
                "Rol", UUID.randomUUID(), "ROL_CREADO",
                "{\"codigo\":\"AUDITOR\"}", "PENDIENTE",
                Instant.now(), null, null);
    }

    @Test
    void listar_conEventos_retornaPagina() {
        PageResult<OutboxEventDto> page = PageResult.of(List.of(outboxDto()), 0, 20, 1L);
        when(persistencePort.findOutboxEvents(any(), any(), any(), anyInt(), anyInt())).thenReturn(page);

        Result<PageResult<OutboxEventDto>> result = handler.listar(
                new ListarOutboxEventsQuery(TENANT_ID, "PENDIENTE", "ROL_CREADO", 0, 20));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().content()).hasSize(1);
        assertThat(result.getValue().content().get(0).estado()).isEqualTo("PENDIENTE");
        assertThat(result.getValue().content().get(0).eventType()).isEqualTo("ROL_CREADO");
    }

    @Test
    void listar_sinEventos_retornaPaginaVacia() {
        PageResult<OutboxEventDto> page = PageResult.empty(0, 20);
        when(persistencePort.findOutboxEvents(any(), any(), any(), anyInt(), anyInt())).thenReturn(page);

        Result<PageResult<OutboxEventDto>> result = handler.listar(
                new ListarOutboxEventsQuery(TENANT_ID, null, null, 0, 20));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().content()).isEmpty();
    }
}
