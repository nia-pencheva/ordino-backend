package com.ordino.domain.products.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ordino.domain.products.model.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByActive(Boolean active, Pageable pageable);

    @Query(
        value = """
            SELECT * FROM (
                SELECT *, 1 AS rank FROM products WHERE LOWER(name) = LOWER(:name) AND active = :active
                UNION
                SELECT *, 2 AS rank FROM products WHERE LOWER(name) LIKE CONCAT(LOWER(:name), '%') AND active = :active
                UNION
                SELECT *, 3 AS rank FROM products WHERE LOWER(name) LIKE CONCAT('%', LOWER(:name), '%') AND active = :active
            ) t
            GROUP BY id
            ORDER BY rank, id
        """,
        countQuery = "SELECT COUNT(*) FROM products WHERE name LIKE CONCAT('%', :name, '%')",
        nativeQuery = true
    )
    Page<Product> searchByNameAndActive(String name, Boolean active, Pageable pageable);

    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
}
