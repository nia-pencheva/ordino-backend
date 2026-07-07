package com.ordino.domain.recipes.validation;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@NotBlank(message = "Title is required")
@Size(max = 250, message = "Title must not exceed 250 characters")
public @interface ValidTitle {
    String message() default "Invalid title";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
