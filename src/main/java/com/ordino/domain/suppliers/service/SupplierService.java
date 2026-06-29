package com.ordino.domain.suppliers.service;

import com.ordino.domain.suppliers.model.dto.SupplierProductInfoResponseDTO;
import com.ordino.domain.suppliers.model.dto.SupplierResponseDTO;
import com.ordino.domain.suppliers.model.dto.add.AddSupplierRequestDTO;
import com.ordino.domain.suppliers.model.dto.addable_products_page.SuppliersAddableWarehouseProductsPageResponseDTO;
import com.ordino.domain.suppliers.model.dto.products.AddSupplierProductRequestDTO;
import com.ordino.domain.suppliers.model.dto.products.SaveSupplierProductRequestDTO;
import com.ordino.domain.suppliers.model.dto.save.SaveSupplierRequestDTO;
import com.ordino.domain.suppliers.model.dto.supplier_orders.SupplierOrdersPageResponseDTO;
import com.ordino.domain.suppliers.model.dto.supplier_products_page.SupplierProductsPageResponseDTO;
import com.ordino.domain.suppliers.model.dto.suppliers_page.SuppliersPageResponseDTO;

public interface SupplierService {
    SuppliersPageResponseDTO getSuppliers(String search, String criteria, Boolean active, Integer page, Integer pageSize);

    SupplierResponseDTO getSupplier(Long id);

    SupplierProductInfoResponseDTO getSupplierProduct(Long supplierId, Long supplierProductId);

    SupplierOrdersPageResponseDTO getSupplierProductOrders(Long supplierId, Long supplierProductId, Integer page, Integer pageSize, String from, String to, String orderStatus, String timeField);

    SupplierProductsPageResponseDTO getSupplierProducts(Long id, String name, Long warehouseProductCategoryId, Integer page, Integer pageSize);

    SupplierOrdersPageResponseDTO getSupplierOrders(Long id, Integer page, Integer pageSize, String from, String to, String orderStatus, String timeField);

    void addSupplier(AddSupplierRequestDTO dto);

    void saveSupplier(Long id, SaveSupplierRequestDTO dto);

    SuppliersAddableWarehouseProductsPageResponseDTO getAddableWarehouseProducts(Long id, String name, Long categoryId, Integer page, Integer pageSize);

    void addSupplierProduct(Long id, AddSupplierProductRequestDTO dto);

    void saveSupplierProduct(Long supplierId, Long supplierProductId, SaveSupplierProductRequestDTO dto);

    void deleteSupplierProduct(Long supplierId, Long supplierProductId);

    void deactivateSupplier(Long id);

    void activateSupplier(Long id);

    void deleteSupplier(Long id);
}
