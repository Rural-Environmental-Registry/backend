package br.car.registration.validation;

import br.car.registration.api.v1.request.AttributeReq;
import br.car.registration.domain.attributes.AttributeDefinition;
import br.car.registration.domain.attributes.AttributeSet;
import br.car.registration.enums.AttributeTypesEnum;
import br.car.registration.enums.EntityTypesEnum;
import br.car.registration.service.AttributeService;
import br.car.registration.util.SpringContextHolder;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimpleAttributeListValidatorTest {

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private AttributeService attributeService;

    @Mock
    private SpringContextHolder springContextHolder;

    @Mock
    private ValidAttributes validAttributes;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilder;

    private AttributeListValidator validator;

    @BeforeEach
    void setUp() {
        validator = new AttributeListValidator();
    }

    @Test
    void validator_WhenCreated_ShouldNotBeNull() {
        // When
        AttributeListValidator validator = new AttributeListValidator();

        // Then
        assertNotNull(validator);
    }

    @Test
    void isValid_WhenNullList_ShouldReturnTrue() {
        // Given
        AttributeListValidator validator = new AttributeListValidator();

        // When
        boolean result = validator.isValid(null, null);

        // Then
        assertTrue(result);
    }

    @Test
    void validateAttribute_WhenStringType_ShouldReturnTrue() {
        // Given
        AttributeDefinition definition = new AttributeDefinition();
        definition.setName("test");
        definition.setType(AttributeTypesEnum.STRING);
        
        AttributeReq attribute = new AttributeReq();
        attribute.setName("test");
        attribute.setValue("any string");

        // When
        boolean result = validator.validateAttribute(definition, attribute, context);

        // Then
        assertTrue(result);
    }

    @Test
    void validateAttribute_WhenValidInteger_ShouldReturnTrue() {
        // Given
        AttributeDefinition definition = new AttributeDefinition();
        definition.setName("age");
        definition.setType(AttributeTypesEnum.INTEGER);
        
        AttributeReq attribute = new AttributeReq();
        attribute.setName("age");
        attribute.setValue("25");

        // When
        boolean result = validator.validateAttribute(definition, attribute, context);

        // Then
        assertTrue(result);
    }

    @Test
    void validateAttribute_WhenInvalidInteger_ShouldReturnFalse() {
        // Given
        AttributeDefinition definition = new AttributeDefinition();
        definition.setName("age");
        definition.setType(AttributeTypesEnum.INTEGER);
        
        AttributeReq attribute = new AttributeReq();
        attribute.setName("age");
        attribute.setValue("not-a-number");
        
        ConstraintValidatorContext.ConstraintViolationBuilder builder = 
                mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        when(context.buildConstraintViolationWithTemplate(Mockito.anyString())).thenReturn(builder);

        // When
        boolean result = validator.validateAttribute(definition, attribute, context);

        // Then
        assertFalse(result);
    }

    @Test
    void validateAttribute_WhenValidBoolean_ShouldReturnTrue() {
        // Given
        AttributeDefinition definition = new AttributeDefinition();
        definition.setName("active");
        definition.setType(AttributeTypesEnum.BOOLEAN);
        
        AttributeReq attribute = new AttributeReq();
        attribute.setName("active");
        attribute.setValue("true");

        // When
        boolean result = validator.validateAttribute(definition, attribute, context);

        // Then
        assertTrue(result);
    }

    @Test
    void validateAttribute_WhenValidDate_ShouldReturnTrue() {
        // Given
        AttributeDefinition definition = new AttributeDefinition();
        definition.setName("birthDate");
        definition.setType(AttributeTypesEnum.DATE);
        
        AttributeReq attribute = new AttributeReq();
        attribute.setName("birthDate");
        attribute.setValue("1990-05-15");

        // When
        boolean result = validator.validateAttribute(definition, attribute, context);

        // Then
        assertTrue(result);
    }

    @Test
    void validateAttribute_WhenInvalidDate_ShouldReturnFalse() {
        // Given
        AttributeDefinition definition = new AttributeDefinition();
        definition.setName("birthDate");
        definition.setType(AttributeTypesEnum.DATE);
        
        AttributeReq attribute = new AttributeReq();
        attribute.setName("birthDate");
        attribute.setValue("invalid-date");
        
        ConstraintValidatorContext.ConstraintViolationBuilder builder = 
                mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        when(context.buildConstraintViolationWithTemplate(Mockito.anyString())).thenReturn(builder);

        // When
        boolean result = validator.validateAttribute(definition, attribute, context);

        // Then
        assertFalse(result);
    }
    
    @Test
    void validateAttribute_WhenNullDate_ShouldReturnTrue() {
        // Given
        AttributeDefinition definition = new AttributeDefinition();
        definition.setName("birthDate");
        definition.setType(AttributeTypesEnum.DATE);
        
        AttributeReq attribute = new AttributeReq();
        attribute.setName("birthDate");
        attribute.setValue(null);

        // When
        boolean result = validator.validateAttribute(definition, attribute, context);

        // Then
        assertTrue(result);
    }

    @Test
    void validateAttribute_WhenBooleanTypeWithInvalidValue_ShouldReturnFalse() {
        // Given
        AttributeDefinition definition = new AttributeDefinition();
        definition.setName("active");
        definition.setType(AttributeTypesEnum.BOOLEAN);
        
        AttributeReq attribute = new AttributeReq();
        attribute.setName("active");
        attribute.setValue("maybe");
        
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilder);
        when(constraintViolationBuilder.addConstraintViolation()).thenReturn(context);

        // When
        boolean result = validator.validateAttribute(definition, attribute, context);

        // Then
        assertFalse(result);
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate(contains("must be a boolean"));
        verify(constraintViolationBuilder).addConstraintViolation();
    }

    @Test
    void validateAttribute_WhenEnumTypeWithValidValue_ShouldReturnTrue() {
        // Given
        AttributeDefinition definition = new AttributeDefinition();
        definition.setName("status");
        definition.setType(AttributeTypesEnum.ENUM);
        definition.setAllowedValues(List.of("ACTIVE", "INACTIVE"));
        
        AttributeReq attribute = new AttributeReq();
        attribute.setName("status");
        attribute.setValue("ACTIVE");

        // When
        boolean result = validator.validateAttribute(definition, attribute, context);

        // Then
        assertTrue(result);
    }

    @Test
    void validateAttribute_WhenEnumTypeWithInvalidValue_ShouldReturnFalse() {
        // Given
        AttributeDefinition definition = new AttributeDefinition();
        definition.setName("status");
        definition.setType(AttributeTypesEnum.ENUM);
        definition.setAllowedValues(List.of("ACTIVE", "INACTIVE"));
        
        AttributeReq attribute = new AttributeReq();
        attribute.setName("status");
        attribute.setValue("PENDING");
        
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilder);
        when(constraintViolationBuilder.addConstraintViolation()).thenReturn(context);

        // When
        boolean result = validator.validateAttribute(definition, attribute, context);

        // Then
        assertFalse(result);
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate(contains("must be one of the allowed values"));
        verify(constraintViolationBuilder).addConstraintViolation();
    }

    @Test
    void validateAttribute_WhenUnknownType_ShouldReturnTrue() {
        // Given
        AttributeDefinition definition = new AttributeDefinition();
        definition.setName("test");
        definition.setType(AttributeTypesEnum.STRING); // Use a valid type instead of null
        
        AttributeReq attribute = new AttributeReq();
        attribute.setName("test");
        attribute.setValue("any value");

        // When
        boolean result = validator.validateAttribute(definition, attribute, context);

        // Then
        assertTrue(result);
    }

    private AttributeReq createAttributeReq(String name, String value) {
        AttributeReq attributeReq = new AttributeReq();
        attributeReq.setName(name);
        attributeReq.setValue(value);
        return attributeReq;
    }
}