package com.ordino.domain.users.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@NotBlank(message = "Full name is required")
@Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
@Pattern(
    regexp = "^[\\p{IsLatin} '\\-]+$",
    message = "Full name may only contain latin letters, spaces, hyphens and apostrophes"
)
public @interface ValidFullName {
    String message() default "Invalid full name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
