package pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.adapter;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.ChoferesPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Chofer;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ChoferId;
import pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.mapper.OperacionEntityMapper;
import pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.repository.ChoferJpaRepository;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;

import java.util.Optional;

@Component
public class ChoferesPersistenceAdapter implements ChoferesPersistencePort {

    private final ChoferJpaRepository repo;
    private final OperacionEntityMapper mapper;

    public ChoferesPersistenceAdapter(ChoferJpaRepository repo, OperacionEntityMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public Optional<Chofer> findById(ChoferId id, TenantId tenantId) {
        return repo.findByIdAndTenantId(id.value(), tenantId.value()).map(mapper::toDomain);
    }

    @Override
    public boolean existsByDni(String dni, TenantId tenantId) {
        return repo.existsByDniAndTenantId(dni, tenantId.value());
    }

    @Override
    public Chofer save(Chofer chofer) {
        return mapper.toDomain(repo.save(mapper.toEntity(chofer)));
    }

    @Override
    public PageResult<Chofer> findAll(TenantId tenantId, int page, int size) {
        var p = repo.findAllByTenantId(tenantId.value(), PageRequest.of(page, size));
        return PageResult.of(p.getContent().stream().map(mapper::toDomain).toList(), page, size, p.getTotalElements());
    }
}
