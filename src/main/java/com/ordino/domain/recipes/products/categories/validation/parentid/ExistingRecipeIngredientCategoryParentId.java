package com.ordino.domain.recipes.products.categories.validation.parentid;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = ExistingRecipeIngredientCategoryParentIdValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistingRecipeIngredientCategoryParentId {
    String message() default "Parent category does not exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
