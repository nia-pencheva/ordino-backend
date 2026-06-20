package com.ordino.domain.warehouse.products.validation.id;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExistingWarehouseProductIdValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistingWarehouseProductId {
    String message() default "Warehouse product does not exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
