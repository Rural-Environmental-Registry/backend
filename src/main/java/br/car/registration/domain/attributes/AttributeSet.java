package br.car.registration.domain.attributes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

import br.car.registration.enums.EntityTypesEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "attribute_set")
public class AttributeSet {

        @Id
        @Column(name = "attribute_set_id", nullable = false, updatable = false)
        private UUID attributeSetId = UUID.randomUUID();

        @Enumerated(EnumType.STRING)
        private EntityTypesEnum entityType;

        /**
         * Many-to-Many for attributes Using a dedicated join table: att_set_def
         */
        @ManyToMany
        @JoinTable(name = "att_set_def", joinColumns = @JoinColumn(name = "attribute_set_id"), inverseJoinColumns = @JoinColumn(name = "attribute_definition_id"))
        private List<AttributeDefinition> attributes;
}
