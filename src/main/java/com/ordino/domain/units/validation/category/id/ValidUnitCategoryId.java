package com.ordino.domain.units.validation.category.id;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.lang.annotation.*;

@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@NotNull(message = "Unit category is required")
@Positive(message = "Unit category ID must be a positive number")
@ExistingUnitCategoryId
public @interface ValidUnitCategoryId {
    String message() default "Invalid unit category ID";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
