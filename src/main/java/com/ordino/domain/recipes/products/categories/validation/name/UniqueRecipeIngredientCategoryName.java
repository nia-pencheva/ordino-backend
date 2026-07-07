package com.ordino.domain.recipes.products.categories.validation.name;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = UniqueRecipeIngredientCategoryNameValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueRecipeIngredientCategoryName {
    String message() default "Recipe ingredient category name is already taken";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
