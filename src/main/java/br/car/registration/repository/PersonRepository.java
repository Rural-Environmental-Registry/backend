package br.car.registration.repository;

import br.car.registration.domain.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonRepository extends CrudRepository<Person, UUID> {
    Optional<Person> findFirstByIdentifier(String identifier);
}
