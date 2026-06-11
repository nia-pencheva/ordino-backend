package com.ordino.domain.recipes.products.categories.validation.id;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LeafRecipeIngredientCategoryValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LeafRecipeIngredientCategory {
    String message() default "Category must be a leaf category (no subcategories) to contain products";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
