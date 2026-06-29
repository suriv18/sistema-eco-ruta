package pe.edu.unmsm.ciudadsana.telemetria.domain.model;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.AggregateRoot;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.telemetria.domain.enums.EstadoDesvio;
import pe.edu.unmsm.ciudadsana.telemetria.domain.enums.SeveridadDesvio;
import pe.edu.unmsm.ciudadsana.telemetria.domain.event.DesvioRutaDetectadoEvent;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.Coordenadas;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.DesvioId;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.RutaExternoId;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.UnidadExternoId;

import java.time.Instant;
import java.util.Optional;

public class DesvioRuta extends AggregateRoot<DesvioId> {

    private final DesvioId id;
    private final TenantId tenantId;
    private final UnidadExternoId unidadExternoId;
    private final RutaExternoId rutaExternoId;
    private final Coordenadas posicion;
    private final double distanciaDesvioM;
    private final SeveridadDesvio severidad;
    private EstadoDesvio estado;
    private final Instant detectadoEn;
    private Optional<Instant> atendidoEn;

    private DesvioRuta(
            DesvioId id,
            TenantId tenantId,
            UnidadExternoId unidadExternoId,
            RutaExternoId rutaExternoId,
            Coordenadas posicion,
            double distanciaDesvioM,
            SeveridadDesvio severidad,
            EstadoDesvio estado,
            Instant detectadoEn,
            Optional<Instant> atendidoEn
    ) {
        this.id = id;
        this.tenantId = tenantId;
        this.unidadExternoId = unidadExternoId;
        this.rutaExternoId = rutaExternoId;
        this.posicion = posicion;
        this.distanciaDesvioM = distanciaDesvioM;
        this.severidad = severidad;
        this.estado = estado;
        this.detectadoEn = detectadoEn;
        this.atendidoEn = atendidoEn;
    }

    public static DesvioRuta create(
            DesvioId id,
            TenantId tenantId,
            UnidadExternoId unidadExternoId,
            RutaExternoId rutaExternoId,
            Coordenadas posicion,
            double distanciaDesvioM,
            SeveridadDesvio severidad,
            Instant detectadoEn
    ) {
        if (id == null) throw new IllegalArgumentException("DesvioId no puede ser nulo");
        if (tenantId == null) throw new IllegalArgumentException("TenantId no puede ser nulo");
        if (unidadExternoId == null) throw new IllegalArgumentException("UnidadExternoId no puede ser nulo");
        if (rutaExternoId == null) throw new IllegalArgumentException("RutaExternoId no puede ser nulo");
        if (posicion == null) throw new IllegalArgumentException("La posición no puede ser nula");
        if (distanciaDesvioM < 0) throw new IllegalArgumentException("La distancia de desvío no puede ser negativa");
        if (severidad == null) throw new IllegalArgumentException("La severidad no puede ser nula");
        if (detectadoEn == null) throw new IllegalArgumentException("La fecha de detección no puede ser nula");

        DesvioRuta desvio = new DesvioRuta(
                id, tenantId, unidadExternoId, rutaExternoId,
                posicion, distanciaDesvioM, severidad,
                EstadoDesvio.ABIERTO, detectadoEn, Optional.empty()
        );

        desvio.recordDomainEvent(new DesvioRutaDetectadoEvent(
                id.value(),
                detectadoEn,
                tenantId.value(),
                unidadExternoId.value(),
                rutaExternoId.value(),
                distanciaDesvioM
        ));

        return desvio;
    }

    public static DesvioRuta reconstitute(
            DesvioId id,
            TenantId tenantId,
            UnidadExternoId unidadExternoId,
            RutaExternoId rutaExternoId,
            Coordenadas posicion,
            double distanciaDesvioM,
            SeveridadDesvio severidad,
            EstadoDesvio estado,
            Instant detectadoEn,
            Optional<Instant> atendidoEn
    ) {
        return new DesvioRuta(
                id, tenantId, unidadExternoId, rutaExternoId,
                posicion, distanciaDesvioM, severidad,
                estado, detectadoEn, atendidoEn
        );
    }

    public void atender(Instant ahora) {
        if (this.estado != EstadoDesvio.ABIERTO) {
            throw new IllegalStateException(
                    "Solo se puede atender un desvío que esté ABIERTO. Estado actual: " + this.estado);
        }
        this.estado = EstadoDesvio.ATENDIDO;
        this.atendidoEn = Optional.of(ahora);
    }

    public void descartar() {
        if (this.estado != EstadoDesvio.ABIERTO) {
            throw new IllegalStateException(
                    "Solo se puede descartar un desvío que esté ABIERTO. Estado actual: " + this.estado);
        }
        this.estado = EstadoDesvio.DESCARTADO;
    }

    @Override
    public DesvioId getId() {
        return id;
    }

    public TenantId getTenantId() {
        return tenantId;
    }

    public UnidadExternoId getUnidadExternoId() {
        return unidadExternoId;
    }

    public RutaExternoId getRutaExternoId() {
        return rutaExternoId;
    }

    public Coordenadas getPosicion() {
        return posicion;
    }

    public double getDistanciaDesvioM() {
        return distanciaDesvioM;
    }

    public SeveridadDesvio getSeveridad() {
        return severidad;
    }

    public EstadoDesvio getEstado() {
        return estado;
    }

    public Instant getDetectadoEn() {
        return detectadoEn;
    }

    public Optional<Instant> getAtendidoEn() {
        return atendidoEn;
    }
}
