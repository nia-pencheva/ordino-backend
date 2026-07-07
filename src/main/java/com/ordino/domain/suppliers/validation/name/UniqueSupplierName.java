package com.ordino.domain.suppliers.validation.name;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = UniqueSupplierNameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueSupplierName {
    String message() default "Supplier name is already in use";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
