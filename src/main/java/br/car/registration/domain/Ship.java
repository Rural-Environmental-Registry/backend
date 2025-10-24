package br.car.registration.domain;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "ship")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class Ship extends TemporalEntity {

    @Id
    @Column(name = "ship_id", nullable = false, updatable = false)
    private UUID shipId = UUID.randomUUID(); // Auto-generate UUID

    @Column(name = "type", nullable = false, updatable = false, insertable = false)
    private String type;

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;
}
