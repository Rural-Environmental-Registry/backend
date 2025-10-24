package br.car.registration.repository;

import br.car.registration.domain.attributes.AttributeSet;
import br.car.registration.enums.EntityTypesEnum;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttributeSetRepository extends CrudRepository<AttributeSet, String> {
    Optional<AttributeSet> findByEntityType(EntityTypesEnum entityType);
}
