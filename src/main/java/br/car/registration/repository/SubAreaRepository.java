package br.car.registration.repository;

import br.car.registration.domain.SubArea;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SubAreaRepository extends CrudRepository<SubArea, UUID> {
}
