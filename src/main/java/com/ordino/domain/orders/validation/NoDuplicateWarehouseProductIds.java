package com.ordino.domain.orders.validation;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = NoDuplicateWarehouseProductIdsValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoDuplicateWarehouseProductIds {
    String message() default "The same warehouse product cannot appear more than once";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
