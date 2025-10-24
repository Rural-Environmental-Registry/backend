package br.car.registration.domain.attributes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import br.car.registration.enums.AttributeTypesEnum;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "attribute_definition")
public class AttributeDefinition {

    @Id
    @Column(name = "attribute_definition_id", nullable = false, updatable = false)
    private UUID attributeDefinitionId = UUID.randomUUID();

    @Enumerated(EnumType.STRING)
    private AttributeTypesEnum type;

    private String name;

    @Column(name = "allowed_values", columnDefinition = "TEXT[]")
    @JdbcTypeCode(SqlTypes.ARRAY)
    private List<String> allowedValues;
}
