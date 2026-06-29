package com.ordino.domain.recipes.logs.review.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ordino.domain.recipes.logs.review.model.dto.RecipeReviewApprovedLogEntryResponseDTO;
import com.ordino.domain.recipes.logs.review.model.dto.RecipeReviewDiscardedLogEntryResponseDTO;
import com.ordino.domain.recipes.logs.review.model.dto.RecipeReviewReturnedForRevisionLogEntryResponseDTO;
import com.ordino.domain.recipes.logs.review.model.dto.RecipeReviewSubmittedForApprovalLogEntryResponseDTO;
import com.ordino.domain.recipes.model.entity.Recipe;
import com.ordino.domain.users.model.entity.User;

public interface RecipeReviewLogService {
    RecipeReviewSubmittedForApprovalLogEntryResponseDTO getSubmittedForApprovalLogEntry(Long id);

    RecipeReviewReturnedForRevisionLogEntryResponseDTO getReturnedForRevisionLogEntry(Long id);

    RecipeReviewDiscardedLogEntryResponseDTO getDiscardedLogEntry(Long id);

    RecipeReviewApprovedLogEntryResponseDTO getApprovedLogEntry(Long id);

    void createLog(Recipe recipe, User reviewer, String eventName) throws JsonProcessingException;

    void createLog(Recipe recipe, User reviewer, String eventName, String returnNotes) throws JsonProcessingException;
}
