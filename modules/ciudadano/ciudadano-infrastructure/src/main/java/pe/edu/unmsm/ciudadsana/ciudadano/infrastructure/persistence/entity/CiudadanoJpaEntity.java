package pe.edu.unmsm.ciudadsana.ciudadano.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.unmsm.ciudadsana.shared.persistence.entity.TenantAwareJpaEntity;

@Entity
@Table(name = "ciudadano", schema = "ciudadano")
@Getter
@Setter
@NoArgsConstructor
public class CiudadanoJpaEntity extends TenantAwareJpaEntity {

    @Column(name = "nombres")
    private String nombres;

    @Column(name = "apellidos")
    private String apellidos;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "email")
    private String email;

    @Column(name = "documento")
    private String documento;

    @Column(name = "estado", nullable = false)
    private String estado;
}
