package com.ordino.domain.warehouse.products.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ordino.domain.products.model.entity.Product;
import com.ordino.domain.warehouse.products.model.entity.WarehouseProduct;

public interface WarehouseProductRepository extends JpaRepository<WarehouseProduct, Long> {

    boolean existsByProductId(Long productId);

    boolean existsByIdAndActiveTrue(Long id);

    @Query(
        value = """
            WITH RECURSIVE category_tree AS (
                SELECT id FROM warehouse_product_categories WHERE id = :categoryId
                UNION ALL
                SELECT wpc.id FROM warehouse_product_categories wpc
                INNER JOIN category_tree ct ON wpc.parent_category_id = ct.id
            )
            SELECT wp.* FROM warehouse_products wp
            WHERE wp.id NOT IN (
                SELECT sp.warehouse_product_id FROM suppliers_products sp
                WHERE sp.supplier_id = :supplierId
            )
            AND wp.active = true
            AND (:categoryId IS NULL OR wp.product_id IN (
                SELECT pwc.product_id FROM products_warehouse_categories pwc
                WHERE pwc.warehouse_product_category_id IN (SELECT id FROM category_tree)
            ))
            ORDER BY wp.id
        """,
        countQuery = """
            WITH RECURSIVE category_tree AS (
                SELECT id FROM warehouse_product_categories WHERE id = :categoryId
                UNION ALL
                SELECT wpc.id FROM warehouse_product_categories wpc
                INNER JOIN category_tree ct ON wpc.parent_category_id = ct.id
            )
            SELECT COUNT(*) FROM warehouse_products wp
            WHERE wp.id NOT IN (
                SELECT sp.warehouse_product_id FROM suppliers_products sp
                WHERE sp.supplier_id = :supplierId
            )
            AND wp.active = true
            AND (:categoryId IS NULL OR wp.product_id IN (
                SELECT pwc.product_id FROM products_warehouse_categories pwc
                WHERE pwc.warehouse_product_category_id IN (SELECT id FROM category_tree)
            ))
        """,
        nativeQuery = true
    )
    Page<WarehouseProduct> findAddableBySupplierFiltered(@Param("supplierId") Long supplierId, @Param("categoryId") Long categoryId, Pageable pageable);

    @Query(
        value = """
            WITH RECURSIVE category_tree AS (
                SELECT id FROM warehouse_product_categories WHERE id = :categoryId
                UNION ALL
                SELECT wpc.id FROM warehouse_product_categories wpc
                INNER JOIN category_tree ct ON wpc.parent_category_id = ct.id
            )
            SELECT * FROM (
                SELECT wp.*, 1 AS rank FROM warehouse_products wp JOIN products p ON p.id = wp.product_id
                    WHERE LOWER(p.name) = LOWER(:name)
                    AND wp.id NOT IN (SELECT sp.warehouse_product_id FROM suppliers_products sp WHERE sp.supplier_id = :supplierId)
                    AND wp.active = true
                    AND (:categoryId IS NULL OR wp.product_id IN (SELECT pwc.product_id FROM products_warehouse_categories pwc WHERE pwc.warehouse_product_category_id IN (SELECT id FROM category_tree)))
                UNION
                SELECT wp.*, 2 AS rank FROM warehouse_products wp JOIN products p ON p.id = wp.product_id
                    WHERE LOWER(p.name) LIKE CONCAT(LOWER(:name), '%')
                    AND wp.id NOT IN (SELECT sp.warehouse_product_id FROM suppliers_products sp WHERE sp.supplier_id = :supplierId)
                    AND wp.active = true
                    AND (:categoryId IS NULL OR wp.product_id IN (SELECT pwc.product_id FROM products_warehouse_categories pwc WHERE pwc.warehouse_product_category_id IN (SELECT id FROM category_tree)))
                UNION
                SELECT wp.*, 3 AS rank FROM warehouse_products wp JOIN products p ON p.id = wp.product_id
                    WHERE LOWER(p.name) LIKE CONCAT('%', LOWER(:name), '%')
                    AND wp.id NOT IN (SELECT sp.warehouse_product_id FROM suppliers_products sp WHERE sp.supplier_id = :supplierId)
                    AND wp.active = true
                    AND (:categoryId IS NULL OR wp.product_id IN (SELECT pwc.product_id FROM products_warehouse_categories pwc WHERE pwc.warehouse_product_category_id IN (SELECT id FROM category_tree)))
            ) t
            GROUP BY id
            ORDER BY rank, id
        """,
        countQuery = """
            WITH RECURSIVE category_tree AS (
                SELECT id FROM warehouse_product_categories WHERE id = :categoryId
                UNION ALL
                SELECT wpc.id FROM warehouse_product_categories wpc
                INNER JOIN category_tree ct ON wpc.parent_category_id = ct.id
            )
            SELECT COUNT(*) FROM warehouse_products wp
            JOIN products p ON p.id = wp.product_id
            WHERE LOWER(p.name) LIKE CONCAT('%', LOWER(:name), '%')
            AND wp.id NOT IN (SELECT sp.warehouse_product_id FROM suppliers_products sp WHERE sp.supplier_id = :supplierId)
            AND wp.active = true
            AND (:categoryId IS NULL OR wp.product_id IN (SELECT pwc.product_id FROM products_warehouse_categories pwc WHERE pwc.warehouse_product_category_id IN (SELECT id FROM category_tree)))
        """,
        nativeQuery = true
    )
    Page<WarehouseProduct> searchAddableBySupplierAndName(@Param("name") String name, @Param("supplierId") Long supplierId, @Param("categoryId") Long categoryId, Pageable pageable);

