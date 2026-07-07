package com.ordino.domain.users.validation.phone_number;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = UniquePhoneNumberValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniquePhoneNumber {
    String message() default "Phone number is already in use";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
