package com.ordino.domain.recipes.logs.archive.model.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeArchiveLogEntriesResponseDTO {
    private List<RecipeArchiveArchivedLogEntryResponseDTO> archivedLogEntries;
    private List<RecipeArchiveReturnedToApprovedLogEntryResponseDTO> returnedToApprovedLogEntries;
}
