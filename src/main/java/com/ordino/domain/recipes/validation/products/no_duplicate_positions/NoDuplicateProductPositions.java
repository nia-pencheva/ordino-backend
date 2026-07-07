package com.ordino.domain.recipes.validation.products.no_duplicate_positions;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = NoDuplicateProductPositionsValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoDuplicateProductPositions {
    String message() default "Each product must have a unique position";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
