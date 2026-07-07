package com.ordino.domain.recipes.categories.validation.name;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@NotBlank(message = "Category name is required")
@Size(min = 2, max = 100, message = "Category name must be between 2 and 100 characters")
@Pattern(regexp = "^[\\p{IsLatin} '\\-&(),./]+$", message = "Category name may only contain latin letters, spaces, hyphens and apostrophes")
@UniqueRecipeCategoryName(message = "Category name is already taken")
public @interface ValidRecipeCategoryName {
    String message() default "Invalid category name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
