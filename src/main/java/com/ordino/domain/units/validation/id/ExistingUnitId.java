package com.ordino.domain.units.validation.id;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = ExistingUnitIdValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistingUnitId {
    String message() default "Unit does not exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
