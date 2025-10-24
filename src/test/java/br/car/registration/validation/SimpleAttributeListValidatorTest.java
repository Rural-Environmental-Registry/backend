package br.car.registration.validation;

import br.car.registration.api.v1.request.AttributeReq;
import br.car.registration.domain.attributes.AttributeDefinition;
import br.car.registration.enums.AttributeTypesEnum;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SimpleAttributeListValidatorTest {

    @Mock
    private ConstraintValidatorContext context;

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
}