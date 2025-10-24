package br.car.registration.domain.attributes;

import br.car.registration.domain.PropertyDocument;
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

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "attributes")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "origin_check", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("PROPERTY_DOCUMENT")
public class PropertyDocumentAttribute extends AttributeEntity<Object> {

    @ManyToOne
    @JoinColumn(name = "property_document_id", nullable = false)
    private PropertyDocument propertyDocument;

    @Override
    public PropertyDocument getEntity() {
        return propertyDocument;
    }
}
