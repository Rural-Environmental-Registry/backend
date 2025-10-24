package br.car.registration.domain.attributes;

import br.car.registration.domain.Person;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "attributes")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "origin_check", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("PERSON")
public class PersonAttribute extends AttributeEntity<Object> {

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @Override
    public Person getEntity() {
        return person;
    }
}
