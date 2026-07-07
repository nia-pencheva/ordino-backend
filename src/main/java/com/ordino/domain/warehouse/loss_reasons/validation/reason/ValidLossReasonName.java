package com.ordino.domain.warehouse.loss_reasons.validation.reason;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@NotBlank(message = "Loss reason is required")
@Size(min = 2, max = 50, message = "Loss reason must be between 2 and 50 characters")
@Pattern(regexp = "^[\\p{IsLatin} '\\-&(),./]+$", message = "Loss reason may only contain latin letters, spaces, hyphens and apostrophes")
@UniqueLossReasonName(message = "Loss reason is already taken")
public @interface ValidLossReasonName {
    String message() default "Invalid loss reason";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
