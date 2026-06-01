package com.ordino.domain.users.validation.roles;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CompatibleRolesValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CompatibleRoles {
    String message() default "The selected roles contain incompatible combinations";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
