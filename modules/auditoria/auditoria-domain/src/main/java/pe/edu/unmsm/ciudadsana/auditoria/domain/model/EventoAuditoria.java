package pe.edu.unmsm.ciudadsana.auditoria.domain.model;

import pe.edu.unmsm.ciudadsana.auditoria.domain.valueobject.EventoAuditoriaId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class EventoAuditoria {

    private final EventoAuditoriaId id;
    private final TenantId tenantId;
    private final Optional<UUID> usuarioId;
    private final String modulo;
    private final String accion;
    private final String entidad;
    private final Optional<UUID> entidadId;
    private final Optional<String> datosAntes;
    private final Optional<String> datosDespues;
    private final Instant creadoEn;

    private EventoAuditoria(
            EventoAuditoriaId id,
            TenantId tenantId,
            Optional<UUID> usuarioId,
            String modulo,
            String accion,
            String entidad,
            Optional<UUID> entidadId,
            Optional<String> datosAntes,
            Optional<String> datosDespues,
            Instant creadoEn
    ) {
        this.id = id;
        this.tenantId = tenantId;
        this.usuarioId = usuarioId;
        this.modulo = modulo;
        this.accion = accion;
        this.entidad = entidad;
        this.entidadId = entidadId;
        this.datosAntes = datosAntes;
        this.datosDespues = datosDespues;
        this.creadoEn = creadoEn;
    }

    public static EventoAuditoria create(
            EventoAuditoriaId id,
            TenantId tenantId,
            UUID usuarioId,
            String modulo,
            String accion,
            String entidad,
            UUID entidadId,
            String datosAntes,
            String datosDespues,
            Instant creadoEn
    ) {
        if (id == null) throw new IllegalArgumentException("EventoAuditoria: id no puede ser nulo");
        if (tenantId == null) throw new IllegalArgumentException("EventoAuditoria: tenantId no puede ser nulo");
        if (modulo == null || modulo.isBlank()) throw new IllegalArgumentException("EventoAuditoria: modulo no puede ser vacío");
        if (accion == null || accion.isBlank()) throw new IllegalArgumentException("EventoAuditoria: accion no puede ser vacía");
        if (entidad == null || entidad.isBlank()) throw new IllegalArgumentException("EventoAuditoria: entidad no puede ser vacía");

        return new EventoAuditoria(
                id, tenantId,
                Optional.ofNullable(usuarioId),
                modulo, accion, entidad,
                Optional.ofNullable(entidadId),
                Optional.ofNullable(datosAntes),
                Optional.ofNullable(datosDespues),
                creadoEn != null ? creadoEn : Instant.now()
        );
    }

    public static EventoAuditoria reconstitute(
            EventoAuditoriaId id,
            TenantId tenantId,
            UUID usuarioId,
            String modulo,
            String accion,
            String entidad,
            UUID entidadId,
            String datosAntes,
            String datosDespues,
            Instant creadoEn
    ) {
        return new EventoAuditoria(
                id, tenantId,
                Optional.ofNullable(usuarioId),
                modulo, accion, entidad,
                Optional.ofNullable(entidadId),
                Optional.ofNullable(datosAntes),
                Optional.ofNullable(datosDespues),
                creadoEn
        );
    }

    public EventoAuditoriaId getId() { return id; }
    public TenantId getTenantId() { return tenantId; }
    public Optional<UUID> getUsuarioId() { return usuarioId; }
    public String getModulo() { return modulo; }
    public String getAccion() { return accion; }
    public String getEntidad() { return entidad; }
    public Optional<UUID> getEntidadId() { return entidadId; }
    public Optional<String> getDatosAntes() { return datosAntes; }
    public Optional<String> getDatosDespues() { return datosDespues; }
    public Instant getCreadoEn() { return creadoEn; }
}
