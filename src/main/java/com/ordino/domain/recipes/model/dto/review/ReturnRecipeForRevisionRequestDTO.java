package com.ordino.domain.recipes.model.dto.review;

import com.ordino.core.validation.NullOrNotBlank.NullOrNotBlank;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReturnRecipeForRevisionRequestDTO {
    @NullOrNotBlank(message = "Return notes must not be blank")
    @Size(max = 65535, message = "Return notes are too long")
    private String returnNotes;
}
