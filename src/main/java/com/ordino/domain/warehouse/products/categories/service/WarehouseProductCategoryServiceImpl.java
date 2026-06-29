package com.ordino.domain.warehouse.products.categories.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ordino.core.config.mapping.CustomMapper;
import com.ordino.domain.products.model.entity.Product;
import com.ordino.domain.products.repository.ProductRepository;
import com.ordino.domain.warehouse.products.categories.model.dto.all_categories.WarehouseProductCategoryForAllListResponseDTO;
import com.ordino.domain.warehouse.products.categories.model.dto.all_categories.WarehouseProductCategoryProductForAllListResponseDTO;
import com.ordino.domain.warehouse.products.categories.model.dto.edit.EditWarehouseProductCategoryResponseDTO;
import com.ordino.domain.warehouse.products.categories.model.dto.move.MoveWarehouseProductCategoryRequestDTO;
import com.ordino.domain.warehouse.products.categories.model.dto.products.WarehouseProductCategoryAddableProductResponseDTO;
import com.ordino.domain.warehouse.products.categories.model.dto.products.WarehouseProductCategoryAddableProductsPageResponseDTO;
import com.ordino.domain.warehouse.products.categories.model.dto.products.add.AddProductToWarehouseProductCategoryRequestDTO;
import com.ordino.domain.warehouse.products.categories.model.dto.save.SaveWarehouseProductCategoryRequestDTO;
import com.ordino.domain.warehouse.products.categories.model.entity.WarehouseProductCategory;
import com.ordino.domain.warehouse.products.categories.repository.WarehouseProductCategoryRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WarehouseProductCategoryServiceImpl implements WarehouseProductCategoryService {
    private WarehouseProductCategoryRepository repository;
    private ProductRepository productRepository;
    private CustomMapper mapper;

    public List<WarehouseProductCategoryForAllListResponseDTO> getAllCategories() {
        List<WarehouseProductCategory> categories = repository.findAll();

        return categories.stream()
                        .filter(c -> c.getParentCategory() == null)
                        .map(category -> mapCategoryForAllListResponse(category))
                        .toList();
    }

    public WarehouseProductCategoryForAllListResponseDTO mapCategoryForAllListResponse(WarehouseProductCategory category) {
        WarehouseProductCategoryForAllListResponseDTO dto = new WarehouseProductCategoryForAllListResponseDTO();

        dto.setId(category.getId());
        dto.setCategory(category.getCategory());

        dto.setSubCategories(
            category
                .getSubCategories()
                .stream()
                .map(subCategory -> mapCategoryForAllListResponse(subCategory))
                .toList()
        );

        dto.setProducts(
            category
                .getProducts()
                .stream()
                .map(product -> mapper.map(product, WarehouseProductCategoryProductForAllListResponseDTO.class))
                .toList()
        );

        return dto;
    }

    public void addCategory(SaveWarehouseProductCategoryRequestDTO dto) {
        WarehouseProductCategory category = new WarehouseProductCategory();

        category.setCategory(dto.getCategory());
        if (dto.getParentId() == null) {
            category.setParentCategory(null);
        } else {
            WarehouseProductCategory parent = repository.findById(dto.getParentId())
                                                        .orElseThrow(() -> new EntityNotFoundException("Warehouse product category not found"));
            category.setParentCategory(parent);
        }

        repository.save(category);
    }

    public EditWarehouseProductCategoryResponseDTO getCategoryForEditing(Long id) {
        WarehouseProductCategory category = repository.findById(id)
                                                        .orElseThrow(() -> new EntityNotFoundException("Warehouse product category not found"));

        return mapper.map(category, EditWarehouseProductCategoryResponseDTO.class);
    }

    public void saveCategory(Long id, SaveWarehouseProductCategoryRequestDTO dto) throws EntityNotFoundException {
        WarehouseProductCategory category = repository.findById(id)
                                                        .orElseThrow(() -> new EntityNotFoundException("Warehouse product category not found"));

        category.setCategory(dto.getCategory());
        if (dto.getParentId() == null) {
            category.setParentCategory(null);
        } else {
            WarehouseProductCategory parent = repository.findById(dto.getParentId())
                                                        .orElseThrow(() -> new EntityNotFoundException("Warehouse product category not found"));
            category.setParentCategory(parent);
        }

        repository.save(category);
    }

    public void moveCategory(Long id, MoveWarehouseProductCategoryRequestDTO dto) {
        WarehouseProductCategory category = repository.findById(id)
                                                    .orElseThrow(() -> new EntityNotFoundException("Warehouse product category not found"));

        if (dto.getParentId() == null) {
            category.setParentCategory(null);
        } else {
            WarehouseProductCategory parent = repository.findById(dto.getParentId())
                                                        .orElseThrow(() -> new EntityNotFoundException("Warehouse product category not found"));
            category.setParentCategory(parent);
        }

        repository.save(category);
    }

    public WarehouseProductCategoryAddableProductsPageResponseDTO getAddableProducts(Long id, String name, Integer page, Integer pageSize) {
        Integer pageNumber = page != null ? page - 1 : 0;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        Page<Product> productsPage = (name == null)
                                        ? repository.findAllAddableProducts(id, pageRequest)
                                        : repository.searchByAddableProductsName(id, name, pageRequest);

        WarehouseProductCategoryAddableProductsPageResponseDTO responseDTO = new WarehouseProductCategoryAddableProductsPageResponseDTO();

        responseDTO.setProducts(
            productsPage
                .stream()
                .map(product -> mapper.map(product, WarehouseProductCategoryAddableProductResponseDTO.class))
                .toList()
        );

        responseDTO.setTotalElements(productsPage.getTotalElements());
        responseDTO.setTotalPages(productsPage.getTotalPages());

        return responseDTO;
    }

    public void addProductToCategory(Long id, AddProductToWarehouseProductCategoryRequestDTO dto) {
        WarehouseProductCategory category = repository.findById(id)
                                                        .orElseThrow(() -> new EntityNotFoundException("Warehouse product category not found"));

        Product product = productRepository.findById(dto.getProductId())
                                                .orElseThrow(() -> new EntityNotFoundException("Product that should be added does not exist"));

        category.getProducts().add(product);
        repository.save(category);
    }

    public void removeProductFromCategory(Long id, Long productId) {
        WarehouseProductCategory category = repository.findById(id)
                                                        .orElseThrow(() -> new EntityNotFoundException("Warehouse product category not found"));

        Product product = productRepository.findById(productId)
                                            .orElseThrow(() -> new EntityNotFoundException("Product does not belong to category"));

        category.getProducts().remove(product);
        repository.save(category);
    }

    public void deleteCategory(Long id) throws EntityNotFoundException {
        WarehouseProductCategory category = repository.findById(id)
                                                        .orElseThrow(() -> new EntityNotFoundException("Warehouse product category not found"));

        repository.delete(category);
    }
}
