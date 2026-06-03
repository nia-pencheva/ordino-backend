package com.ordino.domain.products.validation.name;

import com.ordino.core.util.PathVariablesUtil;
import com.ordino.domain.products.repository.ProductRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueProductNameValidator implements ConstraintValidator<UniqueProductName, String> {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private HttpServletRequest request;

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        if (name == null || name.isBlank()) {
            return true;
        }
        Long excludeId = PathVariablesUtil.extractPathId(request);
        if (excludeId != null) {
            return !productRepository.existsByNameAndIdNot(name, excludeId);
        }
        return !productRepository.existsByName(name);
    }
}
