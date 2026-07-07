package com.ordino.domain.recipes.validation.categories.no_duplicate_ids;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = NoDuplicateCategoryIdsValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoDuplicateCategoryIds {
    String message() default "The same category cannot appear more than once";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
