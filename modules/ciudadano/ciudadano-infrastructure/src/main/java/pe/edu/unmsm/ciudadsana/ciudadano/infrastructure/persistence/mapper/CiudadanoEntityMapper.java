package pe.edu.unmsm.ciudadsana.ciudadano.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.enums.EstadoAlerta;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.enums.EstadoCiudadano;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.enums.FuenteAlerta;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.enums.NivelCriticidad;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.enums.VolumenEstimado;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.model.AlertaCiudadana;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.model.Ciudadano;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.AlertaFoto;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.AlertaFotoId;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.AlertaHistorial;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.AlertaId;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.CiudadanoId;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.Coordenadas;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.DistritoExternoId;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.ValidacionAlerta;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.ValidacionId;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.ZonaExternoId;
import pe.edu.unmsm.ciudadsana.ciudadano.infrastructure.persistence.entity.AlertaCiudadanaJpaEntity;
import pe.edu.unmsm.ciudadsana.ciudadano.infrastructure.persistence.entity.AlertaFotoJpaEntity;
import pe.edu.unmsm.ciudadsana.ciudadano.infrastructure.persistence.entity.AlertaHistorialJpaEntity;
import pe.edu.unmsm.ciudadsana.ciudadano.infrastructure.persistence.entity.CiudadanoJpaEntity;
import pe.edu.unmsm.ciudadsana.ciudadano.infrastructure.persistence.entity.ValidacionAlertaJpaEntity;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CiudadanoEntityMapper {

    default Ciudadano toDomain(CiudadanoJpaEntity e) {
        return Ciudadano.reconstitute(
                CiudadanoId.of(e.getId()),
                TenantId.of(e.getTenantId()),
                e.getNombres(),
                e.getApellidos(),
                e.getEmail(),
                e.getTelefono(),
                e.getDocumento(),
                EstadoCiudadano.valueOf(e.getEstado()),
                e.getCreadoEn()
        );
    }

    default CiudadanoJpaEntity toEntity(Ciudadano c) {
        CiudadanoJpaEntity e = new CiudadanoJpaEntity();
        e.setId(c.getId().value());
        e.setTenantId(c.getTenantId().value());
        e.setNombres(c.getNombres().orElse(null));
        e.setApellidos(c.getApellidos().orElse(null));
        e.setEmail(c.getEmail().orElse(null));
        e.setTelefono(c.getTelefono().orElse(null));
        e.setDocumento(c.getDocumento().orElse(null));
        e.setEstado(c.getEstado().name());
        e.setActualizadoEn(Instant.now());
        return e;
    }

    default AlertaCiudadanaJpaEntity toEntity(AlertaCiudadana a) {
        AlertaCiudadanaJpaEntity e = new AlertaCiudadanaJpaEntity();
        e.setId(a.getId().value());
        e.setTenantId(a.getTenantId().value());
        e.setCiudadanoId(a.getCiudadanoId().map(CiudadanoId::value).orElse(null));
        e.setDistritoIdExterno(a.getDistritoExternoId().value());
        e.setZonaIdExterno(a.getZonaExternoId().map(ZonaExternoId::value).orElse(null));
        e.setTitulo(a.getTitulo());
        e.setDescripcion(a.getDescripcion().orElse(null));
        e.setLatitud(a.getUbicacion().latitud());
        e.setLongitud(a.getUbicacion().longitud());
        e.setVolumenEstimado(a.getVolumenEstimado().name());
        e.setNivelCriticidad(a.getNivelCriticidad().name());
        e.setFuente(a.getFuente().name());
        e.setEstado(a.getEstado().name());
        e.setActualizadoEn(a.getActualizadaEn().orElse(null));
        return e;
    }

    default AlertaCiudadana toDomain(
            AlertaCiudadanaJpaEntity e,
            List<AlertaFotoJpaEntity> fotoEntities,
            List<AlertaHistorialJpaEntity> historialEntities,
            Optional<ValidacionAlertaJpaEntity> validacionEntity
    ) {
        List<AlertaFoto> fotos = fotoEntities.stream().map(this::toDomain).toList();
        List<AlertaHistorial> historial = historialEntities.stream().map(this::toDomain).toList();
        ValidacionAlerta validacion = validacionEntity.map(this::toDomain).orElse(null);

        return AlertaCiudadana.reconstitute(
                AlertaId.of(e.getId()),
                TenantId.of(e.getTenantId()),
                e.getCiudadanoId() != null ? CiudadanoId.of(e.getCiudadanoId()) : null,
                DistritoExternoId.of(e.getDistritoIdExterno()),
                e.getZonaIdExterno() != null ? ZonaExternoId.of(e.getZonaIdExterno()) : null,
                e.getTitulo(),
                e.getDescripcion(),
                Coordenadas.of(e.getLatitud() != null ? e.getLatitud() : 0.0,
                               e.getLongitud() != null ? e.getLongitud() : 0.0),
                VolumenEstimado.valueOf(e.getVolumenEstimado()),
                NivelCriticidad.valueOf(e.getNivelCriticidad()),
                FuenteAlerta.valueOf(e.getFuente()),
                EstadoAlerta.valueOf(e.getEstado()),
                fotos,
                historial,
                validacion,
                e.getCreadoEn(),
                e.getActualizadoEn()
        );
    }

    default AlertaFoto toDomain(AlertaFotoJpaEntity e) {
        return new AlertaFoto(
                AlertaFotoId.of(e.getId()),
                AlertaId.of(e.getAlertaId()),
                e.getUrlArchivo(),
                e.getTipoMime(),
                e.getTamanioBytes()
        );
    }

    default AlertaFotoJpaEntity toEntity(AlertaFoto f, UUID alertaId) {
        AlertaFotoJpaEntity e = new AlertaFotoJpaEntity();
        e.setId(f.id().value());
        e.setAlertaId(alertaId);
        e.setUrlArchivo(f.urlArchivo());
        e.setTipoMime(f.tipoMime());
        e.setTamanioBytes(f.tamanioBytes());
        return e;
    }

    default AlertaHistorial toDomain(AlertaHistorialJpaEntity e) {
        return new AlertaHistorial(
                e.getId(),
                AlertaId.of(e.getAlertaId()),
                e.getEstadoAnterior(),
                e.getEstadoNuevo(),
                e.getComentario(),
                e.getCambiadoPorUsuarioId(),
                e.getCambiadoEn()
        );
    }

    default AlertaHistorialJpaEntity toEntity(AlertaHistorial h) {
        AlertaHistorialJpaEntity e = new AlertaHistorialJpaEntity();
        e.setId(h.historialId());
        e.setAlertaId(h.alertaId().value());
        e.setEstadoAnterior(h.estadoAnterior());
        e.setEstadoNuevo(h.estadoNuevo());
        e.setComentario(h.comentario());
        e.setCambiadoPorUsuarioId(h.cambiadoPorUsuarioId());
        e.setCambiadoEn(h.cambiadoEn());
        return e;
    }

    default ValidacionAlerta toDomain(ValidacionAlertaJpaEntity e) {
        return new ValidacionAlerta(
                ValidacionId.of(e.getId()),
                AlertaId.of(e.getAlertaId()),
                e.isEsDuplicada(),
                e.getAlertaOriginalId() != null ? AlertaId.of(e.getAlertaOriginalId()) : null,
                e.isDentroGeocerca(),
                e.getScoreSpam(),
                e.getResultado(),
                e.getValidadaEn()
        );
    }

    default ValidacionAlertaJpaEntity toEntity(ValidacionAlerta v) {
        ValidacionAlertaJpaEntity e = new ValidacionAlertaJpaEntity();
        e.setId(v.id().value());
        e.setAlertaId(v.alertaId().value());
        e.setEsDuplicada(v.esDuplicada());
        e.setAlertaOriginalId(v.alertaOriginalId() != null ? v.alertaOriginalId().value() : null);
        e.setDentroGeocerca(v.dentroGeocerca());
        e.setScoreSpam(v.scoreSpam());
        e.setResultado(v.resultado());
        e.setValidadaEn(v.validadaEn());
        return e;
    }
}
