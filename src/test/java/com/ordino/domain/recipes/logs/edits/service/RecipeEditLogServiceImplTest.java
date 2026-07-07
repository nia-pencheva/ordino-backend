package com.ordino.domain.recipes.logs.edits.service;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordino.domain.recipes.logs.edits.repository.RecipeEditLogRepository;
import com.ordino.domain.recipes.model.entity.Recipe;
import com.ordino.domain.users.model.entity.User;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

class RecipeEditLogServiceImplTest {

    private final RecipeEditLogRepository repository = mock(RecipeEditLogRepository.class);
    private final RecipeEditLogServiceImpl service = new RecipeEditLogServiceImpl(repository, new ObjectMapper());

    private static final String BASE_SNAPSHOT = """
            {"title":"Carbonara","preparationTime":20,"servings":4,"instructions":"[]","notes":null,"description":"desc","products":[],"categories":[]}""";

    @Test
    void createLog_noActualFieldsChanged_doesNotCreateEditLogEntry() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        User user = new User();
        user.setId(2L);

        service.createLog(BASE_SNAPSHOT, BASE_SNAPSHOT, recipe, user);

        verifyNoInteractions(repository);
    }

    @Test
    void createLog_titleChanged_createsEditLogEntryWithDiff() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        User user = new User();
        user.setId(2L);

        String updatedSnapshot = BASE_SNAPSHOT.replace("Carbonara", "Carbonara V2");

        service.createLog(BASE_SNAPSHOT, updatedSnapshot, recipe, user);

        verify(repository).save(org.mockito.ArgumentMatchers.argThat(log ->
                log.getRecipe() == recipe
                        && log.getUser() == user
                        && log.getNewData().equals(updatedSnapshot)
                        && log.getOldData().contains("Carbonara")
        ));
    }
}
