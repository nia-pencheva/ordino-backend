package com.ordino.domain.units.validation.unit;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueUnitNameValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUnitName {
    String message() default "Unit name is already taken";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
