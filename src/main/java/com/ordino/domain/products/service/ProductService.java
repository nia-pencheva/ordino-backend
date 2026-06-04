package com.ordino.domain.products.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ordino.core.exception.ForbiddenOperationException;
import com.ordino.domain.products.model.dto.ProductRequestDTO;
import com.ordino.domain.products.model.dto.ProductResponseDTO;
import com.ordino.domain.products.model.dto.ProductsPageResponseDTO;
import com.ordino.domain.products.model.entity.Product;
import com.ordino.domain.products.repository.ProductRepository;
import com.ordino.domain.warehouse.products.model.entity.WarehouseProduct;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductService {
    private final Integer pageSize = 10;
    private final ProductRepository repository;
    private final ModelMapper mapper;

    public ProductsPageResponseDTO getProducts(String name, Boolean active, Integer page) {
        Integer pageNumber = page != null ? page - 1 : 0;
        Boolean activeOnly = active != null ? active : true;
        PageRequest pageRequest = PageRequest.of(pageNumber, this.pageSize);
        
        Page<Product> productsPage = (name == null) 
                                ? repository.findByActive(activeOnly, pageRequest)
                                : repository.searchByNameAndActive(name, activeOnly, pageRequest);

        ProductsPageResponseDTO responseDTO = new ProductsPageResponseDTO();

        responseDTO.setProducts(
            productsPage
                .stream()
                .map(product -> {
                    ProductResponseDTO dto = mapper.map(product, ProductResponseDTO.class);

                    dto.setDeactivateForbiddenReasons(canBeDeactivated(product));
                    dto.setDeleteForbiddenReasons(canBeDeleted(product));

                    return dto;
                })
                .toList()
        );

        responseDTO.setTotalElements(productsPage.getTotalElements());
        responseDTO.setTotalPages(productsPage.getTotalPages());

        return responseDTO;
    }

    public ProductResponseDTO getProduct(Long id) throws EntityNotFoundException {
        Product product = repository.findById(id)
                                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        return mapper.map(product, ProductResponseDTO.class);

    }

    public void addProduct(ProductRequestDTO dto) {
        repository.save(mapper.map(dto, Product.class));
    }

    public void saveProduct(Long id, ProductRequestDTO dto) throws EntityNotFoundException {
        Product product = repository.findById(id)
                                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        mapper.map(dto, product);
        repository.save(product);
    }

    public void activateProduct(Long id) throws EntityNotFoundException {
        Product product = repository.findById(id)
                                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        product.setActive(true);
        repository.save(product);
    }

    @Transactional
    public void deactivateProduct(Long id) throws EntityNotFoundException, ForbiddenOperationException {
        Product product = repository.findById(id)
                                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        List<String> deactivateForbiddenReasons = canBeDeactivated(product);

        if(!deactivateForbiddenReasons.isEmpty()) 
            throw new ForbiddenOperationException(deactivateForbiddenReasons);

        product.setActive(false);
        repository.save(product);
    }

    @Transactional
    public void deleteProduct(Long id) throws EntityNotFoundException, ForbiddenOperationException {
        Product product = repository.findById(id)
                                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        List<String> deleteForbiddenReasons = canBeDeleted(product);
        if(!deleteForbiddenReasons.isEmpty())
            throw new ForbiddenOperationException(deleteForbiddenReasons);

        repository.delete(product);
    }

    private List<String> canBeDeactivated(Product product) throws ForbiddenOperationException {
        List<String> reasons = new ArrayList<>();

        WarehouseProduct warehouseProduct = product.getWarehouseProduct();
        if (warehouseProduct != null && warehouseProduct.getActive()) {
            reasons.add("Cannot deactivate a product that is active in the warehouse");
        }

        boolean inApprovedRecipe = product.getRecipeProducts().stream()
                .anyMatch(rp -> "APPROVED".equals(rp.getRecipe().getRecipeStatus().getStatus()));
        if (inApprovedRecipe) {
            reasons.add("Cannot deactivate a product used in an approved recipe");
        }

        return reasons;
    }

    private List<String> canBeDeleted(Product product) throws ForbiddenOperationException {
        List<String> reasons = new ArrayList<>();

        if (!product.getRecipeProducts().isEmpty()) {
            reasons.add("Cannot delete a product that is used in a recipe");
        }

        if (product.getWarehouseProduct() != null) {
            reasons.add("Cannot delete a product that exists in the warehouse");
        }

        return reasons;
    }
}
