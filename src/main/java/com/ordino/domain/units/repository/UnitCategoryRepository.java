package com.ordino.domain.units.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ordino.domain.units.model.entity.UnitCategory;

public interface UnitCategoryRepository extends JpaRepository<UnitCategory, Long> {

    @Query(
        value = "SELECT DISTINCT uc FROM UnitCategory uc LEFT JOIN FETCH uc.units u ORDER BY uc.id, u.id",
        countQuery = "SELECT COUNT(uc) FROM UnitCategory uc"
    )
    Page<UnitCategory> findAllWithUnitsOrderById(Pageable pageable);

    boolean existsByCategory(String category);

    boolean existsByCategoryAndIdNot(String category, Long id);

}
