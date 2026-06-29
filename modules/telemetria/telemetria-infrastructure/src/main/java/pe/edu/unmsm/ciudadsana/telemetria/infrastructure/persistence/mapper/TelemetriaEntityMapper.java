package pe.edu.unmsm.ciudadsana.telemetria.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.DesvioRutaPersistencePort;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.EstadoUnidadPersistencePort;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.EventoConectividadPersistencePort;
import pe.edu.unmsm.ciudadsana.telemetria.domain.enums.EstadoDispositivo;
import pe.edu.unmsm.ciudadsana.telemetria.domain.enums.OrigenPing;
import pe.edu.unmsm.ciudadsana.telemetria.domain.model.DispositivoGps;
import pe.edu.unmsm.ciudadsana.telemetria.domain.model.PingGps;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.Coordenadas;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.DispositivoId;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.PingId;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.RutaExternoId;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.UnidadExternoId;
import pe.edu.unmsm.ciudadsana.telemetria.infrastructure.persistence.entity.DesvioRutaJpaEntity;
import pe.edu.unmsm.ciudadsana.telemetria.infrastructure.persistence.entity.DispositivoGpsJpaEntity;
import pe.edu.unmsm.ciudadsana.telemetria.infrastructure.persistence.entity.EstadoUnidadJpaEntity;
import pe.edu.unmsm.ciudadsana.telemetria.infrastructure.persistence.entity.EventoConectividadJpaEntity;
import pe.edu.unmsm.ciudadsana.telemetria.infrastructure.persistence.entity.PingGpsJpaEntity;

