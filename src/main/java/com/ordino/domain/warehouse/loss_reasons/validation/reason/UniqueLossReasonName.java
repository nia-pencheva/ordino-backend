package com.ordino.domain.warehouse.loss_reasons.validation.reason;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueLossReasonNameValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueLossReasonName {
    String message() default "Loss reason is already taken";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
