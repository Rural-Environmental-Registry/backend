package br.car.registration.domain;

import br.car.registration.domain.attributes.AttributableEntity;
import br.car.registration.domain.attributes.PersonAttribute;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "person")
public class Person extends AttributableEntity {

    @Column(name = "identifier", nullable = false)
    private String identifier;

    @Column(name = "name")
    private String name;

    @Column(name = "mothers_name")
    private String mothersName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PersonAttribute> attributes;
}
