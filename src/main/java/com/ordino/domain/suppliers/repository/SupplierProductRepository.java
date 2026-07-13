package com.ordino.domain.suppliers.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ordino.domain.suppliers.model.entity.SupplierProduct;

public interface SupplierProductRepository extends JpaRepository<SupplierProduct, Long> {

    boolean existsBySupplierIdAndWarehouseProductId(Long supplierId, Long warehouseProductId);

    Optional<SupplierProduct> findBySupplierIdAndWarehouseProductId(Long supplierId, Long warehouseProductId);

    @Query(
        value = """
            WITH RECURSIVE category_tree AS (
                SELECT id FROM warehouse_product_categories WHERE id = :warehouseProductCategoryId
                UNION ALL
                SELECT wpc.id FROM warehouse_product_categories wpc
                INNER JOIN category_tree ct ON wpc.parent_category_id = ct.id
            )
            SELECT sp.* FROM suppliers_products sp
            JOIN warehouse_products wp ON wp.id = sp.warehouse_product_id
            WHERE (sp.supplier_id = :supplierId)
            AND (:warehouseProductCategoryId IS NULL OR wp.product_id IN (
                SELECT pwc.product_id FROM products_warehouse_categories pwc
                WHERE pwc.warehouse_product_category_id IN (SELECT id FROM category_tree)
            ))
            ORDER BY sp.id
        """,
        countQuery = """
            WITH RECURSIVE category_tree AS (
                SELECT id FROM warehouse_product_categories WHERE id = :warehouseProductCategoryId
                UNION ALL
                SELECT wpc.id FROM warehouse_product_categories wpc
                INNER JOIN category_tree ct ON wpc.parent_category_id = ct.id
            )
            SELECT COUNT(*) FROM suppliers_products sp
            JOIN warehouse_products wp ON wp.id = sp.warehouse_product_id
            WHERE (sp.supplier_id = :supplierId)
            AND (:warehouseProductCategoryId IS NULL OR wp.product_id IN (
                SELECT pwc.product_id FROM products_warehouse_categories pwc
                WHERE pwc.warehouse_product_category_id IN (SELECT id FROM category_tree)
            ))
        """,
        nativeQuery = true
    )
    Page<SupplierProduct> findAllFilteredByWarehouseProductCategoryBySupplier(@Param("warehouseProductCategoryId") Long warehouseProductCategoryId, @Param("supplierId") Long supplierId, Pageable pageable);

    @Query(
        value = """
            WITH RECURSIVE category_tree AS (
                SELECT id FROM warehouse_product_categories WHERE id = :warehouseProductCategoryId
                UNION ALL
                SELECT wpc.id FROM warehouse_product_categories wpc
                INNER JOIN category_tree ct ON wpc.parent_category_id = ct.id
            )
            SELECT * FROM (
                SELECT sp.*, 1 AS search_rank FROM suppliers_products sp
                JOIN warehouse_products wp ON wp.id = sp.warehouse_product_id
                JOIN products p ON p.id = wp.product_id
                    WHERE LOWER(p.name) = LOWER(:name)
                    AND (sp.supplier_id = :supplierId)
                    AND (:warehouseProductCategoryId IS NULL OR wp.product_id IN (SELECT pwc.product_id FROM products_warehouse_categories pwc WHERE pwc.warehouse_product_category_id IN (SELECT id FROM category_tree)))
                UNION
                SELECT sp.*, 2 AS search_rank FROM suppliers_products sp
                JOIN warehouse_products wp ON wp.id = sp.warehouse_product_id
                JOIN products p ON p.id = wp.product_id
                    WHERE LOWER(p.name) LIKE CONCAT(LOWER(:name), '%')
                    AND (sp.supplier_id = :supplierId)
                    AND (:warehouseProductCategoryId IS NULL OR wp.product_id IN (SELECT pwc.product_id FROM products_warehouse_categories pwc WHERE pwc.warehouse_product_category_id IN (SELECT id FROM category_tree)))
                UNION
                SELECT sp.*, 3 AS search_rank FROM suppliers_products sp
                JOIN warehouse_products wp ON wp.id = sp.warehouse_product_id
                JOIN products p ON p.id = wp.product_id
                    WHERE LOWER(p.name) LIKE CONCAT('%', LOWER(:name), '%')
                    AND (sp.supplier_id = :supplierId)
                    AND (:warehouseProductCategoryId IS NULL OR wp.product_id IN (SELECT pwc.product_id FROM products_warehouse_categories pwc WHERE pwc.warehouse_product_category_id IN (SELECT id FROM category_tree)))
            ) t
            GROUP BY id
            ORDER BY search_rank, id
        """,
        countQuery = """
            WITH RECURSIVE category_tree AS (
                SELECT id FROM warehouse_product_categories WHERE id = :warehouseProductCategoryId
                UNION ALL
                SELECT wpc.id FROM warehouse_product_categories wpc
                INNER JOIN category_tree ct ON wpc.parent_category_id = ct.id
            )
            SELECT COUNT(*) FROM suppliers_products sp
            JOIN warehouse_products wp ON wp.id = sp.warehouse_product_id
            JOIN products p ON p.id = wp.product_id
            WHERE LOWER(p.name) LIKE CONCAT('%', LOWER(:name), '%')
            AND (sp.supplier_id = :supplierId)
            AND (:warehouseProductCategoryId IS NULL OR wp.product_id IN (SELECT pwc.product_id FROM products_warehouse_categories pwc WHERE pwc.warehouse_product_category_id IN (SELECT id FROM category_tree)))
        """,
        nativeQuery = true
    )
    Page<SupplierProduct> searchByProductNameAndFilteredByWarehouseProductCategoryBySupplier(@Param("name") String name, @Param("warehouseProductCategoryId") Long warehouseProductCategoryId, @Param("supplierId") Long supplierId, Pageable pageable);
    
}
