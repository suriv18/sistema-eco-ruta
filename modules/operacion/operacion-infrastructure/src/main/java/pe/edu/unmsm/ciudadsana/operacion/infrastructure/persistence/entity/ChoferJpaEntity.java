package pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import pe.edu.unmsm.ciudadsana.shared.persistence.entity.TenantAwareJpaEntity;

@Entity
@Table(name = "chofer", schema = "operacion")
public class ChoferJpaEntity extends TenantAwareJpaEntity {

    @Column(name = "nombres", nullable = false, length = 120)
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 120)
    private String apellidos;

    @Column(name = "dni", length = 8)
    private String dni;

    @Column(name = "licencia", length = 30)
    private String licencia;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "estado", nullable = false, length = 30)
    private String estado;

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getLicencia() { return licencia; }
    public void setLicencia(String licencia) { this.licencia = licencia; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
