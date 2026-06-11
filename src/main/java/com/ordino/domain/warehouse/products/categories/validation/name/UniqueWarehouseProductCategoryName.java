package com.ordino.domain.warehouse.products.categories.validation.name;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueWarehouseProductCategoryNameValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueWarehouseProductCategoryName {
    String message() default "Warehouse product category name is already taken";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
