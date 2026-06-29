package pe.edu.unmsm.ciudadsana.telemetria.domain.model;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.telemetria.domain.enums.EstadoMovimiento;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.Coordenadas;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.RutaExternoId;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.UnidadExternoId;

import java.time.Instant;
import java.util.Optional;

public class EstadoUnidad {

    private final UnidadExternoId unidadExternoId;
    private final TenantId tenantId;
    private Optional<RutaExternoId> rutaExternoId;
    private Optional<Coordenadas> ultimaPosicion;
    private Optional<Double> ultimaVelocidadKmh;
    private Optional<Instant> ultimoPingEn;
    private EstadoMovimiento estadoMovimiento;
    private Instant actualizadoEn;

    private EstadoUnidad(
            UnidadExternoId unidadExternoId,
            TenantId tenantId,
            Optional<RutaExternoId> rutaExternoId,
            Optional<Coordenadas> ultimaPosicion,
            Optional<Double> ultimaVelocidadKmh,
            Optional<Instant> ultimoPingEn,
            EstadoMovimiento estadoMovimiento,
            Instant actualizadoEn
    ) {
        this.unidadExternoId = unidadExternoId;
        this.tenantId = tenantId;
        this.rutaExternoId = rutaExternoId;
        this.ultimaPosicion = ultimaPosicion;
        this.ultimaVelocidadKmh = ultimaVelocidadKmh;
        this.ultimoPingEn = ultimoPingEn;
        this.estadoMovimiento = estadoMovimiento;
        this.actualizadoEn = actualizadoEn;
    }

    public static EstadoUnidad create(
            UnidadExternoId unidadExternoId,
            TenantId tenantId,
            Instant actualizadoEn
    ) {
        if (unidadExternoId == null) throw new IllegalArgumentException("UnidadExternoId no puede ser nulo");
        if (tenantId == null) throw new IllegalArgumentException("TenantId no puede ser nulo");
        if (actualizadoEn == null) throw new IllegalArgumentException("La fecha de actualización no puede ser nula");

        return new EstadoUnidad(
                unidadExternoId,
                tenantId,
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                EstadoMovimiento.SIN_SENAL,
                actualizadoEn
        );
    }

    public static EstadoUnidad reconstitute(
            UnidadExternoId unidadExternoId,
            TenantId tenantId,
            Optional<RutaExternoId> rutaExternoId,
            Optional<Coordenadas> ultimaPosicion,
            Optional<Double> ultimaVelocidadKmh,
            Optional<Instant> ultimoPingEn,
            EstadoMovimiento estadoMovimiento,
            Instant actualizadoEn
    ) {
        return new EstadoUnidad(
                unidadExternoId,
                tenantId,
                rutaExternoId,
                ultimaPosicion,
                ultimaVelocidadKmh,
                ultimoPingEn,
                estadoMovimiento,
                actualizadoEn
        );
    }

    public void actualizarPosicion(
            Coordenadas pos,
            Double velocidad,
            Optional<RutaExternoId> rutaId,
            EstadoMovimiento estado,
            Instant ahora
    ) {
        if (pos == null) throw new IllegalArgumentException("La posición no puede ser nula");
        if (rutaId == null) throw new IllegalArgumentException("rutaId no puede ser nulo (use Optional.empty())");
        if (estado == null) throw new IllegalArgumentException("El estado de movimiento no puede ser nulo");
        if (ahora == null) throw new IllegalArgumentException("El instante de actualización no puede ser nulo");

        this.ultimaPosicion = Optional.of(pos);
        this.ultimaVelocidadKmh = Optional.ofNullable(velocidad);
        this.rutaExternoId = rutaId;
        this.estadoMovimiento = estado;
        this.ultimoPingEn = Optional.of(ahora);
        this.actualizadoEn = ahora;
    }

    public UnidadExternoId getUnidadExternoId() {
        return unidadExternoId;
    }

    public TenantId getTenantId() {
        return tenantId;
    }

    public Optional<RutaExternoId> getRutaExternoId() {
        return rutaExternoId;
    }

    public Optional<Coordenadas> getUltimaPosicion() {
        return ultimaPosicion;
    }

    public Optional<Double> getUltimaVelocidadKmh() {
        return ultimaVelocidadKmh;
    }

    public Optional<Instant> getUltimoPingEn() {
        return ultimoPingEn;
    }

    public EstadoMovimiento getEstadoMovimiento() {
        return estadoMovimiento;
    }

    public Instant getActualizadoEn() {
        return actualizadoEn;
    }
}
