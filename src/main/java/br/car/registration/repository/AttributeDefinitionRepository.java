package br.car.registration.repository;

import br.car.registration.domain.attributes.AttributeDefinition;
import br.car.registration.enums.AttributeTypesEnum;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttributeDefinitionRepository extends CrudRepository<AttributeDefinition, String> {

    /**
     * Derives a query from the method name: SELECT a FROM AttributeDefinition a
     * WHERE a.type = :type AND a.name = :name
     */
    Optional<AttributeDefinition> findByTypeAndName(AttributeTypesEnum type, String name);
}
