package com.ordino.domain.recipes.categories.validation.id;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = ExistingRecipeCategoryIdValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistingRecipeCategoryId {
    String message() default "Recipe category does not exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
