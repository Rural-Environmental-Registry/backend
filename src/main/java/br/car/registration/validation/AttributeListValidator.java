package br.car.registration.validation;

import br.car.registration.api.v1.request.AttributeReq;
import br.car.registration.domain.attributes.AttributeDefinition;
import br.car.registration.domain.attributes.AttributeSet;
import br.car.registration.enums.AttributeTypesEnum;
import br.car.registration.enums.EntityTypesEnum;
import br.car.registration.service.AttributeService;
import br.car.registration.util.SpringContextHolder;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class AttributeListValidator implements ConstraintValidator<ValidAttributes, List<AttributeReq>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttributeListValidator.class);

    private AttributeService attributeService;
    private EntityTypesEnum entity;

    @Override
    public void initialize(ValidAttributes constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        attributeService = SpringContextHolder.getBean(AttributeService.class);
        entity = constraintAnnotation.entity();

        LOGGER.info("AttributeListValidator initialized for entity: {}", entity);

    }

    @Override
    public boolean isValid(List<AttributeReq> attributeReqs, ConstraintValidatorContext constraintValidatorContext) {
        if (attributeReqs == null) {
            return true;
        }
        return attributeService.findAttributeSetByEntityType(this.entity)
                .map(set -> validateAttributes(attributeReqs, set, constraintValidatorContext)).orElse(false);

    }

    private boolean validateAttributes(List<AttributeReq> attributeReqs, AttributeSet set,
            ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = true;
        for (AttributeDefinition requiredAttribute : set.getAttributes()) {
            boolean found = false;
            boolean valid = false;
            for (AttributeReq attributeReq : attributeReqs) {
                if (requiredAttribute.getName().equals(attributeReq.getName())) {
                    found = true;
                    valid = validateAttribute(requiredAttribute, attributeReq, constraintValidatorContext);
                }
            }

            if (!found) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext
                        .buildConstraintViolationWithTemplate(
                                "The required attribute '" + requiredAttribute.getName() + "' was not provided.")
                        .addConstraintViolation();
                isValid = false;
            }

            if (found && !valid) {
                isValid = false;
            }
        }

        return isValid;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    boolean validateAttribute(AttributeDefinition definition, AttributeReq attribute,
            ConstraintValidatorContext constraintValidatorContext) {
        String value = attribute.getValue();
        // if (value == null || value.isEmpty()) {
        //     constraintValidatorContext.disableDefaultConstraintViolation();
        //     constraintValidatorContext.buildConstraintViolationWithTemplate(
        //             "The attribute '" + definition.getName() + "' must have a value.").addConstraintViolation();
        //     return false;
        // }

        switch (definition.getType()) {
        case AttributeTypesEnum.STRING:
            return true;
        case AttributeTypesEnum.INTEGER:
            if (!value.matches("-?\\d+")) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext
                        .buildConstraintViolationWithTemplate(
                                "The attribute '" + definition.getName() + "' must be an integer.")
                        .addConstraintViolation();
                return false;
            }
            break;
        case AttributeTypesEnum.BOOLEAN:
            if (!"true".equalsIgnoreCase(value) && !"false".equalsIgnoreCase(value)) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext
                        .buildConstraintViolationWithTemplate(
                                "The attribute '" + definition.getName() + "' must be a boolean.")
                        .addConstraintViolation();
                return false;
            }
            break;
        case AttributeTypesEnum.DATE:
            if (value == null) {
                return true;
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            try {
                LocalDate.parse(value, formatter);
            } catch (DateTimeParseException e) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext
                        .buildConstraintViolationWithTemplate(
                                "The attribute '" + definition.getName() + "' must be a date on the format yyyy-MM-dd.")
                        .addConstraintViolation();
                return false;
            }
            break;
        case AttributeTypesEnum.ENUM:
            if (!definition.getAllowedValues().contains(value)) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext
                        .buildConstraintViolationWithTemplate("The attribute '" + definition.getName()
                                + "' must be one of the allowed values: " + definition.getAllowedValues())
                        .addConstraintViolation();
                return false;
            }
            break;
        default:
            LOGGER.warn("Unknown attribute type: {}", definition.getType());
        }
        return true;
    }
}
