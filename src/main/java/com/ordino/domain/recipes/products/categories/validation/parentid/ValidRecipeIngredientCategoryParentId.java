package com.ordino.domain.recipes.products.categories.validation.parentid;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Positive;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Positive(message = "Parent category ID must be a positive number")
@ExistingRecipeIngredientCategoryParentId
@NonLeafRecipeIngredientCategoryParentId
public @interface ValidRecipeIngredientCategoryParentId {
    String message() default "Invalid parent category ID";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
