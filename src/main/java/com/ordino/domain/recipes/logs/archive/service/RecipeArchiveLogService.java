package com.ordino.domain.recipes.logs.archive.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ordino.domain.recipes.logs.archive.model.dto.RecipeArchiveArchivedLogEntryResponseDTO;
import com.ordino.domain.recipes.logs.archive.model.dto.RecipeArchiveReturnedToApprovedLogEntryResponseDTO;
import com.ordino.domain.recipes.model.entity.Recipe;
import com.ordino.domain.users.model.entity.User;

public interface RecipeArchiveLogService {
    RecipeArchiveArchivedLogEntryResponseDTO getArchivedLogEntry(Long id);

    RecipeArchiveReturnedToApprovedLogEntryResponseDTO getReturnedToApprovedLogEntry(Long id);

    void createLog(Recipe recipe, User user, String eventName) throws JsonProcessingException;
}
