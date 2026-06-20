package com.ordino.domain.warehouse.loss_reasons.validation.id;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = ExistingLossReasonIdValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistingLossReasonId {
    String message() default "Loss reason does not exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
