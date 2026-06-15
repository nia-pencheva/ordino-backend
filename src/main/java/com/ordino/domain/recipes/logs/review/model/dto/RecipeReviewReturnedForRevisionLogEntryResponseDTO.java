package com.ordino.domain.recipes.logs.review.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeReviewReturnedForRevisionLogEntryResponseDTO extends RecipeReviewLogEntryResponseDTO {
    private String notes;
}
