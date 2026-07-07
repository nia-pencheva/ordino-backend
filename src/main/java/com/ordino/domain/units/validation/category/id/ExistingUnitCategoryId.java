package com.ordino.domain.units.validation.category.id;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = ExistingUnitCategoryIdValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistingUnitCategoryId {
    String message() default "Unit category does not exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
