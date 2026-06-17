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
    Page<WarehouseProduct> findAllFiltered(@Param("active") Boolean active, @Param("categoryId") Long categoryId, Pageable pageable);

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
            WHERE id NOT IN (SELECT product_id FROM warehouse_products)
            ORDER BY id
        """,
        countQuery = """
            SELECT COUNT(*) FROM products
            WHERE id NOT IN (SELECT product_id FROM warehouse_products)
        """,
        nativeQuery = true
    )
    Page<Product> findAllAddableProducts(Pageable pageable);

    @Query(
        value = """
            SELECT * FROM (
                SELECT *, 1 AS rank FROM products WHERE LOWER(name) = LOWER(:name)
                UNION
                SELECT *, 2 AS rank FROM products WHERE LOWER(name) LIKE CONCAT(LOWER(:name), '%')
                UNION
                SELECT *, 3 AS rank FROM products WHERE LOWER(name) LIKE CONCAT('%', LOWER(:name), '%')
            ) t
            WHERE id NOT IN (SELECT product_id FROM warehouse_products)
            GROUP BY id
            ORDER BY rank, id
        """,
        countQuery = """
            SELECT COUNT(*) FROM products
            WHERE LOWER(name) LIKE CONCAT('%', LOWER(:name), '%')
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
