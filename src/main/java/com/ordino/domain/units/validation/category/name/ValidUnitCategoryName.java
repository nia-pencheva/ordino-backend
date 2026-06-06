package com.ordino.domain.units.validation.category.name;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@NotBlank(message = "Unit category name is required")
@Size(min = 2, max = 50, message = "Unit category name must be between 2 and 50 characters")
@Pattern(regexp = "^[\\p{IsLatin} '\\-]+$", message = "Unit category name may only contain latin letters, spaces, hyphens and apostrophes")
@UniqueUnitCategoryName(message = "Unit category name is already taken")
public @interface ValidUnitCategoryName {
    String message() default "Invalid unit category name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
