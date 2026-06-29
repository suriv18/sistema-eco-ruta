package pe.edu.unmsm.ciudadsana.telemetria.domain.model;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.AggregateRoot;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.telemetria.domain.enums.EstadoDispositivo;
import pe.edu.unmsm.ciudadsana.telemetria.domain.event.DispositivoRegistradoEvent;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.DispositivoId;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.UnidadExternoId;

import java.time.Instant;
import java.util.Optional;

public class DispositivoGps extends AggregateRoot<DispositivoId> {

    private final DispositivoId id;
    private final TenantId tenantId;
    private final UnidadExternoId unidadExternoId;
    private final Optional<String> imei;
    private final Optional<String> proveedor;
    private EstadoDispositivo estado;
    private Optional<Instant> ultimoPingEn;
    private final Instant creadoEn;

    private DispositivoGps(
            DispositivoId id,
            TenantId tenantId,
            UnidadExternoId unidadExternoId,
            Optional<String> imei,
            Optional<String> proveedor,
            EstadoDispositivo estado,
            Optional<Instant> ultimoPingEn,
            Instant creadoEn
    ) {
        this.id = id;
        this.tenantId = tenantId;
        this.unidadExternoId = unidadExternoId;
        this.imei = imei;
        this.proveedor = proveedor;
        this.estado = estado;
        this.ultimoPingEn = ultimoPingEn;
        this.creadoEn = creadoEn;
    }

    public static DispositivoGps create(
            DispositivoId id,
            TenantId tenantId,
            UnidadExternoId unidadExternoId,
            Optional<String> imei,
            Optional<String> proveedor,
            Instant creadoEn
    ) {
        if (id == null) throw new IllegalArgumentException("DispositivoId no puede ser nulo");
        if (tenantId == null) throw new IllegalArgumentException("TenantId no puede ser nulo");
        if (unidadExternoId == null) throw new IllegalArgumentException("UnidadExternoId no puede ser nulo");
        if (imei == null) throw new IllegalArgumentException("imei no puede ser nulo (use Optional.empty())");
        if (proveedor == null) throw new IllegalArgumentException("proveedor no puede ser nulo (use Optional.empty())");
        if (creadoEn == null) throw new IllegalArgumentException("La fecha de creación no puede ser nula");

        DispositivoGps dispositivo = new DispositivoGps(
                id,
                tenantId,
                unidadExternoId,
                imei,
                proveedor,
                EstadoDispositivo.ACTIVO,
                Optional.empty(),
                creadoEn
        );

        dispositivo.recordDomainEvent(new DispositivoRegistradoEvent(
                id.value(),
                creadoEn,
                tenantId.value(),
                unidadExternoId.value()
        ));

        return dispositivo;
    }

    public static DispositivoGps reconstitute(
            DispositivoId id,
            TenantId tenantId,
            UnidadExternoId unidadExternoId,
            Optional<String> imei,
            Optional<String> proveedor,
            EstadoDispositivo estado,
            Optional<Instant> ultimoPingEn,
            Instant creadoEn
    ) {
        return new DispositivoGps(
                id,
                tenantId,
                unidadExternoId,
                imei,
                proveedor,
                estado,
                ultimoPingEn,
                creadoEn
        );
    }

    public void registrarPing(Instant ahora) {
        if (ahora == null) throw new IllegalArgumentException("El instante del ping no puede ser nulo");
        this.ultimoPingEn = Optional.of(ahora);
        if (this.estado == EstadoDispositivo.FALLA) {
            this.estado = EstadoDispositivo.ACTIVO;
        }
    }

    public void marcarFalla() {
        if (this.estado == EstadoDispositivo.INACTIVO) {
            throw new IllegalStateException("No se puede marcar falla en un dispositivo INACTIVO");
        }
        this.estado = EstadoDispositivo.FALLA;
    }

    public void desactivar() {
        this.estado = EstadoDispositivo.INACTIVO;
    }

    @Override
    public DispositivoId getId() {
        return id;
    }

    public TenantId getTenantId() {
        return tenantId;
    }

    public UnidadExternoId getUnidadExternoId() {
        return unidadExternoId;
    }

    public Optional<String> getImei() {
        return imei;
    }

    public Optional<String> getProveedor() {
        return proveedor;
    }

    public EstadoDispositivo getEstado() {
        return estado;
    }

    public Optional<Instant> getUltimoPingEn() {
        return ultimoPingEn;
    }

    public Instant getCreadoEn() {
        return creadoEn;
    }
}