    @Query(
        value = """
            SELECT * FROM warehouse_products wp
            WHERE (:active IS NULL OR wp.active = :active)
            AND (:categoryId IS NULL OR wp.product_id IN (
                SELECT product_id FROM products_warehouse_categories
                WHERE warehouse_product_category_id = :categoryId
            ))
            ORDER BY wp.id
        """,
        countQuery = """
            SELECT COUNT(*) FROM warehouse_products wp
            WHERE (:active IS NULL OR wp.active = :active)
            AND (:categoryId IS NULL OR wp.product_id IN (
                SELECT product_id FROM products_warehouse_categories
                WHERE warehouse_product_category_id = :categoryId
            ))
        """,
        nativeQuery = true
    )
    Page<WarehouseProduct> findAllByActiveAndCategoryId(@Param("active") Boolean active, @Param("categoryId") Long categoryId, Pageable pageable);

    @Query(
        value = """
            SELECT * FROM (
                SELECT wp.*, 1 AS rank FROM warehouse_products wp JOIN products p ON p.id = wp.product_id
                    WHERE LOWER(p.name) = LOWER(:name)
                    AND (:active IS NULL OR wp.active = :active)
                    AND (:categoryId IS NULL OR wp.product_id IN (
                        SELECT product_id FROM products_warehouse_categories
                        WHERE warehouse_product_category_id = :categoryId
                    ))
                UNION
                SELECT wp.*, 2 AS rank FROM warehouse_products wp JOIN products p ON p.id = wp.product_id
                    WHERE LOWER(p.name) LIKE CONCAT(LOWER(:name), '%')
                    AND (:active IS NULL OR wp.active = :active)
                    AND (:categoryId IS NULL OR wp.product_id IN (
                        SELECT product_id FROM products_warehouse_categories
                        WHERE warehouse_product_category_id = :categoryId
                    ))
                UNION
                SELECT wp.*, 3 AS rank FROM warehouse_products wp JOIN products p ON p.id = wp.product_id
                    WHERE LOWER(p.name) LIKE CONCAT('%', LOWER(:name), '%')
                    AND (:active IS NULL OR wp.active = :active)
                    AND (:categoryId IS NULL OR wp.product_id IN (
                        SELECT product_id FROM products_warehouse_categories
                        WHERE warehouse_product_category_id = :categoryId
                    ))
            ) t
            GROUP BY id
            ORDER BY rank, id
        """,
        countQuery = """
            SELECT COUNT(*) FROM warehouse_products wp
            JOIN products p ON p.id = wp.product_id
            WHERE LOWER(p.name) LIKE CONCAT('%', LOWER(:name), '%')
            AND (:active IS NULL OR wp.active = :active)
            AND (:categoryId IS NULL OR wp.product_id IN (
                SELECT product_id FROM products_warehouse_categories
                WHERE warehouse_product_category_id = :categoryId
            ))
        """,
        nativeQuery = true
    )
    Page<WarehouseProduct> searchByNameAndFilters(@Param("name") String name, @Param("active") Boolean active, @Param("categoryId") Long categoryId, Pageable pageable);

