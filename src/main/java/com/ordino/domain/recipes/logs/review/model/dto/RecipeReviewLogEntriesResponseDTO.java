package com.ordino.domain.recipes.logs.review.model.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeReviewLogEntriesResponseDTO {
    private List<RecipeReviewSubmittedForApprovalLogEntryResponseDTO> submittedForApprovalLogEntries;
    private List<RecipeReviewReturnedForRevisionLogEntryResponseDTO> returnedForRevisionLogEntries;
    private List<RecipeReviewDiscardedLogEntryResponseDTO> discardedLogEntries;
    private List<RecipeReviewApprovedLogEntryResponseDTO> approvedLogEntries;
}
