package com.ordino.domain.suppliers.validation.product_id;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueSupplierProductIdValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueSupplierProductId {
    String message() default "This product is already in the supplier's catalog";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
