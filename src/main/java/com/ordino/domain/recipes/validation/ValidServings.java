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
@NotNull(message = "Servings is required")
@Min(value = 1, message = "Servings must be at least 1")
@Max(value = 255, message = "Servings must not exceed 255")
public @interface ValidServings {
    String message() default "Invalid servings";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
