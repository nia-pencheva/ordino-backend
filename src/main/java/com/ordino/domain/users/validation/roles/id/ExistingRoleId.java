package com.ordino.domain.users.validation.roles.id;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExistingRoleIdValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistingRoleId {
    String message() default "Role does not exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
