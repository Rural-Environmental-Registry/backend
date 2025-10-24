package br.car.registration.repository;

import br.car.registration.domain.Property;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PropertyRepository extends JpaRepository<Property,UUID>, JpaSpecificationExecutor<Property> {}
