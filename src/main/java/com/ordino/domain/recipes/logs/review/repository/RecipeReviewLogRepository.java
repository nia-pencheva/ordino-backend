package com.ordino.domain.recipes.logs.review.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ordino.domain.recipes.logs.review.model.entity.RecipeReviewLog;

public interface RecipeReviewLogRepository extends JpaRepository<RecipeReviewLog, Long> {
    Optional<RecipeReviewLog> findFirstByRecipeIdAndRecipeReviewEventEventOrderByCreatedAtDesc(Long recipeId, String event);
    List<RecipeReviewLog> findByRecipeId(Long recipeId);
    List<RecipeReviewLog> findByRecipeIdAndRecipeReviewEventEventOrderByCreatedAtDesc(Long recipeId, String event);
}
