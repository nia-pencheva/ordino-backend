package com.ordino.domain.suppliers.validation.product_id;

import com.ordino.core.util.PathVariablesUtil;
import com.ordino.domain.suppliers.repository.SupplierProductRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueSupplierProductIdValidator implements ConstraintValidator<UniqueSupplierProductId, Long> {

    @Autowired
    private SupplierProductRepository supplierProductRepository;

    @Autowired
    private HttpServletRequest request;

    @Override
    public boolean isValid(Long productId, ConstraintValidatorContext context) {
        if (productId == null) {
            return true;
        }
        Long supplierId = PathVariablesUtil.extractPathId(request);
        if (supplierId == null) {
            return true;
        }
        return !supplierProductRepository.existsBySupplierIdAndWarehouseProductId(supplierId, productId);
    }
}
