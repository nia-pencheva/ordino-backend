package com.ordino.domain.warehouse.products.categories.validation.id;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = LeafWarehouseProductCategoryValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LeafWarehouseProductCategory {
    String message() default "Category must be a leaf category (no subcategories) to contain products";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
