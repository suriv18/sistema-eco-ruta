package pe.edu.unmsm.ciudadsana.telemetria.domain.model;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.AggregateRoot;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.telemetria.domain.enums.OrigenPing;
import pe.edu.unmsm.ciudadsana.telemetria.domain.event.PingGpsRecibidoEvent;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.Coordenadas;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.DispositivoId;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.PingId;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.RutaExternoId;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.UnidadExternoId;

import java.time.Instant;
import java.util.Optional;

public class PingGps extends AggregateRoot<PingId> {

    private final PingId id;
    private final TenantId tenantId;
    private final DispositivoId dispositivoId;
    private final UnidadExternoId unidadExternoId;
    private final Optional<RutaExternoId> rutaExternoId;
    private final Instant ts;
    private final Coordenadas posicion;
    private final Optional<Double> velocidadKmh;
    private final Optional<Double> rumboGrados;
    private final Optional<Double> precisionM;
    private final OrigenPing origen;
    private final Instant recibidoEn;

    private PingGps(
            PingId id,
            TenantId tenantId,
            DispositivoId dispositivoId,
            UnidadExternoId unidadExternoId,
            Optional<RutaExternoId> rutaExternoId,
            Instant ts,
            Coordenadas posicion,
            Optional<Double> velocidadKmh,
            Optional<Double> rumboGrados,
            Optional<Double> precisionM,
            OrigenPing origen,
            Instant recibidoEn
    ) {
        this.id = id;
        this.tenantId = tenantId;
        this.dispositivoId = dispositivoId;
        this.unidadExternoId = unidadExternoId;
        this.rutaExternoId = rutaExternoId;
        this.ts = ts;
        this.posicion = posicion;
        this.velocidadKmh = velocidadKmh;
        this.rumboGrados = rumboGrados;
        this.precisionM = precisionM;
        this.origen = origen;
        this.recibidoEn = recibidoEn;
    }

    public static PingGps create(
            PingId id,
            TenantId tenantId,
            DispositivoId dispositivoId,
            UnidadExternoId unidadExternoId,
            Optional<RutaExternoId> rutaExternoId,
            Instant ts,
            Coordenadas posicion,
            Optional<Double> velocidadKmh,
            Optional<Double> rumboGrados,
            Optional<Double> precisionM,
            OrigenPing origen,
            Instant recibidoEn
    ) {
        if (id == null) throw new IllegalArgumentException("PingId no puede ser nulo");
        if (tenantId == null) throw new IllegalArgumentException("TenantId no puede ser nulo");
        if (dispositivoId == null) throw new IllegalArgumentException("DispositivoId no puede ser nulo");
        if (unidadExternoId == null) throw new IllegalArgumentException("UnidadExternoId no puede ser nulo");
        if (rutaExternoId == null) throw new IllegalArgumentException("rutaExternoId no puede ser nulo (use Optional.empty())");
        if (ts == null) throw new IllegalArgumentException("El timestamp del ping no puede ser nulo");
        if (posicion == null) throw new IllegalArgumentException("La posición no puede ser nula");
        if (velocidadKmh == null) throw new IllegalArgumentException("velocidadKmh no puede ser nulo (use Optional.empty())");
        if (rumboGrados == null) throw new IllegalArgumentException("rumboGrados no puede ser nulo (use Optional.empty())");
        if (precisionM == null) throw new IllegalArgumentException("precisionM no puede ser nulo (use Optional.empty())");
        if (origen == null) throw new IllegalArgumentException("El origen del ping no puede ser nulo");
        if (recibidoEn == null) throw new IllegalArgumentException("La fecha de recepción no puede ser nula");

        PingGps ping = new PingGps(
                id, tenantId, dispositivoId, unidadExternoId, rutaExternoId,
                ts, posicion, velocidadKmh, rumboGrados, precisionM, origen, recibidoEn
        );

        ping.recordDomainEvent(new PingGpsRecibidoEvent(
                id.value(),
                recibidoEn,
                tenantId.value(),
                unidadExternoId.value(),
                posicion.latitud(),
                posicion.longitud()
        ));

        return ping;
    }

    public static PingGps reconstitute(
            PingId id,
            TenantId tenantId,
            DispositivoId dispositivoId,
            UnidadExternoId unidadExternoId,
            Optional<RutaExternoId> rutaExternoId,
            Instant ts,
            Coordenadas posicion,
            Optional<Double> velocidadKmh,
            Optional<Double> rumboGrados,
            Optional<Double> precisionM,
            OrigenPing origen,
            Instant recibidoEn
    ) {
        return new PingGps(
                id, tenantId, dispositivoId, unidadExternoId, rutaExternoId,
                ts, posicion, velocidadKmh, rumboGrados, precisionM, origen, recibidoEn
        );
    }

    @Override
    public PingId getId() {
        return id;
    }

    public TenantId getTenantId() {
        return tenantId;
    }

    public DispositivoId getDispositivoId() {
        return dispositivoId;
    }

    public UnidadExternoId getUnidadExternoId() {
        return unidadExternoId;
    }

    public Optional<RutaExternoId> getRutaExternoId() {
        return rutaExternoId;
    }

    public Instant getTs() {
        return ts;
    }

    public Coordenadas getPosicion() {
        return posicion;
    }

    public Optional<Double> getVelocidadKmh() {
        return velocidadKmh;
    }

    public Optional<Double> getRumboGrados() {
        return rumboGrados;
    }

    public Optional<Double> getPrecisionM() {
        return precisionM;
    }

    public OrigenPing getOrigen() {
        return origen;
    }

    public Instant getRecibidoEn() {
        return recibidoEn;
    }
}
