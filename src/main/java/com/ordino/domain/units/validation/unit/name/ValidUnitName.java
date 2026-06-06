package com.ordino.domain.units.validation.unit.name;

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
@NotBlank(message = "Unit name is required")
@Size(min = 2, max = 50, message = "Unit name must be between 2 and 50 characters")
@Pattern(regexp = "^[\\p{IsLatin} '\\-]+$", message = "Unit name may only contain latin letters, spaces, hyphens and apostrophes")
@UniqueUnitName(message = "Unit name is already taken")
public @interface ValidUnitName {
    String message() default "Invalid unit name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
