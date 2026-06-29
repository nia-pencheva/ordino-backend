package com.ordino.domain.recipes.logs.edits.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ordino.domain.recipes.logs.edits.model.dto.RecipeEditLogEntryResponseDTO;
import com.ordino.domain.recipes.model.entity.Recipe;
import com.ordino.domain.users.model.entity.User;

public interface RecipeEditLogService {
    RecipeEditLogEntryResponseDTO getLogEntry(Long id);

    void createLog(String oldSnapshot, String newSnapshot, Recipe recipe, User user) throws JsonProcessingException;
}
