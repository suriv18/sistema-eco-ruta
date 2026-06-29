package pe.edu.unmsm.ciudadsana.ciudadano.infrastructure.persistence.adapter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.out.AlertasPersistencePort;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.model.AlertaCiudadana;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.AlertaId;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.ZonaExternoId;
import pe.edu.unmsm.ciudadsana.ciudadano.infrastructure.persistence.entity.AlertaCiudadanaJpaEntity;
import pe.edu.unmsm.ciudadsana.ciudadano.infrastructure.persistence.entity.AlertaFotoJpaEntity;
import pe.edu.unmsm.ciudadsana.ciudadano.infrastructure.persistence.entity.AlertaHistorialJpaEntity;
import pe.edu.unmsm.ciudadsana.ciudadano.infrastructure.persistence.entity.ValidacionAlertaJpaEntity;
import pe.edu.unmsm.ciudadsana.ciudadano.infrastructure.persistence.mapper.CiudadanoEntityMapper;
import pe.edu.unmsm.ciudadsana.ciudadano.infrastructure.persistence.repository.AlertaCiudadanaJpaRepository;
import pe.edu.unmsm.ciudadsana.ciudadano.infrastructure.persistence.repository.AlertaFotoJpaRepository;
import pe.edu.unmsm.ciudadsana.ciudadano.infrastructure.persistence.repository.AlertaHistorialJpaRepository;
import pe.edu.unmsm.ciudadsana.ciudadano.infrastructure.persistence.repository.ValidacionAlertaJpaRepository;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;

import java.util.List;
import java.util.Optional;

@Component
public class AlertasPersistenceAdapter implements AlertasPersistencePort {

    private final AlertaCiudadanaJpaRepository alertaRepo;
    private final AlertaFotoJpaRepository fotoRepo;
    private final AlertaHistorialJpaRepository historialRepo;
    private final ValidacionAlertaJpaRepository validacionRepo;
    private final CiudadanoEntityMapper mapper;

    public AlertasPersistenceAdapter(
            AlertaCiudadanaJpaRepository alertaRepo,
            AlertaFotoJpaRepository fotoRepo,
            AlertaHistorialJpaRepository historialRepo,
            ValidacionAlertaJpaRepository validacionRepo,
            CiudadanoEntityMapper mapper
    ) {
        this.alertaRepo = alertaRepo;
        this.fotoRepo = fotoRepo;
        this.historialRepo = historialRepo;
        this.validacionRepo = validacionRepo;
        this.mapper = mapper;
    }

    @Override
    public Optional<AlertaCiudadana> findByIdAndTenantId(AlertaId id, TenantId tenantId) {
        return alertaRepo.findByIdAndTenantId(id.value(), tenantId.value())
                .map(e -> {
                    List<AlertaFotoJpaEntity> fotos = fotoRepo.findAllByAlertaId(e.getId());
                    List<AlertaHistorialJpaEntity> historial = historialRepo.findAllByAlertaIdOrderByCambiadoEnAsc(e.getId());
                    Optional<ValidacionAlertaJpaEntity> validacion = validacionRepo.findByAlertaId(e.getId());
                    return mapper.toDomain(e, fotos, historial, validacion);
                });
    }

    @Override
    public AlertaCiudadana save(AlertaCiudadana alerta) {
        // 1. Save main entity
        AlertaCiudadanaJpaEntity saved = alertaRepo.save(mapper.toEntity(alerta));

        // 2. Save fotos (idempotent — same UUID overwrites)
        alerta.getFotos().forEach(f -> fotoRepo.save(mapper.toEntity(f, saved.getId())));

        // 3. Save new historial entries only
        alerta.getHistorial().forEach(h -> {
            if (!historialRepo.existsById(h.historialId())) {
                historialRepo.save(mapper.toEntity(h));
            }
        });

        // 4. Save validacion if present
        alerta.getValidacion().ifPresent(v -> validacionRepo.save(mapper.toEntity(v)));

        // 5. Reload with all children and return
        List<AlertaFotoJpaEntity> fotos = fotoRepo.findAllByAlertaId(saved.getId());
        List<AlertaHistorialJpaEntity> historial = historialRepo.findAllByAlertaIdOrderByCambiadoEnAsc(saved.getId());
        Optional<ValidacionAlertaJpaEntity> validacion = validacionRepo.findByAlertaId(saved.getId());
        return mapper.toDomain(saved, fotos, historial, validacion);
    }

    @Override
    public PageResult<AlertaCiudadana> findAllByTenantId(TenantId tenantId, String estado, int page, int size) {
        Page<AlertaCiudadanaJpaEntity> p;
        if (estado != null && !estado.isBlank()) {
            p = alertaRepo.findAllByTenantIdAndEstado(tenantId.value(), estado, PageRequest.of(page, size));
        } else {
            p = alertaRepo.findAllByTenantId(tenantId.value(), PageRequest.of(page, size));
        }
        List<AlertaCiudadana> content = p.getContent().stream()
                .map(e -> {
                    List<AlertaFotoJpaEntity> fotos = fotoRepo.findAllByAlertaId(e.getId());
                    List<AlertaHistorialJpaEntity> historial = historialRepo.findAllByAlertaIdOrderByCambiadoEnAsc(e.getId());
                    Optional<ValidacionAlertaJpaEntity> validacion = validacionRepo.findByAlertaId(e.getId());
                    return mapper.toDomain(e, fotos, historial, validacion);
                })
                .toList();
        return PageResult.of(content, page, size, p.getTotalElements());
    }

    @Override
    public PageResult<AlertaCiudadana> findAllByZonaAndTenantId(ZonaExternoId zonaId, TenantId tenantId, int page, int size) {
        Page<AlertaCiudadanaJpaEntity> p = alertaRepo.findAllByZonaIdExternoAndTenantId(
                zonaId.value(), tenantId.value(), PageRequest.of(page, size));
        List<AlertaCiudadana> content = p.getContent().stream()
                .map(e -> {
                    List<AlertaFotoJpaEntity> fotos = fotoRepo.findAllByAlertaId(e.getId());
                    List<AlertaHistorialJpaEntity> historial = historialRepo.findAllByAlertaIdOrderByCambiadoEnAsc(e.getId());
                    Optional<ValidacionAlertaJpaEntity> validacion = validacionRepo.findByAlertaId(e.getId());
                    return mapper.toDomain(e, fotos, historial, validacion);
                })
                .toList();
        return PageResult.of(content, page, size, p.getTotalElements());
    }
}
