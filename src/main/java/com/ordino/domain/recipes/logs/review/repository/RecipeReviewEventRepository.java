package com.ordino.domain.recipes.logs.review.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ordino.domain.recipes.logs.review.model.entity.RecipeReviewEvent;

public interface RecipeReviewEventRepository extends JpaRepository<RecipeReviewEvent, Long> {
    Optional<RecipeReviewEvent> findByEvent(String event);
}
