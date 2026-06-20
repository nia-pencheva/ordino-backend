package com.ordino.domain.orders.validation;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.ordino.core.util.PathVariablesUtil;
import com.ordino.domain.orders.model.dto.receive.ReceiveOrderRequestProductDTO;
import com.ordino.domain.orders.repository.OrderProductRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AllOrderProductsReceivedValidator
        implements ConstraintValidator<AllOrderProductsReceived, List<ReceiveOrderRequestProductDTO>> {

    private final OrderProductRepository orderProductRepository;
    private final HttpServletRequest request;

    @Override
    public boolean isValid(List<ReceiveOrderRequestProductDTO> products, ConstraintValidatorContext context) {
        if (products == null) return true;

        Long orderId = PathVariablesUtil.extractPathId(request);
        if (orderId == null) return true;

        Set<Long> expected = orderProductRepository.findWarehouseProductIdsByOrderId(orderId);

        Set<Long> provided = products.stream()
                .map(ReceiveOrderRequestProductDTO::getWarehouseProductId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (expected.equals(provided)) return true;

        context.disableDefaultConstraintViolation();

        if (!provided.stream().allMatch(expected::contains)) {
            context.buildConstraintViolationWithTemplate("Products list contains products that do not belong to this order")
                    .addConstraintViolation();
        }

        if (!expected.stream().allMatch(provided::contains)) {
            context.buildConstraintViolationWithTemplate("Not all products from this order are included")
                    .addConstraintViolation();
        }

        return false;
    }
}
