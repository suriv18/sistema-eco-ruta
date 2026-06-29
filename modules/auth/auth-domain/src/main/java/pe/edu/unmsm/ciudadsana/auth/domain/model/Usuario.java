package pe.edu.unmsm.ciudadsana.auth.domain.model;

import pe.edu.unmsm.ciudadsana.auth.domain.enums.EstadoUsuario;
import pe.edu.unmsm.ciudadsana.auth.domain.event.LoginExitosoEvent;
import pe.edu.unmsm.ciudadsana.auth.domain.event.RolAsignadoEvent;
import pe.edu.unmsm.ciudadsana.auth.domain.event.UsuarioBloqueadoEvent;
import pe.edu.unmsm.ciudadsana.auth.domain.event.UsuarioRegistradoEvent;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.Email;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.NombresCompletos;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.PasswordHash;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.RolId;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.UsuarioId;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.Username;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.AggregateRoot;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Usuario extends AggregateRoot<UsuarioId> {

    private final UsuarioId id;
    private final TenantId tenantId;
    private NombresCompletos nombresCompletos;
    private final Email email;
    private final Username username;
    private PasswordHash passwordHash;
    private final String telefono;
    private EstadoUsuario estado;
    private Instant ultimoLoginEn;
    private final Instant creadoEn;
    private Instant actualizadoEn;
    private final Set<RolId> roles;

    private Usuario(
            UsuarioId id,
            TenantId tenantId,
            NombresCompletos nombresCompletos,
            Email email,
            Username username,
            PasswordHash passwordHash,
            String telefono,
            EstadoUsuario estado,
            Instant ultimoLoginEn,
            Instant creadoEn,
            Instant actualizadoEn,
            Set<RolId> roles
    ) {
        this.id = id;
        this.tenantId = tenantId;
        this.nombresCompletos = nombresCompletos;
        this.email = email;
        this.username = username;
        this.passwordHash = passwordHash;
        this.telefono = telefono;
        this.estado = estado;
        this.ultimoLoginEn = ultimoLoginEn;
        this.creadoEn = creadoEn;
        this.actualizadoEn = actualizadoEn;
        this.roles = roles;
    }

    public static Usuario create(
            UsuarioId id,
            TenantId tenantId,
            NombresCompletos nombresCompletos,
            Email email,
            Username username,
            PasswordHash passwordHash,
            String telefono,
            Instant creadoEn
    ) {
        if (id == null) throw new IllegalArgumentException("UsuarioId no puede ser nulo");
        if (tenantId == null) throw new IllegalArgumentException("TenantId no puede ser nulo");
        if (nombresCompletos == null) throw new IllegalArgumentException("NombresCompletos no puede ser nulo");
        if (email == null) throw new IllegalArgumentException("Email no puede ser nulo");
        if (username == null) throw new IllegalArgumentException("Username no puede ser nulo");
        if (passwordHash == null) throw new IllegalArgumentException("PasswordHash no puede ser nulo");
        if (creadoEn == null) throw new IllegalArgumentException("La fecha de creación no puede ser nula");

        Usuario usuario = new Usuario(
                id,
                tenantId,
                nombresCompletos,
                email,
                username,
                passwordHash,
                telefono,
                EstadoUsuario.ACTIVO,
                null,
                creadoEn,
                creadoEn,
                new HashSet<>()
        );

        usuario.recordDomainEvent(new UsuarioRegistradoEvent(
                id.value(),
                creadoEn,
                tenantId.value(),
                email.value(),
                username.value()
        ));

        return usuario;
    }

    public static Usuario reconstitute(
            UsuarioId id,
            TenantId tenantId,
            NombresCompletos nombresCompletos,
            Email email,
            Username username,
            PasswordHash passwordHash,
            String telefono,
            EstadoUsuario estado,
            Instant ultimoLoginEn,
            Instant creadoEn,
            Instant actualizadoEn,
            Set<RolId> roles
    ) {
        return new Usuario(
                id,
                tenantId,
                nombresCompletos,
                email,
                username,
                passwordHash,
                telefono,
                estado,
                ultimoLoginEn,
                creadoEn,
                actualizadoEn,
                new HashSet<>(roles)
        );
    }

    public void registrarLoginExitoso(Instant ahora, String ipOrigen) {
        if (estado == EstadoUsuario.INACTIVO) {
            throw new IllegalStateException("El usuario está inactivo y no puede iniciar sesión");
        }
        if (estado == EstadoUsuario.BLOQUEADO) {
            throw new IllegalStateException("El usuario está bloqueado y no puede iniciar sesión");
        }
        this.ultimoLoginEn = ahora;
        recordDomainEvent(new LoginExitosoEvent(
                id.value(),
                ahora,
                tenantId.value(),
                username.value(),
                ipOrigen
        ));
    }

    public void bloquear(Instant ahora) {
        if (estado != EstadoUsuario.ACTIVO) {
            throw new IllegalStateException("Solo se puede bloquear un usuario que esté ACTIVO. Estado actual: " + estado);
        }
        this.estado = EstadoUsuario.BLOQUEADO;
        this.actualizadoEn = ahora;
        recordDomainEvent(new UsuarioBloqueadoEvent(
                id.value(),
                ahora,
                tenantId.value()
        ));
    }

    public void activar() {
        if (estado == EstadoUsuario.ACTIVO) {
            throw new IllegalStateException("El usuario ya está ACTIVO");
        }
        this.estado = EstadoUsuario.ACTIVO;
    }

    public void asignarRol(RolId rolId, Instant ahora) {
        if (rolId == null) throw new IllegalArgumentException("RolId no puede ser nulo");
        roles.add(rolId);
        recordDomainEvent(new RolAsignadoEvent(
                id.value(),
                ahora,
                tenantId.value(),
                rolId.value()
        ));
    }

    public void removerRol(RolId rolId) {
        roles.remove(rolId);
    }

    public void cambiarPassword(PasswordHash nuevoHash) {
        if (nuevoHash == null) throw new IllegalArgumentException("El nuevo hash de contraseña no puede ser nulo");
        this.passwordHash = nuevoHash;
    }

    @Override
    public UsuarioId getId() {
        return id;
    }

    public TenantId getTenantId() {
        return tenantId;
    }

    public NombresCompletos getNombresCompletos() {
        return nombresCompletos;
    }

    public Email getEmail() {
        return email;
    }

    public Username getUsername() {
        return username;
    }

    public PasswordHash getPasswordHash() {
        return passwordHash;
    }

    public String getTelefono() {
        return telefono;
    }

    public EstadoUsuario getEstado() {
        return estado;
    }

    public Instant getUltimoLoginEn() {
        return ultimoLoginEn;
    }

    public Instant getCreadoEn() {
        return creadoEn;
    }

    public Instant getActualizadoEn() {
        return actualizadoEn;
    }

    public Set<RolId> getRoles() {
        return Collections.unmodifiableSet(roles);
    }
}
