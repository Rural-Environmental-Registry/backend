package br.car.registration.repository;

import br.car.registration.domain.attributes.PersonAttribute;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PersonAttributeRepository extends CrudRepository<PersonAttribute, UUID> {
}
