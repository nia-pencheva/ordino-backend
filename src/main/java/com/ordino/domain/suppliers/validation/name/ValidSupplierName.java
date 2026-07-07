package com.ordino.domain.suppliers.validation.name;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@NotBlank(message = "Name is required")
@Size(max = 200, message = "Name must not exceed 200 characters")
public @interface ValidSupplierName {
    String message() default "Invalid supplier name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
