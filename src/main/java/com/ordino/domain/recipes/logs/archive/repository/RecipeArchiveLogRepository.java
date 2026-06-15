package com.ordino.domain.recipes.logs.archive.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ordino.domain.recipes.logs.archive.model.entity.RecipeArchiveLog;

public interface RecipeArchiveLogRepository extends JpaRepository<RecipeArchiveLog, Long> {
    List<RecipeArchiveLog> findByRecipeId(Long recipeId);
}
