package com.ordino.domain.recipes.products.categories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ordino.domain.recipes.products.categories.model.entity.RecipeIngredientCategory;
import com.ordino.domain.recipes.products.categories.repository.RecipeIngredientCategoryRepository;
import com.ordino.support.AbstractIntegrationTest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RecipeIngredientCategoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private RecipeIngredientCategoryRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void deleteRecipeIngredientCategory_nonLeafCategoryWithSubcategories_cascadesToSubcategories() throws Exception {
        fixtures.chef("chef.categorydelete");
        String token = loginAndGetToken("chef.categorydelete", "Passw0rd!");

        RecipeIngredientCategory parent = new RecipeIngredientCategory();
        parent.setCategory("Dairy");
        parent = repository.save(parent);

        RecipeIngredientCategory child = new RecipeIngredientCategory();
        child.setCategory("Cheese");
        child.setParentCategory(parent);
        child = repository.save(child);
        Long childId = child.getId();

        entityManager.flush();
        entityManager.clear();

        mockMvc.perform(delete("/recipe-ingredient-categories/" + parent.getId())
                        .header("Authorization", bearer(token)))
                .andExpect(status().isNoContent());

        entityManager.flush();
        entityManager.clear();

        assertThat(repository.findById(childId)).isEmpty();
    }
}
