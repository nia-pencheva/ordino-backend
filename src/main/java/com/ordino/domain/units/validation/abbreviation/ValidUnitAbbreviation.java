package com.ordino.domain.units.validation.abbreviation;

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
@NotBlank(message = "Unit abbreviation is required")
@Size(min = 1, max = 30, message = "Unit abbreviation must be between 1 and 30 characters")
@Pattern(regexp = "^[\\p{IsLatin} '\\-]+$", message = "Unit abbreviation may only contain latin letters, spaces, hyphens and apostrophes")
@UniqueUnitAbbreviation(message = "Unit abbreviation is already taken")
public @interface ValidUnitAbbreviation {
    String message() default "Invalid unit abbreviation";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
