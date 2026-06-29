package pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.adapter;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.ZonasPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Zona;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.CodigoZona;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DistritoId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ZonaId;
import pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.mapper.OperacionEntityMapper;
import pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.repository.ZonaJpaRepository;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;

import java.util.List;
import java.util.Optional;

@Component
public class ZonasPersistenceAdapter implements ZonasPersistencePort {

    private final ZonaJpaRepository repo;
    private final OperacionEntityMapper mapper;

    public ZonasPersistenceAdapter(ZonaJpaRepository repo, OperacionEntityMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public Optional<Zona> findById(ZonaId id, TenantId tenantId) {
        return repo.findByIdAndTenantId(id.value(), tenantId.value()).map(mapper::toDomain);
    }

    @Override
    public boolean existsByCodigo(CodigoZona codigo, TenantId tenantId) {
        return repo.existsByCodigoAndTenantId(codigo.value(), tenantId.value());
    }

    @Override
    public List<Zona> findByDistrito(DistritoId distritoId, TenantId tenantId) {
        return repo.findAllByDistritoIdAndTenantId(distritoId.value(), tenantId.value())
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public Zona save(Zona zona) {
        return mapper.toDomain(repo.save(mapper.toEntity(zona)));
    }

    @Override
    public PageResult<Zona> findAll(TenantId tenantId, int page, int size) {
        var p = repo.findAllByTenantId(tenantId.value(), PageRequest.of(page, size));
        return PageResult.of(p.getContent().stream().map(mapper::toDomain).toList(), page, size, p.getTotalElements());
    }
}
