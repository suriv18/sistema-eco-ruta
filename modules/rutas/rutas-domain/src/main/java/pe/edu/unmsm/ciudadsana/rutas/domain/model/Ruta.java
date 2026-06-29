package pe.edu.unmsm.ciudadsana.rutas.domain.model;

import pe.edu.unmsm.ciudadsana.rutas.domain.enums.EstadoRuta;
import pe.edu.unmsm.ciudadsana.rutas.domain.enums.TipoRuta;
import pe.edu.unmsm.ciudadsana.rutas.domain.event.RutaCreadaEvent;
import pe.edu.unmsm.ciudadsana.rutas.domain.event.RutaEstadoCambiadoEvent;
import pe.edu.unmsm.ciudadsana.rutas.domain.event.RutaVersionAgregadaEvent;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.DepositoExternoId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.DistritoExternoId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.MetricasRuta;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.RutaId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.TurnoExternoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.AggregateRoot;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Ruta extends AggregateRoot<RutaId> {

    private final RutaId id;
    private final TenantId tenantId;
    private final TurnoExternoId turnoId;
    private final DistritoExternoId distritoId;
    private final DepositoExternoId depositoOrigenId;
    private final DepositoExternoId depositoDestinoId;
    private final LocalDate fecha;
    private final TipoRuta tipoRuta;
    private EstadoRuta estado;
    private MetricasRuta metricas;
    private RutaVersion versionActual;
    private final List<RutaVersion> historialVersiones;
    private final List<RutaEvento> eventos;
    private final Instant creadoEn;
    private Optional<Instant> actualizadoEn;

    private Ruta(
            RutaId id,
            TenantId tenantId,
            TurnoExternoId turnoId,
            DistritoExternoId distritoId,
            DepositoExternoId depositoOrigenId,
            DepositoExternoId depositoDestinoId,
            LocalDate fecha,
            TipoRuta tipoRuta,
            EstadoRuta estado,
            MetricasRuta metricas,
            RutaVersion versionActual,
            List<RutaVersion> historialVersiones,
            List<RutaEvento> eventos,
            Instant creadoEn,
            Optional<Instant> actualizadoEn
    ) {
        this.id = id;
        this.tenantId = tenantId;
        this.turnoId = turnoId;
        this.distritoId = distritoId;
        this.depositoOrigenId = depositoOrigenId;
        this.depositoDestinoId = depositoDestinoId;
        this.fecha = fecha;
        this.tipoRuta = tipoRuta;
        this.estado = estado;
        this.metricas = metricas;
        this.versionActual = versionActual;
        this.historialVersiones = historialVersiones;
        this.eventos = eventos;
        this.creadoEn = creadoEn;
        this.actualizadoEn = actualizadoEn;
    }

    public static Ruta create(
            RutaId id,
            TenantId tenantId,
            TurnoExternoId turnoId,
            DistritoExternoId distritoId,
            DepositoExternoId depositoOrigenId,
            DepositoExternoId depositoDestinoId,
            LocalDate fecha,
            TipoRuta tipoRuta,
            Instant creadoEn
    ) {
        Ruta ruta = new Ruta(
                id,
                tenantId,
                turnoId,
                distritoId,
                depositoOrigenId,
                depositoDestinoId,
                fecha,
                tipoRuta,
                EstadoRuta.BORRADOR,
                MetricasRuta.of(0, 0, 0),
                null,
                new ArrayList<>(),
                new ArrayList<>(),
                creadoEn,
                Optional.empty()
        );
        ruta.recordDomainEvent(new RutaCreadaEvent(
                id.value(),
                creadoEn,
                tenantId.value(),
                turnoId.value(),
                fecha
        ));
        return ruta;
    }

    public static Ruta reconstitute(
            RutaId id,
            TenantId tenantId,
            TurnoExternoId turnoId,
            DistritoExternoId distritoId,
            DepositoExternoId depositoOrigenId,
            DepositoExternoId depositoDestinoId,
            LocalDate fecha,
            TipoRuta tipoRuta,
            EstadoRuta estado,
            MetricasRuta metricas,
            RutaVersion versionActual,
            List<RutaVersion> historialVersiones,
            List<RutaEvento> eventos,
            Instant creadoEn,
            Instant actualizadoEn
    ) {
        return new Ruta(
                id,
                tenantId,
                turnoId,
                distritoId,
                depositoOrigenId,
                depositoDestinoId,
                fecha,
                tipoRuta,
                estado,
                metricas,
                versionActual,
                new ArrayList<>(historialVersiones),
                new ArrayList<>(eventos),
                creadoEn,
                Optional.ofNullable(actualizadoEn)
        );
    }

    public void aprobar() {
        if (estado != EstadoRuta.BORRADOR) {
            throw new IllegalStateException(
                    "Solo se puede aprobar una ruta en estado BORRADOR, estado actual: " + estado);
        }
        String estadoAnterior = estado.name();
        this.estado = EstadoRuta.APROBADA;
        recordDomainEvent(new RutaEstadoCambiadoEvent(
                id.value(),
                Instant.now(),
                tenantId.value(),
                estadoAnterior,
                estado.name()
        ));
    }

    public void iniciarEjecucion() {
        if (estado != EstadoRuta.APROBADA) {
            throw new IllegalStateException(
                    "Solo se puede iniciar ejecución de una ruta en estado APROBADA, estado actual: " + estado);
        }
        String estadoAnterior = estado.name();
        this.estado = EstadoRuta.EN_EJECUCION;
        recordDomainEvent(new RutaEstadoCambiadoEvent(
                id.value(),
                Instant.now(),
                tenantId.value(),
                estadoAnterior,
                estado.name()
        ));
    }

    public void finalizar() {
        if (estado != EstadoRuta.EN_EJECUCION) {
            throw new IllegalStateException(
                    "Solo se puede finalizar una ruta en estado EN_EJECUCION, estado actual: " + estado);
        }
        String estadoAnterior = estado.name();
        this.estado = EstadoRuta.FINALIZADA;
        recordDomainEvent(new RutaEstadoCambiadoEvent(
                id.value(),
                Instant.now(),
                tenantId.value(),
                estadoAnterior,
                estado.name()
        ));
    }

    public void cancelar() {
        if (estado != EstadoRuta.BORRADOR && estado != EstadoRuta.APROBADA) {
            throw new IllegalStateException(
                    "Solo se puede cancelar una ruta en estado BORRADOR o APROBADA, estado actual: " + estado);
        }
        String estadoAnterior = estado.name();
        this.estado = EstadoRuta.CANCELADA;
        recordDomainEvent(new RutaEstadoCambiadoEvent(
                id.value(),
                Instant.now(),
                tenantId.value(),
                estadoAnterior,
                estado.name()
        ));
    }

    public void agregarVersion(RutaVersion version) {
        historialVersiones.add(version);
        this.versionActual = version;
        this.metricas = version.getMetricas();
        recordDomainEvent(new RutaVersionAgregadaEvent(
                id.value(),
                Instant.now(),
                tenantId.value(),
                version.getId().value(),
                version.getVersion(),
                version.getMotivo().name()
        ));
    }

    public void registrarEvento(RutaEvento evento) {
        eventos.add(evento);
    }

    @Override
    public RutaId getId() {
        return id;
    }

    public Optional<RutaVersion> getVersionActual() {
        return Optional.ofNullable(versionActual);
    }

    public List<RutaVersion> getHistorialVersiones() {
        return List.copyOf(historialVersiones);
    }

    public List<RutaEvento> getEventos() {
        return List.copyOf(eventos);
    }

    public TenantId getTenantId() {
        return tenantId;
    }

    public TurnoExternoId getTurnoId() {
        return turnoId;
    }

    public DistritoExternoId getDistritoId() {
        return distritoId;
    }

    public DepositoExternoId getDepositoOrigenId() {
        return depositoOrigenId;
    }

    public DepositoExternoId getDepositoDestinoId() {
        return depositoDestinoId;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public TipoRuta getTipoRuta() {
        return tipoRuta;
    }

    public EstadoRuta getEstado() {
        return estado;
    }

    public MetricasRuta getMetricas() {
        return metricas;
    }

    public Instant getCreadoEn() {
        return creadoEn;
    }

    public Optional<Instant> getActualizadoEn() {
        return actualizadoEn;
    }
}
