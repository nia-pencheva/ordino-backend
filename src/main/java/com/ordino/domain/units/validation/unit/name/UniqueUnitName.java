package com.ordino.domain.units.validation.unit.name;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = UniqueUnitNameValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUnitName {
    String message() default "Unit name is already taken";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
