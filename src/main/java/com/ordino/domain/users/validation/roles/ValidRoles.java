package com.ordino.domain.users.validation.roles;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidRolesValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@CompatibleRoles(message = "The selected roles contain incompatible combinations")
public @interface ValidRoles {
    String message() default "At least one valid role must be specified";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
