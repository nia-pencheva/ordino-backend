package com.ordino.domain.warehouse.products.categories.validation.parentid;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = NonLeafWarehouseProductCategoryParentIdValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NonLeafWarehouseProductCategoryParentId {
    String message() default "Parent category cannot have products assigned to it";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
