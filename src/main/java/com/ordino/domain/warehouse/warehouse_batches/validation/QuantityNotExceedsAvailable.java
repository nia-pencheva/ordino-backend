package com.ordino.domain.warehouse.warehouse_batches.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = QuantityNotExceedsAvailableValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface QuantityNotExceedsAvailable {
    String message() default "Quantity exceeds the available quantity for this batch";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
