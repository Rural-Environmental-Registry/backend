package br.car.registration.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@DiscriminatorValue("REGISTRAR")
public class Registrarship extends Ship {
    @ManyToOne
    @JoinColumn(name = "id", nullable = false, referencedColumnName = "id")
    private Person registrar;
}