    @Query(
        value = """
            SELECT * FROM products
            WHERE active = true
            AND id NOT IN (SELECT product_id FROM warehouse_products)
            ORDER BY id
        """,
        countQuery = """
            SELECT COUNT(*) FROM products
            WHERE active = true
            AND id NOT IN (SELECT product_id FROM warehouse_products)
        """,
        nativeQuery = true
    )
    Page<Product> findAllAddableProducts(Pageable pageable);

    @Query(
        value = """
            SELECT * FROM (
                SELECT *, 1 AS rank FROM products WHERE LOWER(name) = LOWER(:name) AND active = true
                UNION
                SELECT *, 2 AS rank FROM products WHERE LOWER(name) LIKE CONCAT(LOWER(:name), '%') AND active = true
                UNION
                SELECT *, 3 AS rank FROM products WHERE LOWER(name) LIKE CONCAT('%', LOWER(:name), '%') AND active = true
            ) t
            WHERE id NOT IN (SELECT product_id FROM warehouse_products)
            GROUP BY id
            ORDER BY rank, id
        """,
        countQuery = """
            SELECT COUNT(*) FROM products
            WHERE active = true
            AND LOWER(name) LIKE CONCAT('%', LOWER(:name), '%')
            AND id NOT IN (SELECT product_id FROM warehouse_products)
        """,
        nativeQuery = true
    )
    Page<Product> searchAddableProductsByName(@Param("name") String name, Pageable pageable);

    @Query(
        value = """
            SELECT DISTINCT p.* FROM products p
            JOIN recipes_products rp ON rp.product_id = p.id
            JOIN recipes r ON r.id = rp.recipe_id
            JOIN recipe_statuses rs ON rs.id = r.recipe_status_id
            WHERE rs.status = 'APPROVED'
            AND p.id NOT IN (SELECT product_id FROM warehouse_products)
            ORDER BY p.id
        """,
        countQuery = """
            SELECT COUNT(DISTINCT p.id) FROM products p
            JOIN recipes_products rp ON rp.product_id = p.id
            JOIN recipes r ON r.id = rp.recipe_id
            JOIN recipe_statuses rs ON rs.id = r.recipe_status_id
            WHERE rs.status = 'APPROVED'
            AND p.id NOT IN (SELECT product_id FROM warehouse_products)
        """,
        nativeQuery = true
    )
    Page<Product> findAllAddableFromApprovedRecipes(Pageable pageable);

    @Query(
        value = """
            SELECT * FROM (
                SELECT DISTINCT p.*, 1 AS rank FROM products p
                    JOIN recipes_products rp ON rp.product_id = p.id
                    JOIN recipes r ON r.id = rp.recipe_id
                    JOIN recipe_statuses rs ON rs.id = r.recipe_status_id
                    WHERE rs.status = 'APPROVED'
                    AND p.id NOT IN (SELECT product_id FROM warehouse_products)
                    AND LOWER(p.name) = LOWER(:name)
                UNION
                SELECT DISTINCT p.*, 2 AS rank FROM products p
                    JOIN recipes_products rp ON rp.product_id = p.id
                    JOIN recipes r ON r.id = rp.recipe_id
                    JOIN recipe_statuses rs ON rs.id = r.recipe_status_id
                    WHERE rs.status = 'APPROVED'
                    AND p.id NOT IN (SELECT product_id FROM warehouse_products)
                    AND LOWER(p.name) LIKE CONCAT(LOWER(:name), '%')
                UNION
                SELECT DISTINCT p.*, 3 AS rank FROM products p
                    JOIN recipes_products rp ON rp.product_id = p.id
                    JOIN recipes r ON r.id = rp.recipe_id
                    JOIN recipe_statuses rs ON rs.id = r.recipe_status_id
                    WHERE rs.status = 'APPROVED'
                    AND p.id NOT IN (SELECT product_id FROM warehouse_products)
                    AND LOWER(p.name) LIKE CONCAT('%', LOWER(:name), '%')
            ) t
            GROUP BY id
            ORDER BY rank, id
        """,
        countQuery = """
            SELECT COUNT(DISTINCT p.id) FROM products p
            JOIN recipes_products rp ON rp.product_id = p.id
            JOIN recipes r ON r.id = rp.recipe_id
            JOIN recipe_statuses rs ON rs.id = r.recipe_status_id
            WHERE rs.status = 'APPROVED'
            AND p.id NOT IN (SELECT product_id FROM warehouse_products)
            AND LOWER(p.name) LIKE CONCAT('%', LOWER(:name), '%')
        """,
        nativeQuery = true
    )
    Page<Product> searchAddableFromApprovedRecipesByName(@Param("name") String name, Pageable pageable);
}
