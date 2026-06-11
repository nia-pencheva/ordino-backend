package com.ordino.domain.recipes.products.categories.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ordino.core.config.mapping.CustomMapper;
import com.ordino.domain.products.model.entity.Product;
import com.ordino.domain.products.repository.ProductRepository;
import com.ordino.domain.recipes.products.categories.model.dto.all_categories.RecipeIngredientCategoryForAllListResponseDTO;
import com.ordino.domain.recipes.products.categories.model.dto.all_categories.RecipeIngredientCategoryProductForAllListResponseDTO;
import com.ordino.domain.recipes.products.categories.model.dto.edit.EditRecipeIngredientCategoryResponseDTO;
import com.ordino.domain.recipes.products.categories.model.dto.move.MoveRecipeIngredientCategoryRequestDTO;
import com.ordino.domain.recipes.products.categories.model.dto.products.RecipeIngredientCategoryAddableProductResponseDTO;
import com.ordino.domain.recipes.products.categories.model.dto.products.RecipeIngredientCategoryAddableProductsPageResponseDTO;
import com.ordino.domain.recipes.products.categories.model.dto.products.add.AddProductToRecipeIngredientCategoryRequestDTO;
import com.ordino.domain.recipes.products.categories.model.dto.save.SaveRecipeIngredientCategoryRequestDTO;
import com.ordino.domain.recipes.products.categories.model.entity.RecipeIngredientCategory;
import com.ordino.domain.recipes.products.categories.repository.RecipeIngredientCategoryRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RecipeIngredientCategoryService {
    private RecipeIngredientCategoryRepository repository;
    private ProductRepository productRepository;
    private CustomMapper mapper;

    public List<RecipeIngredientCategoryForAllListResponseDTO> getAllCategories() {
        List<RecipeIngredientCategory> categories = repository.findAll();

        return categories.stream()
                        .filter(c -> c.getParentCategory() == null)
                        .map(category -> mapCategoryForAllListResponse(category))
                        .toList();

    }

    public RecipeIngredientCategoryForAllListResponseDTO mapCategoryForAllListResponse(RecipeIngredientCategory category) {
        RecipeIngredientCategoryForAllListResponseDTO dto = new RecipeIngredientCategoryForAllListResponseDTO();

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
                .map(product -> mapper.map(product, RecipeIngredientCategoryProductForAllListResponseDTO.class))
                .toList()
        );

        return dto;
    }

    public void addCategory(SaveRecipeIngredientCategoryRequestDTO dto) {
        RecipeIngredientCategory category = new RecipeIngredientCategory();

        category.setCategory(dto.getCategory());
        if (dto.getParentId() == null) {
            category.setParentCategory(null);
        } else {
            RecipeIngredientCategory parent = repository.findById(dto.getParentId())
                                                        .orElseThrow(() -> new EntityNotFoundException("Recipe ingredient category not found"));
            category.setParentCategory(parent);
        }

       repository.save(category);
    }

    public EditRecipeIngredientCategoryResponseDTO getCategoryForEditing(Long id) {
        RecipeIngredientCategory category = repository.findById(id)
                                                        .orElseThrow(() -> new EntityNotFoundException("Recipe ingredient category not found"));

        return mapper.map(category, EditRecipeIngredientCategoryResponseDTO.class);
    }

    public void saveCategory(Long id, SaveRecipeIngredientCategoryRequestDTO dto) throws EntityNotFoundException {
        RecipeIngredientCategory category = repository.findById(id)
                                                        .orElseThrow(() -> new EntityNotFoundException("Recipe ingredient category not found"));

        category.setCategory(dto.getCategory());
        if (dto.getParentId() == null) {
            category.setParentCategory(null);
        } else {
            RecipeIngredientCategory parent = repository.findById(dto.getParentId())
                                                        .orElseThrow(() -> new EntityNotFoundException("Recipe ingredient category not found"));
            category.setParentCategory(parent);
        }
        
        repository.save(category);
    }

    public void moveCategory(Long id, MoveRecipeIngredientCategoryRequestDTO dto) {
        RecipeIngredientCategory category = repository.findById(id)
                                                    .orElseThrow(() -> new EntityNotFoundException("Recipe ingredient category not found"));

        if (dto.getParentId() == null) {
            category.setParentCategory(null);
        } else {
            RecipeIngredientCategory parent = repository.findById(dto.getParentId())
                                                        .orElseThrow(() -> new EntityNotFoundException("Recipe ingredient category not found"));
            category.setParentCategory(parent);
        }

        repository.save(category);
    }

    public RecipeIngredientCategoryAddableProductsPageResponseDTO getAddableProducts(Long id, String name, Integer page, Integer pageSize) {
        Integer pageNumber = page != null ? page - 1 : 0;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        Page<Product> productsPage = (name == null)
                                        ? repository.findAllAddableProducts(id, pageRequest)
                                        : repository.searchByAddableProductsName(id, name, pageRequest);

        RecipeIngredientCategoryAddableProductsPageResponseDTO responseDTO = new RecipeIngredientCategoryAddableProductsPageResponseDTO();

        responseDTO.setProducts(
            productsPage
                .stream()
                .map(product -> mapper.map(product, RecipeIngredientCategoryAddableProductResponseDTO.class))
                .toList()  
        );
        
        responseDTO.setTotalElements(productsPage.getTotalElements());
        responseDTO.setTotalPages(productsPage.getTotalPages());

        return responseDTO;
    }

    public void addProductToCategory(Long id, AddProductToRecipeIngredientCategoryRequestDTO dto) {
        RecipeIngredientCategory category = repository.findById(id)
                                                        .orElseThrow(() -> new EntityNotFoundException("Recipe ingredient category not found"));

        Product product = productRepository.findById(dto.getProductId())
                                                .orElseThrow(() -> new EntityNotFoundException("Product that should be added does not exist"));

        category.getProducts().add(product);
        repository.save(category);
    }

    public void removeProductFromCategory(Long id, Long productId) {
        RecipeIngredientCategory category = repository.findById(id)
                                                        .orElseThrow(() -> new EntityNotFoundException("Recipe ingredient category not found"));

        Product product = productRepository.findById(productId)
                                            .orElseThrow(() -> new EntityNotFoundException("Product does not belong to category"));
                      
        category.getProducts().remove(product);
        repository.save(category);
    }

    public void deleteCategory(Long id) throws EntityNotFoundException {
        RecipeIngredientCategory category = repository.findById(id)
                                                        .orElseThrow(() -> new EntityNotFoundException("Recipe ingredient category not found"));

        repository.delete(category);
    }
}
