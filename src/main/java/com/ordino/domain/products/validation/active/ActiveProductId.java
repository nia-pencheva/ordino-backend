package com.ordino.domain.products.validation.active;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ActiveProductIdValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ActiveProductId {
    String message() default "Product is not active";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
