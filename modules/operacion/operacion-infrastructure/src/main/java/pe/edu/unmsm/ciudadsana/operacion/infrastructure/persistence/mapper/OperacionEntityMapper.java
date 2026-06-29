package pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoContenedor;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoChofer;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoDeposito;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoDistrito;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoOperativoUnidad;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoZona;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoTurno;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.TipoDeposito;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.TipoTurno;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.TipoUnidad;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.TipoZona;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Chofer;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Contenedor;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Deposito;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Distrito;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Turno;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Unidad;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Zona;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.CapacidadKg;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.CapacidadM3;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ChoferId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.CodigoZona;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ContenedorId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DepositoId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DistritoId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.Placa;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.PrioridadBase;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.TurnoId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.UnidadId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ZonaId;
import pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.entity.ChoferJpaEntity;
import pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.entity.ContenedorJpaEntity;
import pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.entity.DepositoJpaEntity;
import pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.entity.DistritoJpaEntity;
import pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.entity.TurnoJpaEntity;
import pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.entity.UnidadJpaEntity;
import pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.entity.ZonaJpaEntity;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface OperacionEntityMapper {

    default Distrito toDomain(DistritoJpaEntity e) {
        return Distrito.reconstitute(
                DistritoId.of(e.getId()),
                TenantId.of(e.getTenantId()),
                e.getNombre(),
                e.getUbigeo(),
                EstadoDistrito.valueOf(e.getEstado()),
                e.getCreadoEn()
        );
    }

    default DistritoJpaEntity toEntity(Distrito d) {
        DistritoJpaEntity e = new DistritoJpaEntity();
        e.setId(d.getId().value());
        e.setTenantId(d.getTenantId().value());
        e.setNombre(d.getNombre());
        e.setUbigeo(d.getUbigeo().orElse(null));
        e.setEstado(d.getEstado().name());
        e.setActualizadoEn(Instant.now());
        return e;
    }

    default Zona toDomain(ZonaJpaEntity e) {
        return Zona.reconstitute(
                ZonaId.of(e.getId()),
                TenantId.of(e.getTenantId()),
                DistritoId.of(e.getDistritoId()),
                CodigoZona.of(e.getCodigo()),
                e.getNombre(),
                TipoZona.valueOf(e.getTipoZona()),
                new PrioridadBase(e.getPrioridadBase()),
                EstadoZona.valueOf(e.getEstado()),
                e.getCreadoEn()
        );
    }

    default ZonaJpaEntity toEntity(Zona z) {
        ZonaJpaEntity e = new ZonaJpaEntity();
        e.setId(z.getId().value());
        e.setTenantId(z.getTenantId().value());
        e.setDistritoId(z.getDistritoId().value());
        e.setCodigo(z.getCodigo().value());
        e.setNombre(z.getNombre());
        e.setTipoZona(z.getTipo().name());
        e.setPrioridadBase(z.getPrioridad().value());
        e.setEstado(z.getEstado().name());
        e.setActualizadoEn(Instant.now());
        return e;
    }

    default Deposito toDomain(DepositoJpaEntity e) {
        return Deposito.reconstitute(
                DepositoId.of(e.getId()),
                TenantId.of(e.getTenantId()),
                DistritoId.of(e.getDistritoId()),
                e.getNombre(),
                TipoDeposito.valueOf(e.getTipo()),
                EstadoDeposito.valueOf(e.getEstado()),
                e.getCreadoEn()
        );
    }

    default DepositoJpaEntity toEntity(Deposito d) {
        DepositoJpaEntity e = new DepositoJpaEntity();
        e.setId(d.getId().value());
        e.setTenantId(d.getTenantId().value());
        e.setDistritoId(d.getDistritoId().value());
        e.setNombre(d.getNombre());
        e.setTipo(d.getTipo().name());
        e.setEstado(d.getEstado().name());
        e.setActualizadoEn(Instant.now());
        return e;
    }

    default Unidad toDomain(UnidadJpaEntity e) {
        return Unidad.reconstitute(
                UnidadId.of(e.getId()),
                TenantId.of(e.getTenantId()),
                Placa.of(e.getPlaca()),
                e.getCodigoInterno(),
                TipoUnidad.valueOf(e.getTipoUnidad()),
                new CapacidadM3(e.getCapacidadM3()),
                new CapacidadKg(e.getCapacidadKg()),
                EstadoOperativoUnidad.valueOf(e.getEstadoOperativo()),
                e.getCreadoEn()
        );
    }

    default UnidadJpaEntity toEntity(Unidad u) {
        UnidadJpaEntity e = new UnidadJpaEntity();
        e.setId(u.getId().value());
        e.setTenantId(u.getTenantId().value());
        e.setPlaca(u.getPlaca().value());
        e.setCodigoInterno(u.getCodigoInterno());
        e.setTipoUnidad(u.getTipo().name());
        e.setCapacidadM3(u.getCapacidadM3().value());
        e.setCapacidadKg(u.getCapacidadKg().value());
        e.setEstadoOperativo(u.getEstadoOperativo().name());
        e.setEstado("ACTIVO");
        e.setActualizadoEn(Instant.now());
        return e;
    }

    default Chofer toDomain(ChoferJpaEntity e) {
        return Chofer.reconstitute(
                ChoferId.of(e.getId()),
                TenantId.of(e.getTenantId()),
                e.getNombres(),
                e.getApellidos(),
                e.getDni(),
                e.getLicencia(),
                e.getTelefono(),
                EstadoChofer.valueOf(e.getEstado()),
                e.getCreadoEn()
        );
    }

    default ChoferJpaEntity toEntity(Chofer c) {
        ChoferJpaEntity e = new ChoferJpaEntity();
        e.setId(c.getId().value());
        e.setTenantId(c.getTenantId().value());
        e.setNombres(c.getNombres());
        e.setApellidos(c.getApellidos());
        e.setDni(c.getDni().orElse(null));
        e.setLicencia(c.getLicencia().orElse(null));
        e.setTelefono(c.getTelefono().orElse(null));
        e.setEstado(c.getEstado().name());
        e.setActualizadoEn(Instant.now());
        return e;
    }

    default Contenedor toDomain(ContenedorJpaEntity e) {
        return Contenedor.reconstitute(
                ContenedorId.of(e.getId()),
                TenantId.of(e.getTenantId()),
                ZonaId.of(e.getZonaId()),
                e.getCodigo(),
                new CapacidadM3(e.getCapacidadM3()),
                EstadoContenedor.valueOf(e.getEstadoContenedor()),
                e.getCreadoEn()
        );
    }

    default ContenedorJpaEntity toEntity(Contenedor c) {
        ContenedorJpaEntity e = new ContenedorJpaEntity();
        e.setId(c.getId().value());
        e.setTenantId(c.getTenantId().value());
        e.setZonaId(c.getZonaId().value());
        e.setCodigo(c.getCodigo());
        e.setCapacidadM3(c.getCapacidad().value());
        e.setEstadoContenedor(c.getEstadoContenedor().name());
        e.setEstado("ACTIVO");
        e.setActualizadoEn(Instant.now());
        return e;
    }

    default Turno toDomain(TurnoJpaEntity e) {
        return Turno.reconstitute(
                TurnoId.of(e.getId()),
                TenantId.of(e.getTenantId()),
                UnidadId.of(e.getUnidadId()),
                ChoferId.of(e.getChoferId()),
                DistritoId.of(e.getDistritoId()),
                e.getFecha(),
                e.getHoraInicio(),
                e.getHoraFin(),
                TipoTurno.valueOf(e.getTipoTurno()),
                EstadoTurno.valueOf(e.getEstado()),
                e.getCreadoEn()
        );
    }

    default TurnoJpaEntity toEntity(Turno t) {
        TurnoJpaEntity e = new TurnoJpaEntity();
        e.setId(t.getId().value());
        e.setTenantId(t.getTenantId().value());
        e.setUnidadId(t.getUnidadId().value());
        e.setChoferId(t.getChoferId().value());
        e.setDistritoId(t.getDistritoId().value());
        e.setFecha(t.getFecha());
        e.setHoraInicio(t.getHoraInicio());
        e.setHoraFin(t.getHoraFin());
        e.setTipoTurno(t.getTipo().name());
        e.setEstado(t.getEstado().name());
        e.setActualizadoEn(Instant.now());
        return e;
    }
}
