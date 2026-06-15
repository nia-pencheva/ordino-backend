package com.ordino.domain.recipes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ordino.domain.recipes.model.entity.RecipeStatus;

public interface RecipeStatusRepository extends JpaRepository<RecipeStatus, Long> {

    Optional<RecipeStatus> findByStatus(String status);
}
