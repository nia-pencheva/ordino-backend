package com.ordino.domain.warehouse.products.validation.product_id;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueWarehouseProductProductIdValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueWarehouseProductProductId {
    String message() default "Product already exists in the warehouse";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
