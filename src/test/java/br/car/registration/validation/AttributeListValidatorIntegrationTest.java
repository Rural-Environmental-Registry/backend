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
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AttributeListValidatorIntegrationTest {

    @Mock
    private AttributeService attributeService;

    @Mock
    private ValidAttributes validAttributes;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilder;

    private AttributeListValidator validator;

    @BeforeEach
    void setUp() {
        validator = new AttributeListValidator();
        when(validAttributes.entity()).thenReturn(EntityTypesEnum.PROPERTY);
    }

    @Test
    void initialize_WhenCalled_ShouldSetEntityAndAttributeService() {
        // Given
        try (MockedStatic<SpringContextHolder> mockedStatic = mockStatic(SpringContextHolder.class)) {
            mockedStatic.when(() -> SpringContextHolder.getBean(AttributeService.class)).thenReturn(attributeService);

            // When
            validator.initialize(validAttributes);

            // Then
            // The initialize method should not throw any exceptions
            assertThat(validator).isNotNull();
        }
    }

    @Test
    void isValid_WhenAttributeSetNotFound_ShouldReturnFalse() {
        // Given
        try (MockedStatic<SpringContextHolder> mockedStatic = mockStatic(SpringContextHolder.class)) {
            mockedStatic.when(() -> SpringContextHolder.getBean(AttributeService.class)).thenReturn(attributeService);
            when(attributeService.findAttributeSetByEntityType(EntityTypesEnum.PROPERTY))
                    .thenReturn(Optional.empty());
            
            validator.initialize(validAttributes);
            List<AttributeReq> attributeReqs = List.of(createAttributeReq("test", "value"));

            // When
            boolean result = validator.isValid(attributeReqs, constraintValidatorContext);

            // Then
            assertThat(result).isFalse();
        }
    }

    @Test
    void isValid_WhenAllRequiredAttributesProvided_ShouldReturnTrue() {
        // Given
        try (MockedStatic<SpringContextHolder> mockedStatic = mockStatic(SpringContextHolder.class)) {
            mockedStatic.when(() -> SpringContextHolder.getBean(AttributeService.class)).thenReturn(attributeService);
            
            AttributeSet attributeSet = createAttributeSetWithRequiredAttributes();
            when(attributeService.findAttributeSetByEntityType(EntityTypesEnum.PROPERTY))
                    .thenReturn(Optional.of(attributeSet));
            
            validator.initialize(validAttributes);
            List<AttributeReq> attributeReqs = List.of(createAttributeReq("name", "test value"));

            // When
            boolean result = validator.isValid(attributeReqs, constraintValidatorContext);

            // Then
            assertThat(result).isTrue();
        }
    }

    @Test
    void isValid_WhenRequiredAttributeMissing_ShouldReturnFalse() {
        // Given
        try (MockedStatic<SpringContextHolder> mockedStatic = mockStatic(SpringContextHolder.class)) {
            mockedStatic.when(() -> SpringContextHolder.getBean(AttributeService.class)).thenReturn(attributeService);
            
            AttributeSet attributeSet = createAttributeSetWithRequiredAttributes();
            when(attributeService.findAttributeSetByEntityType(EntityTypesEnum.PROPERTY))
                    .thenReturn(Optional.of(attributeSet));
            
            when(constraintValidatorContext.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilder);
            when(constraintViolationBuilder.addConstraintViolation()).thenReturn(constraintValidatorContext);
            
            validator.initialize(validAttributes);
            List<AttributeReq> attributeReqs = List.of(createAttributeReq("other", "value"));

            // When
            boolean result = validator.isValid(attributeReqs, constraintValidatorContext);

            // Then
            assertThat(result).isFalse();
            verify(constraintValidatorContext).disableDefaultConstraintViolation();
            verify(constraintValidatorContext).buildConstraintViolationWithTemplate("The required attribute 'name' was not provided.");
            verify(constraintViolationBuilder).addConstraintViolation();
        }
    }

    @Test
    void isValid_WhenAttributeFoundButInvalid_ShouldReturnFalse() {
        // Given
        try (MockedStatic<SpringContextHolder> mockedStatic = mockStatic(SpringContextHolder.class)) {
            mockedStatic.when(() -> SpringContextHolder.getBean(AttributeService.class)).thenReturn(attributeService);
            
            AttributeSet attributeSet = createAttributeSetWithIntegerAttribute();
            when(attributeService.findAttributeSetByEntityType(EntityTypesEnum.PROPERTY))
                    .thenReturn(Optional.of(attributeSet));
            
            when(constraintValidatorContext.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilder);
            when(constraintViolationBuilder.addConstraintViolation()).thenReturn(constraintValidatorContext);
            
            validator.initialize(validAttributes);
            List<AttributeReq> attributeReqs = List.of(createAttributeReq("age", "not-a-number"));

            // When
            boolean result = validator.isValid(attributeReqs, constraintValidatorContext);

            // Then
            assertThat(result).isFalse();
        }
    }

    @Test
    void isValid_WhenMultipleAttributesWithMixedValidity_ShouldReturnFalse() {
        // Given
        try (MockedStatic<SpringContextHolder> mockedStatic = mockStatic(SpringContextHolder.class)) {
            mockedStatic.when(() -> SpringContextHolder.getBean(AttributeService.class)).thenReturn(attributeService);
            
            AttributeSet attributeSet = createAttributeSetWithMultipleAttributes();
            when(attributeService.findAttributeSetByEntityType(EntityTypesEnum.PROPERTY))
                    .thenReturn(Optional.of(attributeSet));
            
            when(constraintValidatorContext.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilder);
            when(constraintViolationBuilder.addConstraintViolation()).thenReturn(constraintValidatorContext);
            
            validator.initialize(validAttributes);
            List<AttributeReq> attributeReqs = List.of(
                    createAttributeReq("name", "valid name"),
                    createAttributeReq("age", "invalid-age")
            );

            // When
            boolean result = validator.isValid(attributeReqs, constraintValidatorContext);

            // Then
            assertThat(result).isFalse();
        }
    }

    @Test
    void isValid_WhenAllAttributesValid_ShouldReturnTrue() {
        // Given
        try (MockedStatic<SpringContextHolder> mockedStatic = mockStatic(SpringContextHolder.class)) {
            mockedStatic.when(() -> SpringContextHolder.getBean(AttributeService.class)).thenReturn(attributeService);
            
            AttributeSet attributeSet = createAttributeSetWithMultipleAttributes();
            when(attributeService.findAttributeSetByEntityType(EntityTypesEnum.PROPERTY))
                    .thenReturn(Optional.of(attributeSet));
            
            validator.initialize(validAttributes);
            List<AttributeReq> attributeReqs = List.of(
                    createAttributeReq("name", "valid name"),
                    createAttributeReq("age", "25"),
                    createAttributeReq("active", "true")
            );

            // When
            boolean result = validator.isValid(attributeReqs, constraintValidatorContext);

            // Then
            assertThat(result).isTrue();
        }
    }

    private AttributeSet createAttributeSetWithRequiredAttributes() {
        AttributeDefinition requiredAttr = new AttributeDefinition();
        requiredAttr.setAttributeDefinitionId(UUID.randomUUID());
        requiredAttr.setName("name");
        requiredAttr.setType(AttributeTypesEnum.STRING);
        
        AttributeSet attributeSet = new AttributeSet();
        attributeSet.setAttributeSetId(UUID.randomUUID());
        attributeSet.setEntityType(EntityTypesEnum.PROPERTY);
        attributeSet.setAttributes(List.of(requiredAttr));
        
        return attributeSet;
    }

    private AttributeSet createAttributeSetWithIntegerAttribute() {
        AttributeDefinition ageAttr = new AttributeDefinition();
        ageAttr.setAttributeDefinitionId(UUID.randomUUID());
        ageAttr.setName("age");
        ageAttr.setType(AttributeTypesEnum.INTEGER);
        
        AttributeSet attributeSet = new AttributeSet();
        attributeSet.setAttributeSetId(UUID.randomUUID());
        attributeSet.setEntityType(EntityTypesEnum.PROPERTY);
        attributeSet.setAttributes(List.of(ageAttr));
        
        return attributeSet;
    }

    private AttributeSet createAttributeSetWithMultipleAttributes() {
        AttributeDefinition nameAttr = new AttributeDefinition();
        nameAttr.setAttributeDefinitionId(UUID.randomUUID());
        nameAttr.setName("name");
        nameAttr.setType(AttributeTypesEnum.STRING);
        
        AttributeDefinition ageAttr = new AttributeDefinition();
        ageAttr.setAttributeDefinitionId(UUID.randomUUID());
        ageAttr.setName("age");
        ageAttr.setType(AttributeTypesEnum.INTEGER);
        
        AttributeDefinition activeAttr = new AttributeDefinition();
        activeAttr.setAttributeDefinitionId(UUID.randomUUID());
        activeAttr.setName("active");
        activeAttr.setType(AttributeTypesEnum.BOOLEAN);
        
        AttributeSet attributeSet = new AttributeSet();
        attributeSet.setAttributeSetId(UUID.randomUUID());
        attributeSet.setEntityType(EntityTypesEnum.PROPERTY);
        attributeSet.setAttributes(List.of(nameAttr, ageAttr, activeAttr));
        
        return attributeSet;
    }

    private AttributeReq createAttributeReq(String name, String value) {
        AttributeReq attributeReq = new AttributeReq();
        attributeReq.setName(name);
        attributeReq.setValue(value);
        return attributeReq;
    }
}
