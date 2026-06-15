package com.ordino.domain.recipes.model.dto.review;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmitRecipeRequestDTO {
    @NotNull(message = "Reviewer is required")
    @Positive(message = "Reviewer ID must be positive")
    private Long reviewerId;
}
