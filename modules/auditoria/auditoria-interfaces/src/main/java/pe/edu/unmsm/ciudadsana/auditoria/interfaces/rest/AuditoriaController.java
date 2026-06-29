package pe.edu.unmsm.ciudadsana.auditoria.interfaces.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.unmsm.ciudadsana.auditoria.application.dto.EventoAuditoriaDto;
import pe.edu.unmsm.ciudadsana.auditoria.application.dto.OutboxEventDto;
import pe.edu.unmsm.ciudadsana.auditoria.application.port.in.ListarEventosAuditoriaUseCase;
import pe.edu.unmsm.ciudadsana.auditoria.application.port.in.ListarOutboxEventsUseCase;
import pe.edu.unmsm.ciudadsana.auditoria.application.query.ListarEventosAuditoriaQuery;
import pe.edu.unmsm.ciudadsana.auditoria.application.query.ListarOutboxEventsQuery;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
import pe.edu.unmsm.ciudadsana.shared.security.context.CurrentUserProvider;
import pe.edu.unmsm.ciudadsana.shared.web.util.ResultResponseMapper;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auditoria")
public class AuditoriaController {

    private final ListarEventosAuditoriaUseCase listarEventosUseCase;
    private final ListarOutboxEventsUseCase listarOutboxUseCase;
    private final CurrentUserProvider currentUserProvider;

    public AuditoriaController(
            ListarEventosAuditoriaUseCase listarEventosUseCase,
            ListarOutboxEventsUseCase listarOutboxUseCase,
            CurrentUserProvider currentUserProvider
    ) {
        this.listarEventosUseCase = listarEventosUseCase;
        this.listarOutboxUseCase = listarOutboxUseCase;
        this.currentUserProvider = currentUserProvider;
    }

    @GetMapping("/eventos")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'ANALISTA')")
    public ResponseEntity<?> listarEventos(
            @RequestParam(required = false) String modulo,
            @RequestParam(required = false) String entidad,
            @RequestParam(required = false) UUID usuarioId,
            @RequestParam(required = false) LocalDate fechaDesde,
            @RequestParam(required = false) LocalDate fechaHasta,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        UUID tenantId = currentUserProvider.requireCurrentUser().tenantId();
        Result<PageResult<EventoAuditoriaDto>> result = listarEventosUseCase.listar(
                new ListarEventosAuditoriaQuery(
                        tenantId, modulo, entidad, usuarioId, fechaDesde, fechaHasta, page, size
                )
        );
        return ResultResponseMapper.toOk(result);
    }

    @GetMapping("/outbox")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> listarOutbox(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String eventType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        UUID tenantId = currentUserProvider.requireCurrentUser().tenantId();
        Result<PageResult<OutboxEventDto>> result = listarOutboxUseCase.listar(
                new ListarOutboxEventsQuery(tenantId, estado, eventType, page, size)
        );
        return ResultResponseMapper.toOk(result);
    }
}
