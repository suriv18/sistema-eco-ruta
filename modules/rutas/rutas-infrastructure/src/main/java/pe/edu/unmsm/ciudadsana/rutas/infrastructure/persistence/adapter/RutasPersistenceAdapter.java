package pe.edu.unmsm.ciudadsana.rutas.infrastructure.persistence.adapter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.rutas.application.port.out.RutasPersistencePort;
import pe.edu.unmsm.ciudadsana.rutas.domain.model.Ruta;
import pe.edu.unmsm.ciudadsana.rutas.domain.model.RutaEvento;
import pe.edu.unmsm.ciudadsana.rutas.domain.model.RutaParada;
import pe.edu.unmsm.ciudadsana.rutas.domain.model.RutaVersion;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.RutaId;
import pe.edu.unmsm.ciudadsana.rutas.infrastructure.persistence.entity.RutaEventoJpaEntity;
import pe.edu.unmsm.ciudadsana.rutas.infrastructure.persistence.entity.RutaJpaEntity;
import pe.edu.unmsm.ciudadsana.rutas.infrastructure.persistence.entity.RutaParadaJpaEntity;
import pe.edu.unmsm.ciudadsana.rutas.infrastructure.persistence.entity.RutaVersionJpaEntity;
import pe.edu.unmsm.ciudadsana.rutas.infrastructure.persistence.mapper.RutasEntityMapper;
import pe.edu.unmsm.ciudadsana.rutas.infrastructure.persistence.repository.RutaEventoJpaRepository;
import pe.edu.unmsm.ciudadsana.rutas.infrastructure.persistence.repository.RutaJpaRepository;
import pe.edu.unmsm.ciudadsana.rutas.infrastructure.persistence.repository.RutaParadaJpaRepository;
import pe.edu.unmsm.ciudadsana.rutas.infrastructure.persistence.repository.RutaVersionJpaRepository;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class RutasPersistenceAdapter implements RutasPersistencePort {

    private final RutaJpaRepository rutaJpaRepo;
    private final RutaVersionJpaRepository rutaVersionJpaRepo;
    private final RutaParadaJpaRepository rutaParadaJpaRepo;
    private final RutaEventoJpaRepository rutaEventoJpaRepo;
    private final RutasEntityMapper mapper;

    public RutasPersistenceAdapter(RutaJpaRepository rutaJpaRepo,
                                   RutaVersionJpaRepository rutaVersionJpaRepo,
                                   RutaParadaJpaRepository rutaParadaJpaRepo,
                                   RutaEventoJpaRepository rutaEventoJpaRepo,
                                   RutasEntityMapper mapper) {
        this.rutaJpaRepo = rutaJpaRepo;
        this.rutaVersionJpaRepo = rutaVersionJpaRepo;
        this.rutaParadaJpaRepo = rutaParadaJpaRepo;
        this.rutaEventoJpaRepo = rutaEventoJpaRepo;
        this.mapper = mapper;
    }

    @Override
    public Optional<Ruta> findByIdAndTenantId(RutaId id, TenantId tenantId) {
        return rutaJpaRepo.findByIdAndTenantId(id.value(), tenantId.value())
                .map(this::loadFull);
    }

    @Override
    public PageResult<Ruta> findAllByTenantId(TenantId tenantId,
                                               UUID distritoId,
                                               LocalDate fecha,
                                               String estado,
                                               int page,
                                               int size) {
        Page<RutaJpaEntity> jpaPage = rutaJpaRepo.findAllFiltered(
                tenantId.value(), distritoId, fecha, estado, PageRequest.of(page, size));
        List<Ruta> content = jpaPage.getContent().stream()
                .map(this::loadFull)
                .toList();
        return PageResult.of(content, page, size, jpaPage.getTotalElements());
    }

    @Override
    public Ruta save(Ruta ruta) {
        // 1. Save main ruta entity
        RutaJpaEntity savedRuta = rutaJpaRepo.save(mapper.toEntity(ruta));

        // 2. Save all versions and their paradas
        for (RutaVersion version : ruta.getHistorialVersiones()) {
            rutaVersionJpaRepo.save(mapper.toEntity(version));
            for (RutaParada parada : version.getParadas()) {
                rutaParadaJpaRepo.save(mapper.toEntity(parada));
            }
        }

        // 3. Save new eventos (skip existing ones — they are immutable events)
        for (RutaEvento evento : ruta.getEventos()) {
            if (!rutaEventoJpaRepo.existsById(evento.getId().value())) {
                rutaEventoJpaRepo.save(mapper.toEntity(evento));
            }
        }

        // 4. Reload and return full aggregate
        return loadFull(savedRuta);
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private Ruta loadFull(RutaJpaEntity e) {
        List<RutaVersionJpaEntity> versions =
                rutaVersionJpaRepo.findAllByRutaIdOrderByVersionAsc(e.getId());

        Map<UUID, List<RutaParadaJpaEntity>> paradasByVersion = new HashMap<>();
        for (RutaVersionJpaEntity v : versions) {
            paradasByVersion.put(v.getId(),
                    rutaParadaJpaRepo.findAllByRutaVersionIdOrderByOrdenAsc(v.getId()));
        }

        List<RutaEventoJpaEntity> eventos =
                rutaEventoJpaRepo.findAllByRutaIdOrderByCreadoEnAsc(e.getId());

        return mapper.toDomain(e, versions, paradasByVersion, eventos);
    }
}
