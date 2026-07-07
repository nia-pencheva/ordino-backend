package com.ordino.domain.recipes.validation.draft;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Min(value = 1, message = "Preparation time must be at least 1 minute")
@Max(value = 4320, message = "Preparation time must not exceed 4320 minutes (3 days)")
public @interface ValidDraftPreparationTime {
    String message() default "Invalid preparation time";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
