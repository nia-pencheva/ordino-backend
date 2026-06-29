package com.ordino.domain.products.service;

import com.ordino.core.exception.ForbiddenOperationException;
import com.ordino.domain.products.model.dto.ProductRequestDTO;
import com.ordino.domain.products.model.dto.ProductResponseDTO;
import com.ordino.domain.products.model.dto.ProductsPageResponseDTO;

import jakarta.persistence.EntityNotFoundException;

public interface ProductService {
    ProductsPageResponseDTO getProducts(String name, Boolean active, Integer page);

    ProductResponseDTO getProduct(Long id) throws EntityNotFoundException;

    void addProduct(ProductRequestDTO dto);

    void saveProduct(Long id, ProductRequestDTO dto) throws EntityNotFoundException;

    void activateProduct(Long id) throws EntityNotFoundException;

    void deactivateProduct(Long id) throws EntityNotFoundException, ForbiddenOperationException;

    void deleteProduct(Long id) throws EntityNotFoundException, ForbiddenOperationException;
}
