package com.ordino.domain.recipes.categories.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ordino.domain.recipes.categories.model.entity.RecipeCategory;

public interface RecipeCategoryRepository extends JpaRepository<RecipeCategory, Long> {
    boolean existsByCategory(String category);
    boolean existsByCategoryAndIdNot(String category, Long id);
}
