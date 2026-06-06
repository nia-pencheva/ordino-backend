package com.ordino.core.validation;

import com.ordino.core.util.PathVariablesUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.annotation.Annotation;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class UniquePropertyValidator<A extends Annotation> implements ConstraintValidator<A, String> {

    @Autowired
    private HttpServletRequest request;

    protected abstract boolean existsByProperty(String value);

    protected abstract boolean existsByPropertyAndIdNot(String value, Long excludeId);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        Long excludeId = PathVariablesUtil.extractPathId(request);
        if (excludeId != null) {
            return !existsByPropertyAndIdNot(value, excludeId);
        }
        return !existsByProperty(value);
    }
}
