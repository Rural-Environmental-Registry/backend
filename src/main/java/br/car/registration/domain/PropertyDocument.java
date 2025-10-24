package br.car.registration.domain;

import br.car.registration.domain.attributes.AttributableEntity;
import br.car.registration.domain.attributes.PropertyDocumentAttribute;
import br.car.registration.enums.DocumentTypesEnum;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "property_document")
public class PropertyDocument extends AttributableEntity {

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @Column(name = "registered_property_name")
    private String registeredPropertyName;

    @Column(name = "area")
    private Double area;

    @Column(name = "document_type")
    @Enumerated(EnumType.STRING)
    private DocumentTypesEnum documentType;

    @OneToMany(mappedBy = "propertyDocument", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PropertyDocumentAttribute> attributes;
}
