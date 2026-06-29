package com.ordino.domain.warehouse.products.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ordino.core.config.mapping.CustomMapper;
import com.ordino.domain.products.model.entity.Product;
import com.ordino.domain.products.repository.ProductRepository;
import com.ordino.domain.units.model.entity.Unit;
import com.ordino.domain.units.repository.UnitRepository;
import com.ordino.domain.warehouse.products.model.dto.WarehouseProductResponseDTO;
import com.ordino.domain.warehouse.products.model.dto.add.AddProductToWarehouseRequestDTO;
import com.ordino.domain.warehouse.products.model.dto.addable_products_page.WarehouseProductsAddableProductResponseDTO;
import com.ordino.domain.warehouse.products.model.dto.addable_products_page.WarehouseProductsAddableProductsPageResponseDTO;
import com.ordino.domain.warehouse.products.model.dto.save.SaveWarehouseProductRequestDTO;
import com.ordino.domain.warehouse.products.model.dto.warehouse_products_page.WarehouseProductForPageResponseDTO;
import com.ordino.domain.warehouse.products.model.dto.warehouse_products_page.WarehouseProductsPageResponseDTO;
import com.ordino.domain.suppliers.model.entity.SupplierProduct;
import com.ordino.domain.warehouse.products.model.entity.WarehouseProduct;
import com.ordino.domain.warehouse.products.repository.WarehouseProductRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WarehouseProductServiceImpl implements WarehouseProductService {
    private final Integer pageSize = 10;
    private WarehouseProductRepository repository;
    private ProductRepository productRepository;
    private UnitRepository unitRepository;
    private CustomMapper mapper;

    public WarehouseProductsPageResponseDTO getWarehouseProducts(String name, Boolean active, Long warehouseProductCategoryId, Integer page, Integer pageSize) {
        Integer pageNumber = page != null ? page - 1 : 0;
        Integer size = pageSize != null ? pageSize : this.pageSize;
        PageRequest pageRequest = PageRequest.of(pageNumber, size);

        Page<WarehouseProduct> warehouseProductsPage = (name == null)
                                                            ? repository.findAllByActiveAndCategoryId(active, warehouseProductCategoryId, pageRequest)
                                                            : repository.searchByNameAndFilters(name, active, warehouseProductCategoryId, pageRequest);

        WarehouseProductsPageResponseDTO responseDTO = new WarehouseProductsPageResponseDTO();

        responseDTO.setWarehouseProducts(
            warehouseProductsPage
                .stream()
                .map(warehouseProduct -> {
                    WarehouseProductForPageResponseDTO dto = new WarehouseProductForPageResponseDTO();

                    dto.setId(warehouseProduct.getId());
                    dto.setName(warehouseProduct.getProduct().getName());

                    return dto;
                })
                .toList()
        );

        responseDTO.setTotalElements(warehouseProductsPage.getTotalElements());
        responseDTO.setTotalPages(warehouseProductsPage.getTotalPages());

        return responseDTO;
    }

    public WarehouseProductsAddableProductsPageResponseDTO getProductsFromApprovedRecipesToAdd(String name, Integer page, Integer pageSize) {
        Integer pageNumber = page != null ? page - 1 : 0;
        Integer size = pageSize != null ? pageSize : this.pageSize;
        PageRequest pageRequest = PageRequest.of(pageNumber, size);

        Page<Product> productsPage = (name == null)
                                        ? repository.findAllAddableFromApprovedRecipes(pageRequest)
                                        : repository.searchAddableFromApprovedRecipesByName(name, pageRequest);

        return mapAddableProductsPage(productsPage);
    }

    public WarehouseProductResponseDTO getWarehouseProduct(Long id) {
        WarehouseProduct warehouseProduct = repository.findById(id)
                                                        .orElseThrow(() -> new EntityNotFoundException("Warehouse product not found"));

        WarehouseProductResponseDTO responseDTO = new WarehouseProductResponseDTO();

        responseDTO.setProductId(warehouseProduct.getProduct().getId());
        responseDTO.setProductName(warehouseProduct.getProduct().getName());
        responseDTO.setUnitId(warehouseProduct.getUnit().getId());
        responseDTO.setUnitAbbreviation(warehouseProduct.getUnit().getAbbreviation());
        responseDTO.setUnitCategoryName(warehouseProduct.getUnit().getUnitCategory().getCategory());
        responseDTO.setMinQuantity(warehouseProduct.getMinQuantity());
        responseDTO.setActive(warehouseProduct.getActive());
        responseDTO.setDeactivateForbiddenReasons(canBeDeactivated(warehouseProduct));

        return responseDTO;
    }

    public WarehouseProductsAddableProductsPageResponseDTO getAddableProducts(String name, Integer page, Integer pageSize) {
        Integer pageNumber = page != null ? page - 1 : 0;
        Integer size = pageSize != null ? pageSize : this.pageSize;
        PageRequest pageRequest = PageRequest.of(pageNumber, size);

        Page<Product> productsPage = (name == null)
                                        ? repository.findAllAddableProducts(pageRequest)
                                        : repository.searchAddableProductsByName(name, pageRequest);

        return mapAddableProductsPage(productsPage);
    }

    public void addProductToWarehouse(AddProductToWarehouseRequestDTO dto) {
        Product product = productRepository.findById(dto.getProductId())
                                            .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        Unit unit = unitRepository.findById(dto.getUnitId())
                                    .orElseThrow(() -> new EntityNotFoundException("Unit not found"));

        WarehouseProduct warehouseProduct = new WarehouseProduct();

        warehouseProduct.setProduct(product);
        warehouseProduct.setUnit(unit);
        warehouseProduct.setMinQuantity(dto.getMinQuantity());
        warehouseProduct.setActive(true);

        repository.save(warehouseProduct);
    }

    public void saveWarehouseProduct(Long id, SaveWarehouseProductRequestDTO dto) {
        WarehouseProduct warehouseProduct = repository.findById(id)
                                                        .orElseThrow(() -> new EntityNotFoundException("Warehouse product not found"));

        Unit unit = unitRepository.findById(dto.getUnitId())
                                    .orElseThrow(() -> new EntityNotFoundException("Unit not found"));

        warehouseProduct.setUnit(unit);
        warehouseProduct.setMinQuantity(dto.getMinQuantity());

        repository.save(warehouseProduct);
    }

    public void deactivateWarehouseProduct(Long id) {
        WarehouseProduct warehouseProduct = repository.findById(id)
                                                        .orElseThrow(() -> new EntityNotFoundException("Warehouse product not found"));

        warehouseProduct.setActive(false);
        repository.save(warehouseProduct);
    }

    public void activateWarehouseProduct(Long id) {
        WarehouseProduct warehouseProduct = repository.findById(id)
                                                        .orElseThrow(() -> new EntityNotFoundException("Warehouse product not found"));

        warehouseProduct.setActive(true);
        repository.save(warehouseProduct);
    }

    private List<String> canBeDeactivated(WarehouseProduct warehouseProduct) {
        List<String> reasons = new ArrayList<>();

        List<SupplierProduct> supplierProducts = warehouseProduct.getSupplierProducts();
        if (supplierProducts != null && !supplierProducts.isEmpty()) {
            String supplierNames = supplierProducts.stream()
                    .map(sp -> sp.getSupplier().getName())
                    .collect(Collectors.joining(", "));
            reasons.add("Product is in the following suppliers' catalogs - " + supplierNames + ". Please remove it in order to deactivate the product.");
        }

        return reasons;
    }

    private WarehouseProductsAddableProductsPageResponseDTO mapAddableProductsPage(Page<Product> productsPage) {
        WarehouseProductsAddableProductsPageResponseDTO responseDTO = new WarehouseProductsAddableProductsPageResponseDTO();

        responseDTO.setProducts(
            productsPage
                .stream()
                .map(product -> mapper.map(product, WarehouseProductsAddableProductResponseDTO.class))
                .toList()
        );

        responseDTO.setTotalElements(productsPage.getTotalElements());
        responseDTO.setTotalPages(productsPage.getTotalPages());

        return responseDTO;
    }
}
