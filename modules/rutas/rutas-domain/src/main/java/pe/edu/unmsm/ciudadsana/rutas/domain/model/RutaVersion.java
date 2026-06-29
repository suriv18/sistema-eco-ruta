package pe.edu.unmsm.ciudadsana.rutas.domain.model;

import pe.edu.unmsm.ciudadsana.rutas.domain.enums.GeneradoPor;
import pe.edu.unmsm.ciudadsana.rutas.domain.enums.MotivoVersion;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.AlertaExternoId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.MetricasRuta;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.RutaId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.RutaVersionId;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RutaVersion {

    private final RutaVersionId id;
    private final RutaId rutaId;
    private final int version;
    private final MotivoVersion motivo;
    private final Optional<AlertaExternoId> alertaIdExterno;
    private final GeneradoPor generadoPor;
    private final MetricasRuta metricas;
    private final List<RutaParada> paradas;
    private final Instant creadoEn;

    private RutaVersion(
            RutaVersionId id,
            RutaId rutaId,
            int version,
            MotivoVersion motivo,
            Optional<AlertaExternoId> alertaIdExterno,
            GeneradoPor generadoPor,
            MetricasRuta metricas,
            List<RutaParada> paradas,
            Instant creadoEn
    ) {
        this.id = id;
        this.rutaId = rutaId;
        this.version = version;
        this.motivo = motivo;
        this.alertaIdExterno = alertaIdExterno;
        this.generadoPor = generadoPor;
        this.metricas = metricas;
        this.paradas = paradas;
        this.creadoEn = creadoEn;
    }

    public static RutaVersion create(
            RutaVersionId id,
            RutaId rutaId,
            int version,
            MotivoVersion motivo,
            AlertaExternoId alertaIdExterno,
            GeneradoPor generadoPor,
            MetricasRuta metricas,
            Instant creadoEn
    ) {
        return new RutaVersion(
                id,
                rutaId,
                version,
                motivo,
                Optional.ofNullable(alertaIdExterno),
                generadoPor,
                metricas,
                new ArrayList<>(),
                creadoEn
        );
    }

    public static RutaVersion reconstitute(
            RutaVersionId id,
            RutaId rutaId,
            int version,
            MotivoVersion motivo,
            Optional<AlertaExternoId> alertaIdExterno,
            GeneradoPor generadoPor,
            MetricasRuta metricas,
            List<RutaParada> paradas,
            Instant creadoEn
    ) {
        return new RutaVersion(
                id,
                rutaId,
                version,
                motivo,
                alertaIdExterno,
                generadoPor,
                metricas,
                new ArrayList<>(paradas),
                creadoEn
        );
    }

    public void agregarParada(RutaParada parada) {
        paradas.add(parada);
    }

    public List<RutaParada> getParadas() {
        return List.copyOf(paradas);
    }

    public RutaVersionId getId() {
        return id;
    }

    public RutaId getRutaId() {
        return rutaId;
    }

    public int getVersion() {
        return version;
    }

    public MotivoVersion getMotivo() {
        return motivo;
    }

    public Optional<AlertaExternoId> getAlertaIdExterno() {
        return alertaIdExterno;
    }

    public GeneradoPor getGeneradoPor() {
        return generadoPor;
    }

    public MetricasRuta getMetricas() {
        return metricas;
    }

    public Instant getCreadoEn() {
        return creadoEn;
    }
}