import java.time.Instant;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface TelemetriaEntityMapper {

    // -------------------------------------------------------------------------
    // DispositivoGps
    // -------------------------------------------------------------------------

    default DispositivoGps toDomain(DispositivoGpsJpaEntity e) {
        return DispositivoGps.reconstitute(
                DispositivoId.of(e.getId()),
                TenantId.of(e.getTenantId()),
                UnidadExternoId.of(e.getUnidadExternoId()),
                Optional.ofNullable(e.getImei()),
                Optional.ofNullable(e.getProveedor()),
                EstadoDispositivo.valueOf(e.getEstado()),
                Optional.ofNullable(e.getUltimoPingEn()),
                e.getCreadoEn()
        );
    }

    default DispositivoGpsJpaEntity toEntity(DispositivoGps d) {
        DispositivoGpsJpaEntity e = new DispositivoGpsJpaEntity();
        e.setId(d.getId().value());
        e.setTenantId(d.getTenantId().value());
        e.setUnidadExternoId(d.getUnidadExternoId().value());
        e.setImei(d.getImei().orElse(null));
        e.setProveedor(d.getProveedor().orElse(null));
        e.setEstado(d.getEstado().name());
        e.setUltimoPingEn(d.getUltimoPingEn().orElse(null));
        e.setActualizadoEn(Instant.now());
        return e;
    }

    // -------------------------------------------------------------------------
    // PingGps
    // -------------------------------------------------------------------------

    default PingGps toDomain(PingGpsJpaEntity e) {
        return PingGps.reconstitute(
                PingId.of(e.getId()),
                TenantId.of(e.getTenantId()),
                DispositivoId.of(e.getDispositivoId()),
                UnidadExternoId.of(e.getUnidadExternoId()),
                Optional.ofNullable(e.getRutaExternoId()).map(RutaExternoId::of),
                e.getTs(),
                Coordenadas.of(e.getLatitud(), e.getLongitud()),
                Optional.ofNullable(e.getVelocidadKmh()),
                Optional.ofNullable(e.getRumboGrados()),
                Optional.ofNullable(e.getPrecisionM()),
                OrigenPing.valueOf(e.getOrigen()),
                e.getRecibidoEn()
        );
    }

    default PingGpsJpaEntity toEntity(PingGps p) {
        PingGpsJpaEntity e = new PingGpsJpaEntity();
        e.setId(p.getId().value());
        e.setTenantId(p.getTenantId().value());
        e.setDispositivoId(p.getDispositivoId().value());
        e.setUnidadExternoId(p.getUnidadExternoId().value());
        e.setRutaExternoId(p.getRutaExternoId().map(RutaExternoId::value).orElse(null));
        e.setTs(p.getTs());
        e.setLatitud(p.getPosicion().latitud());
        e.setLongitud(p.getPosicion().longitud());
        e.setVelocidadKmh(p.getVelocidadKmh().orElse(null));
        e.setRumboGrados(p.getRumboGrados().orElse(null));
        e.setPrecisionM(p.getPrecisionM().orElse(null));
        e.setOrigen(p.getOrigen().name());
        e.setRecibidoEn(p.getRecibidoEn());
        e.setActualizadoEn(Instant.now());
        return e;
    }

    // -------------------------------------------------------------------------
    // EstadoUnidad (mapped via EstadoUnidadView — no domain object roundtrip)
    // -------------------------------------------------------------------------

    default EstadoUnidadPersistencePort.EstadoUnidadView toView(EstadoUnidadJpaEntity e) {
        return new EstadoUnidadPersistencePort.EstadoUnidadView(
                e.getId(),
                e.getTenantId(),
                e.getUnidadExternoId(),
                e.getRutaExternoId(),
                e.getLatitud(),
                e.getLongitud(),
                e.getUltimaVelocidadKmh(),
                e.getUltimoPingEn(),
                e.getEstadoMovimiento(),
                e.getActualizadoEn()
        );
    }

    default EstadoUnidadJpaEntity toEntity(EstadoUnidadPersistencePort.EstadoUnidadView v) {
        EstadoUnidadJpaEntity e = new EstadoUnidadJpaEntity();
        e.setId(v.id());
        e.setTenantId(v.tenantId());
        e.setUnidadExternoId(v.unidadExternoId());
        e.setRutaExternoId(v.rutaExternoId());
        e.setLatitud(v.latitud());
        e.setLongitud(v.longitud());
        e.setUltimaVelocidadKmh(v.ultimaVelocidadKmh());
        e.setUltimoPingEn(v.ultimoPingEn());
        e.setEstadoMovimiento(v.estadoMovimiento());
        e.setActualizadoEn(Instant.now());
        return e;
    }

    // -------------------------------------------------------------------------
    // DesvioRuta (mapped via DesvioView)
    // -------------------------------------------------------------------------

    default DesvioRutaPersistencePort.DesvioView toView(DesvioRutaJpaEntity e) {
        return new DesvioRutaPersistencePort.DesvioView(
                e.getId(),
                e.getTenantId(),
                e.getUnidadExternoId(),
                e.getRutaExternoId(),
                e.getLatitud(),
                e.getLongitud(),
                e.getDistanciaDesvioM(),
                e.getSeveridad(),
                e.getEstado(),
                e.getDetectadoEn(),
                e.getAtendidoEn()
        );
    }

    default DesvioRutaJpaEntity toEntity(DesvioRutaPersistencePort.DesvioView v) {
        DesvioRutaJpaEntity e = new DesvioRutaJpaEntity();
        e.setId(v.id());
        e.setTenantId(v.tenantId());
        e.setUnidadExternoId(v.unidadExternoId());
        e.setRutaExternoId(v.rutaExternoId());
        e.setLatitud(v.latitud());
        e.setLongitud(v.longitud());
        e.setDistanciaDesvioM(v.distanciaDesvioM());
        e.setSeveridad(v.severidad());
        e.setEstado(v.estado());
        e.setDetectadoEn(v.detectadoEn());
        e.setAtendidoEn(v.atendidoEn());
        e.setActualizadoEn(Instant.now());
        return e;
    }

    // -------------------------------------------------------------------------
    // EventoConectividad (mapped via EventoConectividadView)
    // -------------------------------------------------------------------------

    default EventoConectividadPersistencePort.EventoConectividadView toView(EventoConectividadJpaEntity e) {
        return new EventoConectividadPersistencePort.EventoConectividadView(
                e.getId(),
                e.getTenantId(),
                e.getUnidadExternoId(),
                e.getDispositivoId(),
                e.getTipoEvento(),
                e.getDetalle(),
                e.getDetectadoEn()
        );
    }

    default EventoConectividadJpaEntity toEntity(EventoConectividadPersistencePort.EventoConectividadView v) {
        EventoConectividadJpaEntity e = new EventoConectividadJpaEntity();
        e.setId(v.id());
        e.setTenantId(v.tenantId());
        e.setUnidadExternoId(v.unidadExternoId());
        e.setDispositivoId(v.dispositivoId());
        e.setTipoEvento(v.tipoEvento());
        e.setDetalle(v.detalle());
        e.setDetectadoEn(v.detectadoEn());
        e.setActualizadoEn(Instant.now());
        return e;
    }
}
