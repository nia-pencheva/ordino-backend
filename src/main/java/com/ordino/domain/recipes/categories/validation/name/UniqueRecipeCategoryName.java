package com.ordino.domain.recipes.categories.validation.name;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = UniqueRecipeCategoryNameValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueRecipeCategoryName {
    String message() default "Category name is already taken";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
