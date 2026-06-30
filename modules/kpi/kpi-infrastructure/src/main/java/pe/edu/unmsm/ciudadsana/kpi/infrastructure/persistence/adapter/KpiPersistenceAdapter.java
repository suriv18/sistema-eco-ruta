package pe.edu.unmsm.ciudadsana.kpi.infrastructure.persistence.adapter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.kpi.application.dto.KpiAlertaDto;
import pe.edu.unmsm.ciudadsana.kpi.application.dto.KpiRutaDto;
import pe.edu.unmsm.ciudadsana.kpi.application.dto.KpiUnidadDto;
import pe.edu.unmsm.ciudadsana.kpi.application.dto.KpiZonaDto;
import pe.edu.unmsm.ciudadsana.kpi.application.dto.ResumenOperativoDto;
import pe.edu.unmsm.ciudadsana.kpi.application.port.out.KpiPersistencePort;
import pe.edu.unmsm.ciudadsana.kpi.infrastructure.persistence.entity.KpiAlertaJpaEntity;
import pe.edu.unmsm.ciudadsana.kpi.infrastructure.persistence.entity.KpiRutaJpaEntity;
import pe.edu.unmsm.ciudadsana.kpi.infrastructure.persistence.entity.KpiUnidadJpaEntity;
import pe.edu.unmsm.ciudadsana.kpi.infrastructure.persistence.entity.KpiZonaJpaEntity;
import pe.edu.unmsm.ciudadsana.kpi.infrastructure.persistence.entity.ResumenOperativoDiarioJpaEntity;
import pe.edu.unmsm.ciudadsana.kpi.infrastructure.persistence.repository.KpiAlertaJpaRepository;
import pe.edu.unmsm.ciudadsana.kpi.infrastructure.persistence.repository.KpiRutaJpaRepository;
import pe.edu.unmsm.ciudadsana.kpi.infrastructure.persistence.repository.KpiUnidadJpaRepository;
import pe.edu.unmsm.ciudadsana.kpi.infrastructure.persistence.repository.KpiZonaJpaRepository;
import pe.edu.unmsm.ciudadsana.kpi.infrastructure.persistence.repository.ResumenOperativoDiarioJpaRepository;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class KpiPersistenceAdapter implements KpiPersistencePort {

    private final ResumenOperativoDiarioJpaRepository resumenRepo;
    private final KpiRutaJpaRepository rutaRepo;
    private final KpiUnidadJpaRepository unidadRepo;
    private final KpiZonaJpaRepository zonaRepo;
    private final KpiAlertaJpaRepository alertaRepo;

    public KpiPersistenceAdapter(
            ResumenOperativoDiarioJpaRepository resumenRepo,
            KpiRutaJpaRepository rutaRepo,
            KpiUnidadJpaRepository unidadRepo,
            KpiZonaJpaRepository zonaRepo,
            KpiAlertaJpaRepository alertaRepo) {
        this.resumenRepo = resumenRepo;
        this.rutaRepo = rutaRepo;
        this.unidadRepo = unidadRepo;
        this.zonaRepo = zonaRepo;
        this.alertaRepo = alertaRepo;
    }

    @Override
    public Optional<ResumenOperativoDto> findResumenByDistritoAndFecha(UUID tenantId, UUID distritoId, LocalDate fecha) {
        return resumenRepo.findByTenantIdAndDistritoIdExternoAndFecha(tenantId, distritoId, fecha)
                .map(this::toResumenDto);
    }

    @Override
    public ResumenOperativoDto saveResumen(ResumenOperativoDto dto) {
        ResumenOperativoDiarioJpaEntity entity = resumenRepo.findById(dto.resumenId())
                .orElseGet(ResumenOperativoDiarioJpaEntity::new);

        entity.setId(dto.resumenId());
        entity.setTenantId(dto.tenantId());
        entity.setDistritoIdExterno(dto.distritoIdExterno());
        entity.setFecha(dto.fecha());
        entity.setKmProgramados(dto.kmProgramados());
        entity.setKmRecorridos(dto.kmRecorridos());
        entity.setToneladasRecolectadas(dto.toneladasRecolectadas());
        entity.setCoberturaPorcentaje(dto.coberturaPorcentaje());
        entity.setAlertasRegistradas(dto.alertasRegistradas());
        entity.setAlertasAtendidas(dto.alertasAtendidas());
        entity.setTiempoRespuestaPromedioMin(dto.tiempoRespuestaPromedioMin());

        return toResumenDto(resumenRepo.save(entity));
    }

    @Override
    public PageResult<KpiRutaDto> findKpisRuta(UUID tenantId, LocalDate fechaDesde, LocalDate fechaHasta, int page, int size) {
        Page<KpiRutaJpaEntity> jpaPage = rutaRepo.findAllFiltered(tenantId, fechaDesde, fechaHasta, PageRequest.of(page, size));
        List<KpiRutaDto> content = jpaPage.getContent().stream().map(this::toRutaDto).toList();
        return PageResult.of(content, page, size, jpaPage.getTotalElements());
    }

    @Override
    public PageResult<KpiUnidadDto> findKpisUnidad(UUID tenantId, UUID unidadId, LocalDate fechaDesde, LocalDate fechaHasta, int page, int size) {
        Page<KpiUnidadJpaEntity> jpaPage = unidadRepo.findAllFiltered(tenantId, unidadId, fechaDesde, fechaHasta, PageRequest.of(page, size));
        List<KpiUnidadDto> content = jpaPage.getContent().stream().map(this::toUnidadDto).toList();
        return PageResult.of(content, page, size, jpaPage.getTotalElements());
    }

    @Override
    public PageResult<KpiZonaDto> findKpisZona(UUID tenantId, UUID zonaId, LocalDate fechaDesde, LocalDate fechaHasta, int page, int size) {
        Page<KpiZonaJpaEntity> jpaPage = zonaRepo.findAllFiltered(tenantId, zonaId, fechaDesde, fechaHasta, PageRequest.of(page, size));
        List<KpiZonaDto> content = jpaPage.getContent().stream().map(this::toZonaDto).toList();
        return PageResult.of(content, page, size, jpaPage.getTotalElements());
    }

    @Override
    public PageResult<KpiAlertaDto> findKpisAlerta(UUID tenantId, UUID zonaId, LocalDate fechaDesde, LocalDate fechaHasta, int page, int size) {
        Page<KpiAlertaJpaEntity> jpaPage = alertaRepo.findAllFiltered(tenantId, zonaId, fechaDesde, fechaHasta, PageRequest.of(page, size));
        List<KpiAlertaDto> content = jpaPage.getContent().stream().map(this::toAlertaDto).toList();
        return PageResult.of(content, page, size, jpaPage.getTotalElements());
    }

    // -------------------------------------------------------------------------
    // Mappers
    // -------------------------------------------------------------------------

    private ResumenOperativoDto toResumenDto(ResumenOperativoDiarioJpaEntity e) {
        return new ResumenOperativoDto(
                e.getId(), e.getTenantId(), e.getDistritoIdExterno(), e.getFecha(),
                e.getKmProgramados(), e.getKmRecorridos(), e.getToneladasRecolectadas(),
                e.getCoberturaPorcentaje(), e.getAlertasRegistradas(), e.getAlertasAtendidas(),
                e.getTiempoRespuestaPromedioMin(), e.getCreadoEn()
        );
    }

    private KpiRutaDto toRutaDto(KpiRutaJpaEntity e) {
        return new KpiRutaDto(
                e.getId(), e.getTenantId(), e.getRutaIdExterno(), e.getFecha(),
                e.getDistanciaPlanificadaM(), e.getDistanciaRealM(),
                e.getDuracionPlanificadaS(), e.getDuracionRealS(),
                e.getZonasProgramadas(), e.getZonasAtendidas(),
                e.getCumplimientoPorcentaje(), e.getKmPorTonelada(), e.getCreadoEn()
        );
    }

    private KpiUnidadDto toUnidadDto(KpiUnidadJpaEntity e) {
        return new KpiUnidadDto(
                e.getId(), e.getTenantId(), e.getUnidadIdExterno(), e.getFecha(),
                e.getKmRecorridos(), e.getHorasOperacion(),
                e.getToneladasRecolectadas(), e.getConsumoEstimadoLitros(), e.getCreadoEn()
        );
    }

    private KpiZonaDto toZonaDto(KpiZonaJpaEntity e) {
        return new KpiZonaDto(
                e.getId(), e.getTenantId(), e.getZonaIdExterno(), e.getFecha(),
                e.getVecesProgramada(), e.getVecesAtendida(),
                e.getKgRecolectados(), e.getCoberturaPorcentaje(), e.getCreadoEn()
        );
    }

    private KpiAlertaDto toAlertaDto(KpiAlertaJpaEntity e) {
        return new KpiAlertaDto(
                e.getId(), e.getTenantId(), e.getAlertaIdExterno(), e.getZonaIdExterno(),
                e.getRegistradaEn(), e.getAtendidaEn(), e.getTiempoRespuestaMin(),
                e.isFueCritica(), e.isIncluidaEnRuta(), e.getCreadoEn()
        );
    }
}
