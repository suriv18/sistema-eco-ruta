package pe.edu.unmsm.ciudadsana.ciudadano.domain.model;

import pe.edu.unmsm.ciudadsana.ciudadano.domain.enums.EstadoAlerta;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.enums.FuenteAlerta;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.enums.NivelCriticidad;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.enums.VolumenEstimado;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.event.AlertaEstadoCambiadoEvent;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.event.AlertaRegistradaEvent;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.event.AlertaValidadaEvent;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.AlertaFoto;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.AlertaHistorial;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.AlertaId;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.CiudadanoId;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.Coordenadas;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.DistritoExternoId;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.ValidacionAlerta;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.ZonaExternoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.AggregateRoot;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;

import java.time.Instant;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class AlertaCiudadana extends AggregateRoot<AlertaId> {

    /**
     * Legal state transitions.
     */
    private static final Map<EstadoAlerta, Set<EstadoAlerta>> TRANSICIONES_VALIDAS = Map.of(
            EstadoAlerta.REGISTRADA, EnumSet.of(
                    EstadoAlerta.VALIDADA,
                    EstadoAlerta.DESCARTADA,
                    EstadoAlerta.DUPLICADA),
            EstadoAlerta.VALIDADA, EnumSet.of(
                    EstadoAlerta.EN_ATENCION,
                    EstadoAlerta.DESCARTADA),
            EstadoAlerta.EN_ATENCION, EnumSet.of(
                    EstadoAlerta.ATENDIDA,
                    EstadoAlerta.DESCARTADA),
            EstadoAlerta.ATENDIDA, EnumSet.noneOf(EstadoAlerta.class),
            EstadoAlerta.DESCARTADA, EnumSet.noneOf(EstadoAlerta.class),
            EstadoAlerta.DUPLICADA, EnumSet.noneOf(EstadoAlerta.class)
    );

    private final AlertaId id;
    private final TenantId tenantId;
    private final Optional<CiudadanoId> ciudadanoId;
    private final DistritoExternoId distritoExternoId;
    private final Optional<ZonaExternoId> zonaExternoId;
    private final String titulo;
    private final Optional<String> descripcion;
    private final Coordenadas ubicacion;
    private final VolumenEstimado volumenEstimado;
    private final NivelCriticidad nivelCriticidad;
    private final FuenteAlerta fuente;
    private EstadoAlerta estado;
    private final List<AlertaFoto> fotos;
    private final List<AlertaHistorial> historial;
    private Optional<ValidacionAlerta> validacion;
    private final Instant registradaEn;
    private Optional<Instant> actualizadaEn;

    private AlertaCiudadana(
            AlertaId id,
            TenantId tenantId,
            Optional<CiudadanoId> ciudadanoId,
            DistritoExternoId distritoExternoId,
            Optional<ZonaExternoId> zonaExternoId,
            String titulo,
            Optional<String> descripcion,
            Coordenadas ubicacion,
            VolumenEstimado volumenEstimado,
            NivelCriticidad nivelCriticidad,
            FuenteAlerta fuente,
            EstadoAlerta estado,
            List<AlertaFoto> fotos,
            List<AlertaHistorial> historial,
            Optional<ValidacionAlerta> validacion,
            Instant registradaEn,
            Optional<Instant> actualizadaEn
    ) {
        this.id = id;
        this.tenantId = tenantId;
        this.ciudadanoId = ciudadanoId;
        this.distritoExternoId = distritoExternoId;
        this.zonaExternoId = zonaExternoId;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.volumenEstimado = volumenEstimado;
        this.nivelCriticidad = nivelCriticidad;
        this.fuente = fuente;
        this.estado = estado;
        this.fotos = fotos;
        this.historial = historial;
        this.validacion = validacion;
        this.registradaEn = registradaEn;
        this.actualizadaEn = actualizadaEn;
    }

    /**
     * Factory method that creates a new AlertaCiudadana in state REGISTRADA
     * and fires {@link AlertaRegistradaEvent}.
     *
     * @param ciudadanoId  optional — pass {@code null} for anonymous reports
     * @param zonaExternoId optional — pass {@code null} when zone is unknown
     * @param descripcion  optional — pass {@code null} when not provided
     */
    public static AlertaCiudadana create(
            AlertaId id,
            TenantId tenantId,
            CiudadanoId ciudadanoId,
            DistritoExternoId distritoExternoId,
            ZonaExternoId zonaExternoId,
            String titulo,
            String descripcion,
            Coordenadas ubicacion,
            VolumenEstimado volumenEstimado,
            NivelCriticidad nivelCriticidad,
            FuenteAlerta fuente,
            Instant registradaEn
    ) {
        requireNonNull(id, "AlertaCiudadana: id no puede ser nulo");
        requireNonNull(tenantId, "AlertaCiudadana: tenantId no puede ser nulo");
        requireNonNull(distritoExternoId, "AlertaCiudadana: distritoExternoId no puede ser nulo");
        requireNonNullOrBlank(titulo, "AlertaCiudadana: titulo no puede ser nulo ni vacío");
        requireNonNull(ubicacion, "AlertaCiudadana: ubicacion no puede ser nulo");
        requireNonNull(volumenEstimado, "AlertaCiudadana: volumenEstimado no puede ser nulo");
        requireNonNull(nivelCriticidad, "AlertaCiudadana: nivelCriticidad no puede ser nulo");
        requireNonNull(fuente, "AlertaCiudadana: fuente no puede ser nulo");
        requireNonNull(registradaEn, "AlertaCiudadana: registradaEn no puede ser nulo");

        AlertaCiudadana alerta = new AlertaCiudadana(
                id,
                tenantId,
                Optional.ofNullable(ciudadanoId),
                distritoExternoId,
                Optional.ofNullable(zonaExternoId),
                titulo,
                Optional.ofNullable(descripcion),
                ubicacion,
                volumenEstimado,
                nivelCriticidad,
                fuente,
                EstadoAlerta.REGISTRADA,
                new ArrayList<>(),
                new ArrayList<>(),
                Optional.empty(),
                registradaEn,
                Optional.empty()
        );

        alerta.recordDomainEvent(new AlertaRegistradaEvent(
                id.value(),
                registradaEn,
                tenantId.value(),
                distritoExternoId.value(),
                titulo
        ));

        return alerta;
    }

    /**
     * Reconstitutes an AlertaCiudadana from persistent state without firing domain events.
     *
     * @param ciudadanoId   nullable
     * @param zonaExternoId nullable
     * @param descripcion   nullable
     * @param validacion    nullable
     * @param actualizadaEn nullable
     */
    public static AlertaCiudadana reconstitute(
            AlertaId id,
            TenantId tenantId,
            CiudadanoId ciudadanoId,
            DistritoExternoId distritoExternoId,
            ZonaExternoId zonaExternoId,
            String titulo,
            String descripcion,
            Coordenadas ubicacion,
            VolumenEstimado volumenEstimado,
            NivelCriticidad nivelCriticidad,
            FuenteAlerta fuente,
            EstadoAlerta estado,
            List<AlertaFoto> fotos,
            List<AlertaHistorial> historial,
            ValidacionAlerta validacion,
            Instant registradaEn,
            Instant actualizadaEn
    ) {
        requireNonNull(id, "AlertaCiudadana: id no puede ser nulo");
        requireNonNull(tenantId, "AlertaCiudadana: tenantId no puede ser nulo");
        requireNonNull(distritoExternoId, "AlertaCiudadana: distritoExternoId no puede ser nulo");
        requireNonNullOrBlank(titulo, "AlertaCiudadana: titulo no puede ser nulo ni vacío");
        requireNonNull(ubicacion, "AlertaCiudadana: ubicacion no puede ser nulo");
        requireNonNull(volumenEstimado, "AlertaCiudadana: volumenEstimado no puede ser nulo");
        requireNonNull(nivelCriticidad, "AlertaCiudadana: nivelCriticidad no puede ser nulo");
        requireNonNull(fuente, "AlertaCiudadana: fuente no puede ser nulo");
        requireNonNull(estado, "AlertaCiudadana: estado no puede ser nulo");
        requireNonNull(registradaEn, "AlertaCiudadana: registradaEn no puede ser nulo");

        return new AlertaCiudadana(
                id,
                tenantId,
                Optional.ofNullable(ciudadanoId),
                distritoExternoId,
                Optional.ofNullable(zonaExternoId),
                titulo,
                Optional.ofNullable(descripcion),
                ubicacion,
                volumenEstimado,
                nivelCriticidad,
                fuente,
                estado,
                fotos != null ? new ArrayList<>(fotos) : new ArrayList<>(),
                historial != null ? new ArrayList<>(historial) : new ArrayList<>(),
                Optional.ofNullable(validacion),
                registradaEn,
                Optional.ofNullable(actualizadaEn)
        );
    }

    // -------------------------------------------------------------------------
    // Behaviour
    // -------------------------------------------------------------------------

    /**
     * Adds a photo attachment to this alert.
     */
    public void agregarFoto(AlertaFoto foto) {
        if (foto == null) {
            throw new IllegalArgumentException("La foto no puede ser nula");
        }
        this.fotos.add(foto);
    }

    /**
     * Transitions the alert to a new state. Validates that the transition is legal,
     * appends an {@link AlertaHistorial} entry, fires {@link AlertaEstadoCambiadoEvent},
     * and updates {@code actualizadaEn}.
     *
     * @param nuevoEstado          the target state
     * @param comentario           optional comment (may be {@code null})
     * @param cambiadoPorUsuarioId optional user who performed the change (may be {@code null})
     * @param ahora                the timestamp of the transition
     * @throws IllegalStateException if the transition is not allowed
     */
    public void cambiarEstado(
            EstadoAlerta nuevoEstado,
            String comentario,
            UUID cambiadoPorUsuarioId,
            Instant ahora
    ) {
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El nuevo estado no puede ser nulo");
        }
        if (ahora == null) {
            throw new IllegalArgumentException("El timestamp 'ahora' no puede ser nulo");
        }

        Set<EstadoAlerta> permitidos = TRANSICIONES_VALIDAS.getOrDefault(this.estado, EnumSet.noneOf(EstadoAlerta.class));
        if (!permitidos.contains(nuevoEstado)) {
            throw new IllegalStateException(String.format(
                    "Transición de estado no permitida: %s -> %s", this.estado, nuevoEstado));
        }

        String estadoAnteriorNombre = this.estado.name();
        this.estado = nuevoEstado;
        this.actualizadaEn = Optional.of(ahora);

        this.historial.add(new AlertaHistorial(
                UUID.randomUUID(),
                this.id,
                estadoAnteriorNombre,
                nuevoEstado.name(),
                comentario,
                cambiadoPorUsuarioId,
                ahora
        ));

        recordDomainEvent(new AlertaEstadoCambiadoEvent(
                this.id.value(),
                ahora,
                this.tenantId.value(),
                estadoAnteriorNombre,
                nuevoEstado.name()
        ));
    }

    /**
     * Registers a validation result for this alert, fires {@link AlertaValidadaEvent},
     * and updates {@code actualizadaEn}.
     */
    public void registrarValidacion(ValidacionAlerta validacion, Instant ahora) {
        if (validacion == null) {
            throw new IllegalArgumentException("La validación no puede ser nula");
        }
        if (ahora == null) {
            throw new IllegalArgumentException("El timestamp 'ahora' no puede ser nulo");
        }

        this.validacion = Optional.of(validacion);
        this.actualizadaEn = Optional.of(ahora);

        recordDomainEvent(new AlertaValidadaEvent(
                this.id.value(),
                ahora,
                this.tenantId.value(),
                validacion.resultado()
        ));
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    @Override
    public AlertaId getId() {
        return id;
    }

    public TenantId getTenantId() {
        return tenantId;
    }

    public Optional<CiudadanoId> getCiudadanoId() {
        return ciudadanoId;
    }

    public DistritoExternoId getDistritoExternoId() {
        return distritoExternoId;
    }

    public Optional<ZonaExternoId> getZonaExternoId() {
        return zonaExternoId;
    }

    public String getTitulo() {
        return titulo;
    }

    public Optional<String> getDescripcion() {
        return descripcion;
    }

    public Coordenadas getUbicacion() {
        return ubicacion;
    }

    public VolumenEstimado getVolumenEstimado() {
        return volumenEstimado;
    }

    public NivelCriticidad getNivelCriticidad() {
        return nivelCriticidad;
    }

    public FuenteAlerta getFuente() {
        return fuente;
    }

    public EstadoAlerta getEstado() {
        return estado;
    }

    /**
     * Returns an unmodifiable snapshot of the photos list.
     */
    public List<AlertaFoto> getFotos() {
        return List.copyOf(fotos);
    }

    /**
     * Returns an unmodifiable snapshot of the state history.
     */
    public List<AlertaHistorial> getHistorial() {
        return List.copyOf(historial);
    }

    public Optional<ValidacionAlerta> getValidacion() {
        return validacion;
    }

    public Instant getRegistradaEn() {
        return registradaEn;
    }

    public Optional<Instant> getActualizadaEn() {
        return actualizadaEn;
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private static void requireNonNull(Object value, String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }
    }

    private static void requireNonNullOrBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }
}
