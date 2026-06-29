package com.ordino.domain.recipes.logs.edits.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ordino.domain.recipes.logs.edits.model.dto.RecipeEditLogEntryResponseDTO;
import com.ordino.domain.recipes.logs.edits.model.entity.RecipeEditLog;
import com.ordino.domain.recipes.logs.edits.repository.RecipeEditLogRepository;
import com.ordino.domain.recipes.model.entity.Recipe;
import com.ordino.domain.users.model.entity.User;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RecipeEditLogServiceImpl implements RecipeEditLogService {
    private final RecipeEditLogRepository recipeEditLogRepository;
    private final ObjectMapper objectMapper;

    public RecipeEditLogEntryResponseDTO getLogEntry(Long id) {
        RecipeEditLog logEntry = recipeEditLogRepository.findById(id)
                                                        .orElseThrow(() -> new EntityNotFoundException("Recipe edit log entry not found"));

        Recipe recipe = logEntry.getRecipe();
        User user = logEntry.getUser();

        RecipeEditLogEntryResponseDTO responseDTO = new RecipeEditLogEntryResponseDTO();

        responseDTO.setRecipeId(recipe.getId());
        responseDTO.setRecipeTitle(recipe.getTitle());
        responseDTO.setUserId(user.getId());
        responseDTO.setUserFullName(user.getFullName());
        responseDTO.setOldData(logEntry.getOldData());
        responseDTO.setNewData(logEntry.getNewData());
        responseDTO.setCreatedAt(logEntry.getCreatedAt());

        return responseDTO;
    }

    public void createLog(String oldSnapshot, String newSnapshot, Recipe recipe, User user) throws JsonProcessingException {
        ObjectNode changedFields = computeChangedFields(oldSnapshot, newSnapshot);
        if (changedFields.size() == 0) return;

        RecipeEditLog log = new RecipeEditLog();
        log.setRecipe(recipe);
        log.setUser(user);
        log.setOldData(objectMapper.writeValueAsString(changedFields));
        log.setNewData(newSnapshot);

        recipeEditLogRepository.save(log);
    }

    private ObjectNode computeChangedFields(String oldJson, String newJson) throws JsonProcessingException {
        JsonNode oldNode = objectMapper.readTree(oldJson);
        JsonNode newNode = objectMapper.readTree(newJson);
        ObjectNode diff = objectMapper.createObjectNode();

        for (String field : List.of("title", "preparationTime", "servings", "instructions", "notes", "description")) {
            if (!Objects.equals(oldNode.get(field), newNode.get(field))) {
                diff.set(field, oldNode.get(field));
            }
        }

        if (!oldNode.get("products").equals(newNode.get("products"))) {
            diff.set("products", oldNode.get("products"));
        }

        if (!oldNode.get("categories").equals(newNode.get("categories"))) {
            diff.set("categories", oldNode.get("categories"));
        }

        return diff;
    }
}
