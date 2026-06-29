package pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import pe.edu.unmsm.ciudadsana.shared.persistence.entity.TenantAwareJpaEntity;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "usuario", schema = "auth")
public class UsuarioJpaEntity extends TenantAwareJpaEntity {

    @Column(name = "nombres", nullable = false, length = 120)
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 120)
    private String apellidos;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "username", nullable = false, unique = true, length = 80)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "estado", nullable = false, length = 30)
    private String estado;

    @Column(name = "ultimo_login_en")
    private Instant ultimoLoginEn;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "usuario_rol",
            schema = "auth",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<RolJpaEntity> roles = new HashSet<>();

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Instant getUltimoLoginEn() { return ultimoLoginEn; }
    public void setUltimoLoginEn(Instant ultimoLoginEn) { this.ultimoLoginEn = ultimoLoginEn; }
    public Set<RolJpaEntity> getRoles() { return roles; }
    public void setRoles(Set<RolJpaEntity> roles) { this.roles = roles; }
}
