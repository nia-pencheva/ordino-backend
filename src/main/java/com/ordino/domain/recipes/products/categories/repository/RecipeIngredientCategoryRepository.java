package com.ordino.domain.recipes.products.categories.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ordino.domain.products.model.entity.Product;
import com.ordino.domain.recipes.products.categories.model.entity.RecipeIngredientCategory;

public interface RecipeIngredientCategoryRepository extends JpaRepository<RecipeIngredientCategory, Long>{

    boolean existsByCategory(String category);

    boolean existsByCategoryAndIdNot(String category, Long id);

    boolean existsByIdAndProductsId(Long categoryId, Long productId);

    boolean existsByParentCategoryId(Long parentCategoryId);

    @Query("SELECT DISTINCT c FROM RecipeIngredientCategory c LEFT JOIN FETCH c.products p WHERE p.active IS NULL OR p.active = true")
    List<RecipeIngredientCategory> findAllWithActiveProducts();

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM RecipeIngredientCategory c JOIN c.products p WHERE c.id = :id")
    boolean hasProducts(@Param("id") Long id);

    @Query(
        value = """
            SELECT * FROM products
            WHERE id NOT IN (
                SELECT product_id FROM products_recipe_ingredient_categories
                WHERE recipe_ingredient_category_id = :categoryId
            )
            ORDER BY id
        """,
        countQuery = """
            SELECT COUNT(*) FROM products
            WHERE id NOT IN (
                SELECT product_id FROM products_recipe_ingredient_categories
                WHERE recipe_ingredient_category_id = :categoryId
            )
        """,
        nativeQuery = true
    )
    Page<Product> findAllAddableProducts(@Param("categoryId") Long categoryId, Pageable pageable);

    @Query(
        value = """
            SELECT * FROM (
                SELECT *, 1 AS rank FROM products WHERE LOWER(name) = LOWER(:name)
                UNION
                SELECT *, 2 AS rank FROM products WHERE LOWER(name) LIKE CONCAT(LOWER(:name), '%')
                UNION
                SELECT *, 3 AS rank FROM products WHERE LOWER(name) LIKE CONCAT('%', LOWER(:name), '%')
            ) t
            WHERE id NOT IN (
                SELECT product_id FROM products_recipe_ingredient_categories
                WHERE recipe_ingredient_category_id = :categoryId
            )
            GROUP BY id
            ORDER BY rank, id
        """,
        countQuery = """
            SELECT COUNT(*) FROM products
            WHERE name LIKE CONCAT('%', :name, '%') AND active = true
            AND id NOT IN (
                SELECT product_id FROM products_recipe_ingredient_categories
                WHERE recipe_ingredient_category_id = :categoryId
            )
        """,
        nativeQuery = true
    )
    Page<Product> searchByAddableProductsName(@Param("categoryId") Long categoryId, @Param("name") String name, Pageable pageable);
}
