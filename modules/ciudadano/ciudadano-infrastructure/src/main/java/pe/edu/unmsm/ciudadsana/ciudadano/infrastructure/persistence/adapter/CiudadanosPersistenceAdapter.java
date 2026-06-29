package pe.edu.unmsm.ciudadsana.ciudadano.infrastructure.persistence.adapter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.out.CiudadanosPersistencePort;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.model.Ciudadano;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.CiudadanoId;
import pe.edu.unmsm.ciudadsana.ciudadano.infrastructure.persistence.entity.CiudadanoJpaEntity;
import pe.edu.unmsm.ciudadsana.ciudadano.infrastructure.persistence.mapper.CiudadanoEntityMapper;
import pe.edu.unmsm.ciudadsana.ciudadano.infrastructure.persistence.repository.CiudadanoJpaRepository;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;

import java.util.List;
import java.util.Optional;

@Component
public class CiudadanosPersistenceAdapter implements CiudadanosPersistencePort {

    private final CiudadanoJpaRepository repo;
    private final CiudadanoEntityMapper mapper;

    public CiudadanosPersistenceAdapter(CiudadanoJpaRepository repo, CiudadanoEntityMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public Optional<Ciudadano> findByIdAndTenantId(CiudadanoId id, TenantId tenantId) {
        return repo.findByIdAndTenantId(id.value(), tenantId.value())
                .map(mapper::toDomain);
    }

    @Override
    public PageResult<Ciudadano> findAllByTenantId(TenantId tenantId, int page, int size) {
        Page<CiudadanoJpaEntity> p = repo.findAllByTenantId(tenantId.value(), PageRequest.of(page, size));
        List<Ciudadano> content = p.getContent().stream()
                .map(mapper::toDomain)
                .toList();
        return PageResult.of(content, page, size, p.getTotalElements());
    }

    @Override
    public Ciudadano save(Ciudadano ciudadano) {
        CiudadanoJpaEntity saved = repo.save(mapper.toEntity(ciudadano));
        return mapper.toDomain(saved);
    }
}
