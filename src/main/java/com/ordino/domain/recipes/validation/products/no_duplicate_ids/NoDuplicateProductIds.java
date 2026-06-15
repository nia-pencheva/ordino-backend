package com.ordino.domain.recipes.validation.products.no_duplicate_ids;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = NoDuplicateProductIdsValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoDuplicateProductIds {
    String message() default "The same product cannot appear more than once";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
