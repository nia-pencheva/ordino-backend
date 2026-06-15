package com.ordino.domain.recipes.validation;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Documented
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@NotNull(message = "Preparation time is required")
@Min(value = 1, message = "Preparation time must be at least 1 minute")
@Max(value = 4320, message = "Preparation time must not exceed 4320 minutes (3 days)")
public @interface ValidPreparationTime {
    String message() default "Invalid preparation time";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
