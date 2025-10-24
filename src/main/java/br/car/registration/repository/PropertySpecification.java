package br.car.registration.repository;


import br.car.registration.api.v1.request.PropertyFilter;
import br.car.registration.domain.*;
import jakarta.persistence.criteria.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PropertySpecification {

    public static Specification<Property> withFilters(PropertyFilter filter, String identifier) {
        return (root, query, cb) -> {
            var predicates = cb.conjunction();

            predicates = filterByIdNational(identifier, root, cb, predicates);

            if (filter.getPropertyName() != null && !filter.getPropertyName().isEmpty()) {
                predicates = cb.and(predicates, cb.like(cb.lower(root.get("propertyName")), "%" + filter.getPropertyName().toLowerCase() + "%"));
            }

            if (filter.getStateDistrict() != null && !filter.getStateDistrict().isEmpty()) {
                predicates = cb.and(predicates, cb.equal(cb.lower(root.get("stateDistrict")), filter.getStateDistrict().toLowerCase()));
            }
           
            if (filter.getMunicipality() != null && !filter.getMunicipality().isEmpty()) {
                predicates = cb.and(predicates, cb.equal(cb.lower(root.get("municipality")), filter.getMunicipality().toLowerCase()));
            }

            if (filter.getCode() != null && !filter.getCode().isEmpty()) {
                var orPredicates = cb.disjunction();
                for (String partialCode : filter.getCode()) {
                    orPredicates = cb.or(
                        orPredicates,
                        cb.like(cb.lower(root.get("code")), "%" + partialCode.toLowerCase() + "%")
                    );
                }
                predicates = cb.and(predicates, orPredicates);
            }

            return predicates;
        };
    }

    private static Predicate filterByIdNational(String identifier, Root<Property> root, CriteriaBuilder cb, Predicate basePredicate) {
        if (StringUtils.isBlank(identifier)) {
            return basePredicate;
        }

        String normalizedId = identifier.replaceAll("\\s+", "");

        List<Predicate> orConditions = new ArrayList<>();

        Join<Property, Ownership> ownershipJoin = root.join("ownerships", JoinType.LEFT);
        Join<Ownership, Person> ownerJoin = ownershipJoin.join("owner", JoinType.LEFT);
        orConditions.add(
                cb.equal(
                        cb.function("REPLACE", String.class, ownerJoin.get("identifier"), cb.literal(" "), cb.literal("")),
                        normalizedId
                )
        );

        Join<Property, Representativeship> repJoin = root.join("representativeships", JoinType.LEFT);
        Join<Representativeship, Person> repPersonJoin = repJoin.join("representative", JoinType.LEFT);
        orConditions.add(
                cb.equal(
                        cb.function("REPLACE", String.class, repPersonJoin.get("identifier"), cb.literal(" "), cb.literal("")),
                        normalizedId
                )
        );

        return cb.and(basePredicate, cb.or(orConditions.toArray(new Predicate[0])));
    }

}
