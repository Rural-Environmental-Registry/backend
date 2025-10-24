package br.car.registration.domain.attributes;

import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.car.registration.enums.EntityTypesEnum;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class AttributeEntity<T> implements Attribute<T> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Id
    @Column(name = "attribute_id", nullable = false, updatable = false)
    private UUID attributeId = UUID.randomUUID();

    @Column(name = "origin_check", insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private EntityTypesEnum originCheck;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "value_string")
    private String stringValue;

    @Column(name = "value_integer")
    private Integer integerValue;

    @Column(name = "value_boolean")
    private Boolean booleanValue;

    @Column(name = "value_date")
    private LocalDate dateValue;

    @Column(name = "value_json", columnDefinition = "jsonb") // For dynamic types
    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode jsonValue;

    @Transient
    public T getValue() {
        if (stringValue != null)
            return (T) stringValue;
        if (integerValue != null)
            return (T) integerValue;
        if (booleanValue != null)
            return (T) booleanValue;
        if (dateValue != null)
            return (T) dateValue;
        if (jsonValue != null)
            return (T) jsonValue; // JSON handling
        return null;
    }

    public void setValue(T value) {
        if (value instanceof String strVal) {
            this.stringValue = strVal;
        } else if (value instanceof Integer intVal) {
            this.integerValue = intVal;
        } else if (value instanceof Boolean boolVal) {
            this.booleanValue = boolVal;
        } else if (value instanceof LocalDate dateVal) {
            this.dateValue = dateVal;
        } else {
            this.jsonValue = OBJECT_MAPPER.valueToTree(value); // Store as JSON
            // this.jsonValue = new Gson().toJson(value); // Store as JSON
        }
    }

    public abstract AttributableEntity getEntity();
}
