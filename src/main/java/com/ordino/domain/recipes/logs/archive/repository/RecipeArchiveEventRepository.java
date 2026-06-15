package com.ordino.domain.recipes.logs.archive.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ordino.domain.recipes.logs.archive.model.entity.RecipeArchiveEvent;

public interface RecipeArchiveEventRepository extends JpaRepository<RecipeArchiveEvent, Long> {
    Optional<RecipeArchiveEvent> findByEvent(String event);
}
