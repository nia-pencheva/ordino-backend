package com.ordino.domain.warehouse.products.validation.id;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = ExistingActiveWarehouseProductIdValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistingActiveWarehouseProductId {
    String message() default "Warehouse product does not exist or is not active";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
