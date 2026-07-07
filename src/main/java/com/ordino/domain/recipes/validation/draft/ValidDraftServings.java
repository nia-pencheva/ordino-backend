package com.ordino.domain.recipes.validation.draft;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Min(value = 1, message = "Servings must be at least 1")
@Max(value = 255, message = "Servings must not exceed 255")
public @interface ValidDraftServings {
    String message() default "Invalid servings";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
