package com.ordino.domain.recipes.logs.edits.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ordino.domain.recipes.logs.edits.model.entity.RecipeEditLog;

public interface RecipeEditLogRepository extends JpaRepository<RecipeEditLog, Long> {
    List<RecipeEditLog> findByRecipeId(Long recipeId);
}
