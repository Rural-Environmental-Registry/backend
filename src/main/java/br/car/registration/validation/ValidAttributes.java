package br.car.registration.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

import br.car.registration.enums.EntityTypesEnum;

@Documented
@Constraint(validatedBy = AttributeListValidator.class) // Link to Validator
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAttributes {
    String message() default "Invalid attributes list";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    EntityTypesEnum entity();
}
