package com.ordino.domain.units.validation.unit.abbreviation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueUnitAbbreviationValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUnitAbbreviation {
    String message() default "Unit abbreviation is already taken";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
