package pe.edu.unmsm.ciudadsana.shared.persistence.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class JpaSpecificationBuilder<T> {

    private final List<Specification<T>> specs = new ArrayList<>();

    private JpaSpecificationBuilder() {}

    public static <T> JpaSpecificationBuilder<T> of() {
        return new JpaSpecificationBuilder<>();
    }

    public JpaSpecificationBuilder<T> withTenantId(UUID tenantId) {
        if (tenantId != null) {
            specs.add((root, query, cb) -> cb.equal(root.get("tenantId"), tenantId));
        }
        return this;
    }

    public JpaSpecificationBuilder<T> withField(String fieldName, Object value) {
        if (value != null) {
            specs.add((root, query, cb) -> cb.equal(root.get(fieldName), value));
        }
        return this;
    }

    public JpaSpecificationBuilder<T> withFieldLike(String fieldName, String value) {
        if (value != null && !value.isBlank()) {
            specs.add((root, query, cb) ->
                    cb.like(cb.lower(root.get(fieldName)), "%" + value.toLowerCase() + "%"));
        }
        return this;
    }

    public JpaSpecificationBuilder<T> withInstantBetween(String fieldName, Instant from, Instant to) {
        if (from != null && to != null) {
            specs.add((root, query, cb) -> cb.between(root.get(fieldName), from, to));
        } else if (from != null) {
            specs.add((root, query, cb) -> cb.greaterThanOrEqualTo(root.get(fieldName), from));
        } else if (to != null) {
            specs.add((root, query, cb) -> cb.lessThanOrEqualTo(root.get(fieldName), to));
        }
        return this;
    }

    public Specification<T> build() {
        Specification<T> base = (root, query, cb) -> cb.conjunction();
        return specs.stream().reduce(base, Specification::and);
    }

    public Specification<T> buildOr() {
        if (specs.isEmpty()) return (root, query, cb) -> cb.conjunction();
        return (root, query, cb) -> {
            List<Predicate> predicates = specs.stream()
                    .map(s -> s.toPredicate(root, query, cb))
                    .toList();
            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }
}
