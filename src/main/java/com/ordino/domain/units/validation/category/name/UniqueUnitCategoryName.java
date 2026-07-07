package com.ordino.domain.units.validation.category.name;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = UniqueUnitCategoryNameValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUnitCategoryName {
    String message() default "Unit category name is already taken";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
