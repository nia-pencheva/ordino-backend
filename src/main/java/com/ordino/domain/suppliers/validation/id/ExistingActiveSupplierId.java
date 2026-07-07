package com.ordino.domain.suppliers.validation.id;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = ExistingActiveSupplierIdValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistingActiveSupplierId {
    String message() default "Supplier does not exist or is not active";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
