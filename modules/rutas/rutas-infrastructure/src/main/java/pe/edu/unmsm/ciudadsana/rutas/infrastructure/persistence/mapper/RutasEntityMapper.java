package pe.edu.unmsm.ciudadsana.rutas.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;
import pe.edu.unmsm.ciudadsana.rutas.domain.enums.EstadoParada;
import pe.edu.unmsm.ciudadsana.rutas.domain.enums.EstadoRuta;
import pe.edu.unmsm.ciudadsana.rutas.domain.enums.GeneradoPor;
import pe.edu.unmsm.ciudadsana.rutas.domain.enums.MotivoVersion;
import pe.edu.unmsm.ciudadsana.rutas.domain.enums.TipoEventoRuta;
import pe.edu.unmsm.ciudadsana.rutas.domain.enums.TipoRuta;
import pe.edu.unmsm.ciudadsana.rutas.domain.model.Ruta;
import pe.edu.unmsm.ciudadsana.rutas.domain.model.RutaEvento;
import pe.edu.unmsm.ciudadsana.rutas.domain.model.RutaParada;
import pe.edu.unmsm.ciudadsana.rutas.domain.model.RutaVersion;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.AlertaExternoId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.ContenedorExternoId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.DepositoExternoId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.DistritoExternoId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.MetricasRuta;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.ParadaId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.RutaEventoId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.RutaId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.RutaVersionId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.TurnoExternoId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.ZonaExternoId;
import pe.edu.unmsm.ciudadsana.rutas.infrastructure.persistence.entity.RutaEventoJpaEntity;
import pe.edu.unmsm.ciudadsana.rutas.infrastructure.persistence.entity.RutaJpaEntity;
import pe.edu.unmsm.ciudadsana.rutas.infrastructure.persistence.entity.RutaParadaJpaEntity;
import pe.edu.unmsm.ciudadsana.rutas.infrastructure.persistence.entity.RutaVersionJpaEntity;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface RutasEntityMapper {

    default RutaVersion toDomain(RutaVersionJpaEntity e, List<RutaParadaJpaEntity> paradaEntities) {
        List<RutaParada> paradas = paradaEntities.stream()
                .map(this::toDomain)
                .toList();
        return RutaVersion.reconstitute(
                RutaVersionId.of(e.getId()),
                RutaId.of(e.getRutaId()),
                e.getVersion(),
                MotivoVersion.valueOf(e.getMotivo()),
                Optional.ofNullable(e.getAlertaIdExterno()).map(AlertaExternoId::of),
                GeneradoPor.valueOf(e.getGeneradoPor()),
                MetricasRuta.of(e.getDistanciaTotalM(), e.getDuracionTotalS(), e.getCargaTotalKg()),
                paradas,
                e.getCreadoEn());
    }

    default RutaVersionJpaEntity toEntity(RutaVersion v) {
        RutaVersionJpaEntity e = new RutaVersionJpaEntity();
        e.setId(v.getId().value());
        e.setTenantId(v.getRutaId().value());
        e.setRutaId(v.getRutaId().value());
        e.setVersion(v.getVersion());
        e.setMotivo(v.getMotivo().name());
        e.setAlertaIdExterno(v.getAlertaIdExterno().map(AlertaExternoId::value).orElse(null));
        e.setGeneradoPor(v.getGeneradoPor().name());
        e.setDistanciaTotalM(v.getMetricas().distanciaM());
        e.setDuracionTotalS(v.getMetricas().duracionS());
        e.setCargaTotalKg(v.getMetricas().cargaKg());
        return e;
    }

    default RutaParada toDomain(RutaParadaJpaEntity e) {
        return RutaParada.reconstitute(
                ParadaId.of(e.getId()),
                RutaVersionId.of(e.getRutaVersionId()),
                ZonaExternoId.of(e.getZonaId()),
                Optional.ofNullable(e.getContenedorId()).map(ContenedorExternoId::of),
                e.getOrden(),
                Optional.ofNullable(e.getEta()),
                Optional.ofNullable(e.getHoraLlegadaReal()),
                Optional.ofNullable(e.getHoraSalidaReal()),
                e.getDemandaEstimadaKg(),
                e.getCargaAcumuladaKg(),
                EstadoParada.valueOf(e.getEstado()),
                e.getCreadoEn(),
                Optional.ofNullable(e.getActualizadoEn()));
    }

    default RutaParadaJpaEntity toEntity(RutaParada p) {
        RutaParadaJpaEntity e = new RutaParadaJpaEntity();
        e.setId(p.getId().value());
        e.setRutaVersionId(p.getRutaVersionId().value());
        e.setZonaId(p.getZonaId().value());
        e.setContenedorId(p.getContenedorId().map(ContenedorExternoId::value).orElse(null));
        e.setOrden(p.getOrden());
        e.setEta(p.getEta().orElse(null));
        e.setHoraLlegadaReal(p.getHoraLlegadaReal().orElse(null));
        e.setHoraSalidaReal(p.getHoraSalidaReal().orElse(null));
        e.setDemandaEstimadaKg(p.getDemandaEstimadaKg());
        e.setCargaAcumuladaKg(p.getCargaAcumuladaKg());
        e.setEstado(p.getEstado().name());
        e.setActualizadoEn(Instant.now());
        return e;
    }

    default RutaEvento toDomain(RutaEventoJpaEntity e) {
        return RutaEvento.reconstitute(
                RutaEventoId.of(e.getId()),
                RutaId.of(e.getRutaId()),
                TipoEventoRuta.valueOf(e.getTipoEvento()),
                e.getDescripcion(),
                e.getDatosJson(),
                e.getCreadoEn());
    }

    default RutaEventoJpaEntity toEntity(RutaEvento ev) {
        RutaEventoJpaEntity e = new RutaEventoJpaEntity();
        e.setId(ev.getId().value());
        e.setTenantId(ev.getRutaId().value());
        e.setRutaId(ev.getRutaId().value());
        e.setTipoEvento(ev.getTipoEvento().name());
        e.setDescripcion(ev.getDescripcion().orElse(null));
        e.setDatosJson(ev.getDatosJson().orElse(null));
        return e;
    }

    default Ruta toDomain(RutaJpaEntity e,
                          List<RutaVersionJpaEntity> versionEntities,
                          Map<UUID, List<RutaParadaJpaEntity>> paradasByVersion,
                          List<RutaEventoJpaEntity> eventoEntities) {
        List<RutaVersion> versiones = versionEntities.stream()
                .map(v -> toDomain(v, paradasByVersion.getOrDefault(v.getId(), List.of())))
                .toList();
        RutaVersion versionActual = versiones.isEmpty() ? null : versiones.get(versiones.size() - 1);
        List<RutaEvento> eventos = eventoEntities.stream()
                .map(this::toDomain)
                .toList();
        return Ruta.reconstitute(
                RutaId.of(e.getId()),
                TenantId.of(e.getTenantId()),
                TurnoExternoId.of(e.getTurnoId()),
                DistritoExternoId.of(e.getDistritoId()),
                DepositoExternoId.of(e.getDepositoOrigenId()),
                DepositoExternoId.of(e.getDepositoDestinoId()),
                e.getFecha(),
                TipoRuta.valueOf(e.getTipoRuta()),
                EstadoRuta.valueOf(e.getEstado()),
                MetricasRuta.of(e.getDistanciaTotalM(), e.getDuracionTotalS(), e.getCargaTotalKg()),
                versionActual,
                versiones,
                eventos,
                e.getCreadoEn(),
                e.getActualizadoEn());
    }

    default RutaJpaEntity toEntity(Ruta r) {
        RutaJpaEntity e = new RutaJpaEntity();
        e.setId(r.getId().value());
        e.setTenantId(r.getTenantId().value());
        e.setTurnoId(r.getTurnoId().value());
        e.setDistritoId(r.getDistritoId().value());
        e.setDepositoOrigenId(r.getDepositoOrigenId().value());
        e.setDepositoDestinoId(r.getDepositoDestinoId().value());
        e.setFecha(r.getFecha());
        e.setTipoRuta(r.getTipoRuta().name());
        e.setEstado(r.getEstado().name());
        e.setDistanciaTotalM(r.getMetricas().distanciaM());
        e.setDuracionTotalS(r.getMetricas().duracionS());
        e.setCargaTotalKg(r.getMetricas().cargaKg());
        e.setActualizadoEn(Instant.now());
        return e;
    }
}
