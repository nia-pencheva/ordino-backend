package com.ordino.domain.recipes.logs.archive.service;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ordino.domain.recipes.logs.archive.model.dto.RecipeArchiveArchivedLogEntryResponseDTO;
import com.ordino.domain.recipes.logs.archive.model.dto.RecipeArchiveReturnedToApprovedLogEntryResponseDTO;
import com.ordino.domain.recipes.logs.archive.model.entity.RecipeArchiveEvent;
import com.ordino.domain.recipes.logs.archive.model.entity.RecipeArchiveLog;
import com.ordino.domain.recipes.logs.archive.repository.RecipeArchiveEventRepository;
import com.ordino.domain.recipes.logs.archive.repository.RecipeArchiveLogRepository;
import com.ordino.domain.recipes.logs.service.RecipeLogService;
import com.ordino.domain.recipes.model.entity.Recipe;
import com.ordino.domain.users.model.entity.User;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RecipeArchiveLogService {
    private final RecipeArchiveLogRepository recipeArchiveLogRepository;
    private final RecipeArchiveEventRepository recipeArchiveEventRepository;
    private final RecipeLogService recipeLogService;

    public RecipeArchiveArchivedLogEntryResponseDTO getArchivedLogEntry(Long id) {
        RecipeArchiveLog logEntry = recipeArchiveLogRepository.findById(id)
                                                                .orElseThrow(() -> new EntityNotFoundException("Recipe archive log entry not found"));

        Recipe recipe = logEntry.getRecipe();
        User user = logEntry.getUser();

        RecipeArchiveArchivedLogEntryResponseDTO responseDTO = new RecipeArchiveArchivedLogEntryResponseDTO();
        responseDTO.setRecipeId(recipe.getId());
        responseDTO.setRecipeTitle(recipe.getTitle());
        responseDTO.setUserId(user.getId());
        responseDTO.setUserFullName(user.getFullName());
        responseDTO.setCreatedAt(logEntry.getCreatedAt());

        return responseDTO;
    }

    public RecipeArchiveReturnedToApprovedLogEntryResponseDTO getReturnedToApprovedLogEntry(Long id) {
        RecipeArchiveLog logEntry = recipeArchiveLogRepository.findById(id)
                                                                .orElseThrow(() -> new EntityNotFoundException("Recipe archive log entry not found"));

        Recipe recipe = logEntry.getRecipe();
        User user = logEntry.getUser();

        RecipeArchiveReturnedToApprovedLogEntryResponseDTO responseDTO = new RecipeArchiveReturnedToApprovedLogEntryResponseDTO();
        responseDTO.setRecipeId(recipe.getId());
        responseDTO.setRecipeTitle(recipe.getTitle());
        responseDTO.setUserId(user.getId());
        responseDTO.setUserFullName(user.getFullName());
        responseDTO.setCreatedAt(logEntry.getCreatedAt());

        return responseDTO;
    }
    
    public void createLog(Recipe recipe, User user, String eventName) throws JsonProcessingException {
        RecipeArchiveEvent event = recipeArchiveEventRepository.findByEvent(eventName)
            .orElseThrow(() -> new EntityNotFoundException("Archive event not found: " + eventName));

        RecipeArchiveLog log = new RecipeArchiveLog();
        log.setRecipe(recipe);
        log.setUser(user);
        log.setSnapshot(recipeLogService.createRecipeSnapshot(recipe));
        log.setRecipeArchiveEvent(event);

        recipeArchiveLogRepository.save(log);
    }
}
