package com.ordino.domain.products.validation.name;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@NotBlank(message = "Product name is required")
@Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
@Pattern(regexp = "^[\\p{IsLatin} '\\-]+$", message = "Product name may only contain latin letters, spaces, hyphens and apostrophes")
@UniqueProductName(message = "Product name is already taken")
public @interface ValidProductName {
    String message() default "Invalid product name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
