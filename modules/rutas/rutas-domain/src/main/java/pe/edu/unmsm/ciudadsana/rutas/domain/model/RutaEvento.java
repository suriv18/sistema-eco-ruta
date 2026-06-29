package pe.edu.unmsm.ciudadsana.rutas.domain.model;

import pe.edu.unmsm.ciudadsana.rutas.domain.enums.TipoEventoRuta;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.RutaEventoId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.RutaId;

import java.time.Instant;
import java.util.Optional;

public class RutaEvento {

    private final RutaEventoId id;
    private final RutaId rutaId;
    private final TipoEventoRuta tipoEvento;
    private final Optional<String> descripcion;
    private final Optional<String> datosJson;
    private final Instant creadoEn;

    private RutaEvento(
            RutaEventoId id,
            RutaId rutaId,
            TipoEventoRuta tipoEvento,
            Optional<String> descripcion,
            Optional<String> datosJson,
            Instant creadoEn
    ) {
        this.id = id;
        this.rutaId = rutaId;
        this.tipoEvento = tipoEvento;
        this.descripcion = descripcion;
        this.datosJson = datosJson;
        this.creadoEn = creadoEn;
    }

    public static RutaEvento create(
            RutaEventoId id,
            RutaId rutaId,
            TipoEventoRuta tipoEvento,
            String descripcion,
            String datosJson,
            Instant creadoEn
    ) {
        return new RutaEvento(
                id,
                rutaId,
                tipoEvento,
                Optional.ofNullable(descripcion),
                Optional.ofNullable(datosJson),
                creadoEn
        );
    }

    public static RutaEvento reconstitute(
            RutaEventoId id,
            RutaId rutaId,
            TipoEventoRuta tipoEvento,
            String descripcion,
            String datosJson,
            Instant creadoEn
    ) {
        return new RutaEvento(
                id,
                rutaId,
                tipoEvento,
                Optional.ofNullable(descripcion),
                Optional.ofNullable(datosJson),
                creadoEn
        );
    }

    public RutaEventoId getId() {
        return id;
    }

    public RutaId getRutaId() {
        return rutaId;
    }

    public TipoEventoRuta getTipoEvento() {
        return tipoEvento;
    }

    public Optional<String> getDescripcion() {
        return descripcion;
    }

    public Optional<String> getDatosJson() {
        return datosJson;
    }

    public Instant getCreadoEn() {
        return creadoEn;
    }
}
