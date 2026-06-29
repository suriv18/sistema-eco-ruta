package pe.edu.unmsm.ciudadsana.rutas.domain.model;

import pe.edu.unmsm.ciudadsana.rutas.domain.enums.EstadoParada;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.ContenedorExternoId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.ParadaId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.RutaVersionId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.ZonaExternoId;

import java.time.Instant;
import java.util.Optional;

public class RutaParada {

    private final ParadaId id;
    private final RutaVersionId rutaVersionId;
    private final ZonaExternoId zonaId;
    private final Optional<ContenedorExternoId> contenedorId;
    private final int orden;
    private Optional<Instant> eta;
    private Optional<Instant> horaLlegadaReal;
    private Optional<Instant> horaSalidaReal;
    private double demandaEstimadaKg;
    private double cargaAcumuladaKg;
    private EstadoParada estado;
    private final Instant creadoEn;
    private Optional<Instant> actualizadoEn;

    private RutaParada(
            ParadaId id,
            RutaVersionId rutaVersionId,
            ZonaExternoId zonaId,
            Optional<ContenedorExternoId> contenedorId,
            int orden,
            Optional<Instant> eta,
            Optional<Instant> horaLlegadaReal,
            Optional<Instant> horaSalidaReal,
            double demandaEstimadaKg,
            double cargaAcumuladaKg,
            EstadoParada estado,
            Instant creadoEn,
            Optional<Instant> actualizadoEn
    ) {
        this.id = id;
        this.rutaVersionId = rutaVersionId;
        this.zonaId = zonaId;
        this.contenedorId = contenedorId;
        this.orden = orden;
        this.eta = eta;
        this.horaLlegadaReal = horaLlegadaReal;
        this.horaSalidaReal = horaSalidaReal;
        this.demandaEstimadaKg = demandaEstimadaKg;
        this.cargaAcumuladaKg = cargaAcumuladaKg;
        this.estado = estado;
        this.creadoEn = creadoEn;
        this.actualizadoEn = actualizadoEn;
    }

    public static RutaParada create(
            ParadaId id,
            RutaVersionId rutaVersionId,
            ZonaExternoId zonaId,
            ContenedorExternoId contenedorId,
            int orden,
            Instant eta,
            double demandaEstimadaKg,
            Instant creadoEn
    ) {
        return new RutaParada(
                id,
                rutaVersionId,
                zonaId,
                Optional.ofNullable(contenedorId),
                orden,
                Optional.ofNullable(eta),
                Optional.empty(),
                Optional.empty(),
                demandaEstimadaKg,
                0.0,
                EstadoParada.PENDIENTE,
                creadoEn,
                Optional.empty()
        );
    }

    public static RutaParada reconstitute(
            ParadaId id,
            RutaVersionId rutaVersionId,
            ZonaExternoId zonaId,
            Optional<ContenedorExternoId> contenedorId,
            int orden,
            Optional<Instant> eta,
            Optional<Instant> horaLlegadaReal,
            Optional<Instant> horaSalidaReal,
            double demandaEstimadaKg,
            double cargaAcumuladaKg,
            EstadoParada estado,
            Instant creadoEn,
            Optional<Instant> actualizadoEn
    ) {
        return new RutaParada(
                id,
                rutaVersionId,
                zonaId,
                contenedorId,
                orden,
                eta,
                horaLlegadaReal,
                horaSalidaReal,
                demandaEstimadaKg,
                cargaAcumuladaKg,
                estado,
                creadoEn,
                actualizadoEn
        );
    }

    public void iniciarAtencion() {
        if (estado != EstadoParada.PENDIENTE) {
            throw new IllegalStateException(
                    "Solo se puede iniciar atención de una parada en estado PENDIENTE, estado actual: " + estado);
        }
        this.estado = EstadoParada.EN_ATENCION;
    }

    public void marcarAtendida(Instant horaLlegada, Instant horaSalida) {
        this.horaLlegadaReal = Optional.ofNullable(horaLlegada);
        this.horaSalidaReal = Optional.ofNullable(horaSalida);
        this.estado = EstadoParada.ATENDIDA;
    }

    public void marcarOmitida() {
        this.estado = EstadoParada.OMITIDA;
    }

    public ParadaId getId() {
        return id;
    }

    public RutaVersionId getRutaVersionId() {
        return rutaVersionId;
    }

    public ZonaExternoId getZonaId() {
        return zonaId;
    }

    public Optional<ContenedorExternoId> getContenedorId() {
        return contenedorId;
    }

    public int getOrden() {
        return orden;
    }

    public Optional<Instant> getEta() {
        return eta;
    }

    public Optional<Instant> getHoraLlegadaReal() {
        return horaLlegadaReal;
    }

    public Optional<Instant> getHoraSalidaReal() {
        return horaSalidaReal;
    }

    public double getDemandaEstimadaKg() {
        return demandaEstimadaKg;
    }

    public double getCargaAcumuladaKg() {
        return cargaAcumuladaKg;
    }

    public EstadoParada getEstado() {
        return estado;
    }

    public Instant getCreadoEn() {
        return creadoEn;
    }

    public Optional<Instant> getActualizadoEn() {
        return actualizadoEn;
    }
}
