package com.ordino.domain.orders.validation;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = AllOrderProductsReceivedValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AllOrderProductsReceived {
    String message() default "Products list must contain exactly all products from this order";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
