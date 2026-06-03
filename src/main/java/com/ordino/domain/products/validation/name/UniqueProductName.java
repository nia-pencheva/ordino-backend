package com.ordino.domain.products.validation.name;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueProductNameValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueProductName {
    String message() default "Product name is already taken";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
