package pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.adapter;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.DepositosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Deposito;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DepositoId;
import pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.mapper.OperacionEntityMapper;
import pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.repository.DepositoJpaRepository;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;

import java.util.List;
import java.util.Optional;

@Component
public class DepositosPersistenceAdapter implements DepositosPersistencePort {

    private final DepositoJpaRepository repo;
    private final OperacionEntityMapper mapper;

    public DepositosPersistenceAdapter(DepositoJpaRepository repo, OperacionEntityMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public Optional<Deposito> findByIdAndTenantId(DepositoId id, TenantId tenantId) {
        return repo.findByIdAndTenantId(id.value(), tenantId.value()).map(mapper::toDomain);
    }

    @Override
    public List<Deposito> findAllByTenantId(TenantId tenantId) {
        return repo.findAllByTenantId(tenantId.value()).stream().map(mapper::toDomain).toList();
    }

    @Override
    public long countByTenantId(TenantId tenantId) {
        return repo.countByTenantId(tenantId.value());
    }

    @Override
    public void save(Deposito deposito) {
        repo.save(mapper.toEntity(deposito));
    }
}
