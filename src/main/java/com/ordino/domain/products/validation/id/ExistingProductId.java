package com.ordino.domain.products.validation.id;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExistingProductIdValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistingProductId {
    String message() default "Product does not exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
