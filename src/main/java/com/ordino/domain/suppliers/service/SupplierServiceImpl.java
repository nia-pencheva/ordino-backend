package com.ordino.domain.suppliers.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ordino.core.config.mapping.CustomMapper;
import com.ordino.core.exception.ForbiddenOperationException;
import com.ordino.core.exception.ValidationException;
import com.ordino.domain.suppliers.model.dto.SupplierProductInfoResponseDTO;
import com.ordino.domain.suppliers.model.dto.SupplierProductResponseDTO;
import com.ordino.domain.suppliers.model.dto.SupplierResponseDTO;
import com.ordino.domain.suppliers.model.dto.add.AddSupplierRequestDTO;
import com.ordino.domain.suppliers.model.dto.addable_products_page.SuppliersAddableWarehouseProductResponseDTO;
import com.ordino.domain.suppliers.model.dto.addable_products_page.SuppliersAddableWarehouseProductsPageResponseDTO;
import com.ordino.domain.suppliers.model.dto.products.AddSupplierProductRequestDTO;
import com.ordino.domain.suppliers.model.dto.products.SaveSupplierProductRequestDTO;
import com.ordino.domain.suppliers.model.dto.save.SaveSupplierRequestDTO;
import com.ordino.domain.suppliers.model.dto.supplier_orders.SupplierOrderForPageResponseDTO;
import com.ordino.domain.suppliers.model.dto.supplier_orders.SupplierOrdersPageResponseDTO;
import com.ordino.domain.suppliers.model.dto.supplier_products_page.SupplierProductsPageResponseDTO;
import com.ordino.domain.suppliers.model.dto.suppliers_page.SupplierForPageResponseDTO;
import com.ordino.domain.suppliers.model.dto.suppliers_page.SuppliersPageResponseDTO;
import com.ordino.domain.suppliers.model.entity.Supplier;
import com.ordino.domain.suppliers.model.entity.SupplierProduct;
import com.ordino.domain.orders.model.entity.Order;
import com.ordino.domain.orders.repository.OrderRepository;
import com.ordino.domain.orders.repository.OrderStatusRepository;
import com.ordino.domain.suppliers.repository.SupplierProductRepository;
import com.ordino.domain.suppliers.repository.SupplierRepository;
import com.ordino.domain.warehouse.products.model.entity.WarehouseProduct;
import com.ordino.domain.warehouse.products.repository.WarehouseProductRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SupplierServiceImpl implements SupplierService {
    private final Integer pageSize = 10;
    private final SupplierRepository supplierRepository;
    private final SupplierProductRepository supplierProductRepository;
    private final OrderRepository orderRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final WarehouseProductRepository warehouseProductRepository;
    private final CustomMapper mapper;

    public SuppliersPageResponseDTO getSuppliers(String search, String criteria, Boolean active, Integer page, Integer pageSize) {
        Integer pageNumber = page != null ? page - 1 : 0;
        Integer size = pageSize != null ? pageSize : this.pageSize;
        PageRequest pageRequest = PageRequest.of(pageNumber, size);

        Page<Supplier> suppliersPage;

        if (search == null || search.isBlank()) {
            suppliersPage = supplierRepository.findAllByActive(active, pageRequest);
        } else {
            suppliersPage = switch (criteria) {
                case "name" -> supplierRepository.searchByName(search, active, pageRequest);
                case "address" -> supplierRepository.searchByAddress(search, active, pageRequest);
                case "email" -> supplierRepository.searchByEmail(search, active, pageRequest);
                case "phoneNumber" -> supplierRepository.searchByPhoneNumber(search, active, pageRequest);
                default -> throw new ValidationException("criteria", "Invalid search criteria. Must be one of: name, address, email, phoneNumber");
            };
        }

        SuppliersPageResponseDTO responseDTO = new SuppliersPageResponseDTO();

        responseDTO.setSuppliers(
            suppliersPage
                .stream()
                .map(supplier -> {
                    SupplierForPageResponseDTO dto = new SupplierForPageResponseDTO();
                    dto.setId(supplier.getId());
                    dto.setName(supplier.getName());
                    return dto;
                })
                .toList()
        );

        responseDTO.setTotalElements(suppliersPage.getTotalElements());
        responseDTO.setTotalPages(suppliersPage.getTotalPages());

        return responseDTO;
    }

    public SupplierResponseDTO getSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                                                .orElseThrow(() -> new EntityNotFoundException("Supplier not found"));

        SupplierResponseDTO responseDTO = mapper.map(supplier, SupplierResponseDTO.class);
        responseDTO.setDeleteForbiddenReasons(canBeDeleted(supplier));

        return responseDTO;
    }

    public SupplierProductInfoResponseDTO getSupplierProduct(Long supplierId, Long supplierProductId) {
        SupplierProduct supplierProduct = supplierProductRepository.findById(supplierProductId)
                                                                   .orElseThrow(() -> new EntityNotFoundException("Supplier product not found"));

        if (!supplierProduct.getSupplier().getId().equals(supplierId)) {
            throw new EntityNotFoundException("Supplier product not found");
        }

        SupplierProductResponseDTO productDTO = new SupplierProductResponseDTO();
        productDTO.setId(supplierProduct.getId());
        productDTO.setWarehouseProductId(supplierProduct.getWarehouseProduct().getId());
        productDTO.setProductName(supplierProduct.getWarehouseProduct().getProduct().getName());
        productDTO.setUnitAbbreviation(supplierProduct.getWarehouseProduct().getUnit().getAbbreviation());
        productDTO.setPrice(supplierProduct.getPrice());
        productDTO.setMinOrderQuantity(supplierProduct.getMinOrderQuantity());

        SupplierProductInfoResponseDTO responseDTO = new SupplierProductInfoResponseDTO();
        responseDTO.setProduct(productDTO);
        responseDTO.setSupplierName(supplierProduct.getSupplier().getName());

        return responseDTO;
    }

    public SupplierOrdersPageResponseDTO getSupplierProductOrders(Long supplierId, Long supplierProductId, Integer page, Integer pageSize, String from, String to, String orderStatus, String timeField) {
        SupplierProduct supplierProduct = supplierProductRepository.findById(supplierProductId)
                                                                   .orElseThrow(() -> new EntityNotFoundException("Supplier product not found"));

        if (!supplierProduct.getSupplier().getId().equals(supplierId)) {
            throw new EntityNotFoundException("Supplier product not found");
        }

        if (orderStatus != null) {
            orderStatusRepository.findByStatus(orderStatus)
                    .orElseThrow(() -> new EntityNotFoundException("Order status not found"));
        }

        Integer pageNumber = page != null ? page - 1 : 0;
        Integer size = pageSize != null ? pageSize : this.pageSize;
        PageRequest pageRequest = PageRequest.of(pageNumber, size);

        Instant fromInstant = from != null ? Instant.parse(from) : null;
        Instant toInstant = to != null ? Instant.parse(to) : null;

        Long warehouseProductId = supplierProduct.getWarehouseProduct().getId();
        Page<Order> ordersPage = "receivedAt".equals(timeField)
            ? orderRepository.findBySupplierIdAndWarehouseProductIdAndReceivedAtBetweenAndOrderStatus(supplierId, warehouseProductId, fromInstant, toInstant, orderStatus, pageRequest)
            : orderRepository.findBySupplierIdAndWarehouseProductIdAndCreatedAtBetweenAndOrderStatus(supplierId, warehouseProductId, fromInstant, toInstant, orderStatus, pageRequest);

        SupplierOrdersPageResponseDTO responseDTO = new SupplierOrdersPageResponseDTO();

        responseDTO.setOrders(
            ordersPage.stream()
                      .map(order -> {
                          SupplierOrderForPageResponseDTO dto = new SupplierOrderForPageResponseDTO();
                          dto.setId(order.getId());
                          dto.setCreatedAt(order.getCreatedAt());
                          return dto;
                      })
                      .toList()
        );

        responseDTO.setTotalElements(ordersPage.getTotalElements());
        responseDTO.setTotalPages(ordersPage.getTotalPages());

        return responseDTO;
    }

    public SupplierProductsPageResponseDTO getSupplierProducts(Long id, String name, Long warehouseProductCategoryId, Integer page, Integer pageSize) {
        supplierRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Supplier not found"));
                                                
        Integer pageNumber = page != null ? page - 1 : 0;
        Integer size = pageSize != null ? pageSize : this.pageSize;
        PageRequest pageRequest = PageRequest.of(pageNumber, size);

        Page<SupplierProduct> productsPage = (name == null) 
                                                    ? supplierProductRepository.findAllFilteredByWarehouseProductCategoryBySupplier(warehouseProductCategoryId, id, pageRequest)
                                                    : supplierProductRepository.searchByProductNameAndFilteredByWarehouseProductCategoryBySupplier(name, warehouseProductCategoryId, id, pageRequest);

        SupplierProductsPageResponseDTO responseDTO = new SupplierProductsPageResponseDTO();

        responseDTO.setProducts(
            productsPage.stream()
                        .map(product -> {
                            SupplierProductResponseDTO dto = new SupplierProductResponseDTO();

                            dto.setId(product.getId());
                            dto.setWarehouseProductId(product.getWarehouseProduct().getId());
                            dto.setProductName(product.getWarehouseProduct().getProduct().getName());
                            dto.setUnitAbbreviation(product.getWarehouseProduct().getUnit().getAbbreviation());
                            dto.setPrice(product.getPrice());
                            dto.setMinOrderQuantity(product.getMinOrderQuantity());

                            return dto;
                        })
                        .toList()
        );

        responseDTO.setTotalElements(productsPage.getTotalElements());
        responseDTO.setTotalPages(productsPage.getTotalPages());

        return responseDTO;
    }

    public SupplierOrdersPageResponseDTO getSupplierOrders(Long id, Integer page, Integer pageSize, String from, String to, String orderStatus, String timeField) {
        supplierRepository.findById(id)
                            .orElseThrow(() -> new EntityNotFoundException("Supplier not found"));

        orderStatusRepository.findByStatus(orderStatus)
                .orElseThrow(() -> new EntityNotFoundException("Order status not found"));

        Integer pageNumber = page != null ? page - 1 : 0;
        Integer size = pageSize != null ? pageSize : this.pageSize;
        PageRequest pageRequest = PageRequest.of(pageNumber, size);

        Instant fromInstant = from != null ? Instant.parse(from) : null;
        Instant toInstant = to != null ? Instant.parse(to) : null;

        Page<Order> ordersPage = "receivedAt".equals(timeField)
            ? orderRepository.findBySupplierIdAndReceivedAtBetweenAndOrderStatus(id, fromInstant, toInstant, orderStatus, pageRequest)
            : orderRepository.findBySupplierIdAndCreatedAtBetweenAndOrderStatus(id, fromInstant, toInstant, orderStatus, pageRequest);

        SupplierOrdersPageResponseDTO responseDTO = new SupplierOrdersPageResponseDTO();

        responseDTO.setOrders(
            ordersPage.stream()
                      .map(order -> {
                          SupplierOrderForPageResponseDTO dto = new SupplierOrderForPageResponseDTO();
                          dto.setId(order.getId());
                          dto.setCreatedAt(order.getCreatedAt());
                          return dto;
                      })
                      .toList()
        );

        responseDTO.setTotalElements(ordersPage.getTotalElements());
        responseDTO.setTotalPages(ordersPage.getTotalPages());

        return responseDTO;
    }

    public void addSupplier(AddSupplierRequestDTO dto) {
        Supplier supplier = new Supplier();

        supplier.setName(dto.getName());
        supplier.setAddress(dto.getAddress());
        supplier.setEmail(dto.getEmail());
        supplier.setPhoneNumber(dto.getPhoneNumber());

        supplierRepository.save(supplier);
    }

    public void saveSupplier(Long id, SaveSupplierRequestDTO dto) {
        Supplier supplier = supplierRepository.findById(id)
                                                .orElseThrow(() -> new EntityNotFoundException("Supplier not found"));

        supplier.setName(dto.getName());
        supplier.setAddress(dto.getAddress());
        supplier.setEmail(dto.getEmail());
        supplier.setPhoneNumber(dto.getPhoneNumber());

        supplierRepository.save(supplier);
    }

    public SuppliersAddableWarehouseProductsPageResponseDTO getAddableWarehouseProducts(Long id, String name, Long categoryId, Integer page, Integer pageSize) {
        supplierRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Supplier not found"));

        Integer pageNumber = page != null ? page - 1 : 0;
        Integer size = pageSize != null ? pageSize : this.pageSize;
        PageRequest pageRequest = PageRequest.of(pageNumber, size);

        Page<WarehouseProduct> productsPage;
        if (name == null || name.isBlank()) {
            productsPage = warehouseProductRepository.findAddableBySupplierFiltered(id, categoryId, pageRequest);
        } else {
            productsPage = warehouseProductRepository.searchAddableBySupplierAndName(name, id, categoryId, pageRequest);
        }

        SuppliersAddableWarehouseProductsPageResponseDTO responseDTO = new SuppliersAddableWarehouseProductsPageResponseDTO();

        responseDTO.setProducts(
            productsPage.stream()
                    .map(wp -> {
                        SuppliersAddableWarehouseProductResponseDTO dto = new SuppliersAddableWarehouseProductResponseDTO();
                        dto.setId(wp.getId());
                        dto.setName(wp.getProduct().getName());
                        dto.setUnitAbbreviation(wp.getUnit().getAbbreviation());
                        return dto;
                    })
                    .toList()
        );

        responseDTO.setTotalElements(productsPage.getTotalElements());
        responseDTO.setTotalPages(productsPage.getTotalPages());

        return responseDTO;
    }

    public void addSupplierProduct(Long id, AddSupplierProductRequestDTO dto) {
        Supplier supplier = supplierRepository.findById(id)
                                              .orElseThrow(() -> new EntityNotFoundException("Supplier not found"));

        WarehouseProduct warehouseProduct = warehouseProductRepository.findById(dto.getWarehouseProductId())
                                                                      .orElseThrow(() -> new EntityNotFoundException("Warehouse product not found"));

        SupplierProduct supplierProduct = new SupplierProduct();
        supplierProduct.setSupplier(supplier);
        supplierProduct.setWarehouseProduct(warehouseProduct);
        supplierProduct.setPrice(dto.getPrice());
        supplierProduct.setMinOrderQuantity(dto.getMinOrderQuantity());

        supplierProductRepository.save(supplierProduct);
    }

    public void saveSupplierProduct(Long supplierId, Long supplierProductId, SaveSupplierProductRequestDTO dto) {
        SupplierProduct supplierProduct = supplierProductRepository.findById(supplierProductId)
                                                                   .orElseThrow(() -> new EntityNotFoundException("Supplier product not found"));

        if (!supplierProduct.getSupplier().getId().equals(supplierId)) {
            throw new EntityNotFoundException("Supplier product not found");
        }

        supplierProduct.setPrice(dto.getPrice());
        supplierProduct.setMinOrderQuantity(dto.getMinOrderQuantity());

        supplierProductRepository.save(supplierProduct);
    }

    public void deleteSupplierProduct(Long supplierId, Long supplierProductId) {
        SupplierProduct supplierProduct = supplierProductRepository.findById(supplierProductId)
                                                                   .orElseThrow(() -> new EntityNotFoundException("Supplier product not found"));

        if (!supplierProduct.getSupplier().getId().equals(supplierId)) {
            throw new EntityNotFoundException("Supplier product not found");
        }

        supplierProductRepository.delete(supplierProduct);
    }

    public void deactivateSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                                                .orElseThrow(() -> new EntityNotFoundException("Supplier not found"));

        supplier.setActive(false);
        supplierRepository.save(supplier);
    }

    public void activateSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                                                .orElseThrow(() -> new EntityNotFoundException("Supplier not found"));

        supplier.setActive(true);
        supplierRepository.save(supplier);
    }

    public void deleteSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                                                .orElseThrow(() -> new EntityNotFoundException("Supplier not found"));

        List<String> deleteForbiddenReasons = canBeDeleted(supplier);
        if (!deleteForbiddenReasons.isEmpty()) {
            throw new ForbiddenOperationException(deleteForbiddenReasons);
        }

        supplierRepository.delete(supplier);
    }

    private List<String> canBeDeleted(Supplier supplier) {
        List<String> reasons = new ArrayList<>();

        List<Order> orders = supplier.getOrders();
        if (orders != null && !orders.isEmpty()) {
            reasons.add("Supplier has existing orders and cannot be deleted.");
        }

        return reasons;
    }
}
