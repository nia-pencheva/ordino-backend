package com.ordino.domain.products.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

import com.ordino.core.validation.NullOrNotBlank.NullOrNotBlank;

@Documented
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Size(max = 2000, message = "Notes must not exceed 2000 characters")
@NullOrNotBlank(message = "Notes can be null but not blank")
public @interface ValidProductNotes {
    String message() default "Invalid product notes";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
