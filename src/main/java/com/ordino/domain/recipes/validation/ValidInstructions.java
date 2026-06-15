package com.ordino.domain.recipes.validation;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = ValidInstructionsValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidInstructions {
    String message() default "Invalid instructions";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
