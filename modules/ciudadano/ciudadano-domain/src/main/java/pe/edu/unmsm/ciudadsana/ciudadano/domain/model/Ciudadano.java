package pe.edu.unmsm.ciudadsana.ciudadano.domain.model;

import pe.edu.unmsm.ciudadsana.ciudadano.domain.enums.EstadoCiudadano;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.event.CiudadanoRegistradoEvent;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.CiudadanoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.AggregateRoot;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;

import java.time.Instant;
import java.util.Optional;

public class Ciudadano extends AggregateRoot<CiudadanoId> {

    private final CiudadanoId id;
    private final TenantId tenantId;
    private final Optional<String> nombres;
    private final Optional<String> apellidos;
    private final Optional<String> telefono;
    private final Optional<String> email;
    private final Optional<String> documento;
    private EstadoCiudadano estado;
    private final Instant creadoEn;

    private Ciudadano(
            CiudadanoId id,
            TenantId tenantId,
            Optional<String> nombres,
            Optional<String> apellidos,
            Optional<String> email,
            Optional<String> telefono,
            Optional<String> documento,
            EstadoCiudadano estado,
            Instant creadoEn
    ) {
        this.id = id;
        this.tenantId = tenantId;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
        this.telefono = telefono;
        this.documento = documento;
        this.estado = estado;
        this.creadoEn = creadoEn;
    }

    /**
     * Factory method that registers a new Ciudadano and fires {@link CiudadanoRegistradoEvent}.
     * All String parameters are optional and may be {@code null}.
     */
    public static Ciudadano create(
            CiudadanoId id,
            TenantId tenantId,
            String nombres,
            String apellidos,
            String email,
            String telefono,
            String documento,
            Instant creadoEn
    ) {
        if (id == null) {
            throw new IllegalArgumentException("Ciudadano: id no puede ser nulo");
        }
        if (tenantId == null) {
            throw new IllegalArgumentException("Ciudadano: tenantId no puede ser nulo");
        }
        if (creadoEn == null) {
            throw new IllegalArgumentException("Ciudadano: creadoEn no puede ser nulo");
        }

        Ciudadano ciudadano = new Ciudadano(
                id,
                tenantId,
                Optional.ofNullable(nombres),
                Optional.ofNullable(apellidos),
                Optional.ofNullable(email),
                Optional.ofNullable(telefono),
                Optional.ofNullable(documento),
                EstadoCiudadano.ACTIVO,
                creadoEn
        );

        ciudadano.recordDomainEvent(new CiudadanoRegistradoEvent(
                id.value(),
                creadoEn,
                tenantId.value(),
                email
        ));

        return ciudadano;
    }

    /**
     * Reconstitutes a Ciudadano from persistent state without firing domain events.
     */
    public static Ciudadano reconstitute(
            CiudadanoId id,
            TenantId tenantId,
            String nombres,
            String apellidos,
            String email,
            String telefono,
            String documento,
            EstadoCiudadano estado,
            Instant creadoEn
    ) {
        if (id == null) {
            throw new IllegalArgumentException("Ciudadano: id no puede ser nulo");
        }
        if (tenantId == null) {
            throw new IllegalArgumentException("Ciudadano: tenantId no puede ser nulo");
        }
        if (estado == null) {
            throw new IllegalArgumentException("Ciudadano: estado no puede ser nulo");
        }
        if (creadoEn == null) {
            throw new IllegalArgumentException("Ciudadano: creadoEn no puede ser nulo");
        }

        return new Ciudadano(
                id,
                tenantId,
                Optional.ofNullable(nombres),
                Optional.ofNullable(apellidos),
                Optional.ofNullable(email),
                Optional.ofNullable(telefono),
                Optional.ofNullable(documento),
                estado,
                creadoEn
        );
    }

    // -------------------------------------------------------------------------
    // Behaviour
    // -------------------------------------------------------------------------

    /**
     * Bloquea al ciudadano. Lanza {@link IllegalStateException} si ya está BLOQUEADO.
     */
    public void bloquear() {
        if (EstadoCiudadano.BLOQUEADO.equals(this.estado)) {
            throw new IllegalStateException(
                    "El ciudadano ya se encuentra en estado BLOQUEADO");
        }
        this.estado = EstadoCiudadano.BLOQUEADO;
    }

    /**
     * Activa al ciudadano. Lanza {@link IllegalStateException} si ya está ACTIVO.
     */
    public void activar() {
        if (EstadoCiudadano.ACTIVO.equals(this.estado)) {
            throw new IllegalStateException(
                    "El ciudadano ya se encuentra en estado ACTIVO");
        }
        this.estado = EstadoCiudadano.ACTIVO;
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    @Override
    public CiudadanoId getId() {
        return id;
    }

    public TenantId getTenantId() {
        return tenantId;
    }

    public Optional<String> getNombres() {
        return nombres;
    }

    public Optional<String> getApellidos() {
        return apellidos;
    }

    public Optional<String> getTelefono() {
        return telefono;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public Optional<String> getDocumento() {
        return documento;
    }

    public EstadoCiudadano getEstado() {
        return estado;
    }

    public Instant getCreadoEn() {
        return creadoEn;
    }
}
