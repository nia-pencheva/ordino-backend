package com.ordino.domain.recipes;

import com.ordino.domain.products.model.entity.Product;
import com.ordino.domain.recipes.categories.model.entity.RecipeCategory;
import com.ordino.domain.recipes.repository.RecipeRepository;
import com.ordino.domain.units.model.entity.Unit;
import com.ordino.domain.users.model.entity.User;
import com.ordino.support.AbstractIntegrationTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RecipeLifecycleIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private RecipeRepository recipeRepository;

    private Product product;
    private Unit unit;
    private RecipeCategory category;

    @BeforeEach
    void seedReferenceData() {
        fixtures.seedRecipeReferenceData();
        product = fixtures.product("Flour");
        unit = fixtures.unit("gram", "g");
        category = fixtures.recipeCategory("Baking");
    }

    private String fullRecipeBody(String title) {
        return """
                {
                  "title": "%s",
                  "preparationTime": 30,
                  "servings": 4,
                  "instructions": "[{\\"text\\":\\"Mix ingredients\\"}]",
                  "notes": "some notes",
                  "description": "A tasty recipe",
                  "recipeProducts": [{"productId": %d, "position": 1, "quantity": 1, "unitId": %d}],
                  "recipeCategories": [{"recipeCategoryId": %d}]
                }
                """.formatted(title, product.getId(), unit.getId(), category.getId());
    }

    private Long createDraft(String token, String title) throws Exception {
        String response = mockMvc.perform(post("/recipes/draft")
                        .header("Authorization", bearer(token))
                        .contentType("application/json")
                        .content(fullRecipeBody(title)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(response).asLong();
    }

    private Long createAndSelfApproveAsChef(String chefToken, String title) throws Exception {
        Long id = createDraft(chefToken, title);
        mockMvc.perform(post("/recipes/" + id + "/self-approve").header("Authorization", bearer(chefToken)))
                .andExpect(status().isNoContent());
        return id;
    }

    private String recipeStatusOf(Long id) {
        return recipeRepository.findById(id).orElseThrow().getRecipeStatus().getStatus();
    }

    @Test
    void asLineCook_getOwnDraft_returns200() throws Exception {
        User lineCook = fixtures.lineCook("linecook.owndraft");
        String token = loginAndGetToken("linecook.owndraft", "Passw0rd!");

        Long id = createDraft(token, "Own Draft Recipe");

        mockMvc.perform(get("/recipes/" + id).header("Authorization", bearer(token)))
                .andExpect(status().isOk());
    }

    @Test
    void asLineCook_getAnotherUsersDraft_returns404() throws Exception {
        fixtures.lineCook("linecook.owner1");
        String ownerToken = loginAndGetToken("linecook.owner1", "Passw0rd!");
        Long id = createDraft(ownerToken, "Someone Elses Draft");

        fixtures.lineCook("linecook.intruder1");
        String intruderToken = loginAndGetToken("linecook.intruder1", "Passw0rd!");

        mockMvc.perform(get("/recipes/" + id).header("Authorization", bearer(intruderToken)))
                .andExpect(status().isNotFound());
    }

    @Test
    void asLineCook_editApprovedRecipe_returns404() throws Exception {
        User chef = fixtures.chef("chef.forapprovededit");
        String chefToken = loginAndGetToken("chef.forapprovededit", "Passw0rd!");
        Long id = createAndSelfApproveAsChef(chefToken, "Approved For LineCook Edit Test");

        fixtures.lineCook("linecook.editattempt");
        String lineCookToken = loginAndGetToken("linecook.editattempt", "Passw0rd!");

        mockMvc.perform(get("/recipes/" + id + "/edit").header("Authorization", bearer(lineCookToken)))
                .andExpect(status().isNotFound());
    }

    @Test
    void asLineCook_createDraft_returns200AndStatusDraft() throws Exception {
        fixtures.lineCook("linecook.createdraft");
        String token = loginAndGetToken("linecook.createdraft", "Passw0rd!");

        Long id = createDraft(token, "Brand New Draft");

        assertThat(recipeStatusOf(id)).isEqualTo("DRAFT");
    }

    @Test
    void asLineCook_submitDraftForApproval_returns200AndStatusChangesToWaitingForApproval() throws Exception {
        fixtures.lineCook("linecook.submitter");
        String lineCookToken = loginAndGetToken("linecook.submitter", "Passw0rd!");
        Long id = createDraft(lineCookToken, "Recipe To Submit");

        User reviewerChef = fixtures.chef("chef.reviewerfor.submit");

        mockMvc.perform(post("/recipes/" + id + "/submit")
                        .header("Authorization", bearer(lineCookToken))
                        .contentType("application/json")
                        .content("{\"reviewerId\": " + reviewerChef.getId() + "}"))
                .andExpect(status().isNoContent());

        assertThat(recipeStatusOf(id)).isEqualTo("WAITING_FOR_APPROVAL");
    }

    @Test
    void asChef_accessRecipeWaitingForApprovalAssignedToSelf_returns200() throws Exception {
        fixtures.lineCook("linecook.forselfreview");
        String lineCookToken = loginAndGetToken("linecook.forselfreview", "Passw0rd!");
        Long id = createDraft(lineCookToken, "Recipe For Self Review");

        User reviewerChef = fixtures.chef("chef.assignedreviewer");
        mockMvc.perform(post("/recipes/" + id + "/submit")
                        .header("Authorization", bearer(lineCookToken))
                        .contentType("application/json")
                        .content("{\"reviewerId\": " + reviewerChef.getId() + "}"))
                .andExpect(status().isNoContent());

        String reviewerToken = loginAndGetToken("chef.assignedreviewer", "Passw0rd!");
        mockMvc.perform(get("/recipes/" + id + "/review").header("Authorization", bearer(reviewerToken)))
                .andExpect(status().isOk());
    }

    @Test
    void asChef_accessRecipeWaitingForApprovalAssignedToAnotherChef_returns404() throws Exception {
        fixtures.lineCook("linecook.forotherreview");
        String lineCookToken = loginAndGetToken("linecook.forotherreview", "Passw0rd!");
        Long id = createDraft(lineCookToken, "Recipe For Other Review");

        User assignedChef = fixtures.chef("chef.trueassignee");
        mockMvc.perform(post("/recipes/" + id + "/submit")
                        .header("Authorization", bearer(lineCookToken))
                        .contentType("application/json")
                        .content("{\"reviewerId\": " + assignedChef.getId() + "}"))
                .andExpect(status().isNoContent());

        fixtures.chef("chef.notassigned");
        String notAssignedToken = loginAndGetToken("chef.notassigned", "Passw0rd!");
        mockMvc.perform(get("/recipes/" + id + "/review").header("Authorization", bearer(notAssignedToken)))
                .andExpect(status().isNotFound());
    }

    @Test
    void asChef_editApprovedRecipe_returns200() throws Exception {
        User chef = fixtures.chef("chef.editapproved");
        String chefToken = loginAndGetToken("chef.editapproved", "Passw0rd!");
        Long id = createAndSelfApproveAsChef(chefToken, "Approved Recipe For Editing");

        mockMvc.perform(get("/recipes/" + id + "/edit").header("Authorization", bearer(chefToken)))
                .andExpect(status().isOk());
    }

    @Test
    void asChef_editRecipeNotApproved_returns404() throws Exception {
        fixtures.lineCook("linecook.draftowner");
        String lineCookToken = loginAndGetToken("linecook.draftowner", "Passw0rd!");
        Long id = createDraft(lineCookToken, "Not Approved Recipe");

        fixtures.chef("chef.editnotapproved");
        String chefToken = loginAndGetToken("chef.editnotapproved", "Passw0rd!");

        mockMvc.perform(get("/recipes/" + id + "/edit").header("Authorization", bearer(chefToken)))
                .andExpect(status().isNotFound());
    }

    @Test
    void asChef_selfApproveOwnDraft_returns200AndStatusApproved() throws Exception {
        fixtures.chef("chef.selfapprove");
        String chefToken = loginAndGetToken("chef.selfapprove", "Passw0rd!");
        Long id = createDraft(chefToken, "Self Approve Recipe");

        mockMvc.perform(post("/recipes/" + id + "/self-approve").header("Authorization", bearer(chefToken)))
                .andExpect(status().isNoContent());

        assertThat(recipeStatusOf(id)).isEqualTo("APPROVED");
    }

    @Test
    void asChef_returnRecipeForRevision_returns200AndStatusReturnedForRevision() throws Exception {
        fixtures.lineCook("linecook.forreturn");
        String lineCookToken = loginAndGetToken("linecook.forreturn", "Passw0rd!");
        Long id = createDraft(lineCookToken, "Recipe To Return");

        User reviewerChef = fixtures.chef("chef.returner");
        mockMvc.perform(post("/recipes/" + id + "/submit")
                        .header("Authorization", bearer(lineCookToken))
                        .contentType("application/json")
                        .content("{\"reviewerId\": " + reviewerChef.getId() + "}"))
                .andExpect(status().isNoContent());

        String reviewerToken = loginAndGetToken("chef.returner", "Passw0rd!");
        mockMvc.perform(post("/recipes/" + id + "/return-for-revision")
                        .header("Authorization", bearer(reviewerToken))
                        .contentType("application/json")
                        .content("{\"returnNotes\": \"Please fix the quantities\"}"))
                .andExpect(status().isNoContent());

        assertThat(recipeStatusOf(id)).isEqualTo("RETURNED_FOR_REVISION");
    }

    @Test
    void asChef_discardRecipe_returns200AndStatusDiscarded() throws Exception {
        fixtures.lineCook("linecook.fordiscard");
        String lineCookToken = loginAndGetToken("linecook.fordiscard", "Passw0rd!");
        Long id = createDraft(lineCookToken, "Recipe To Discard");

        User reviewerChef = fixtures.chef("chef.discarder");
        mockMvc.perform(post("/recipes/" + id + "/submit")
                        .header("Authorization", bearer(lineCookToken))
                        .contentType("application/json")
                        .content("{\"reviewerId\": " + reviewerChef.getId() + "}"))
                .andExpect(status().isNoContent());

        String reviewerToken = loginAndGetToken("chef.discarder", "Passw0rd!");
        mockMvc.perform(post("/recipes/" + id + "/discard").header("Authorization", bearer(reviewerToken)))
                .andExpect(status().isNoContent());

        assertThat(recipeStatusOf(id)).isEqualTo("DISCARDED");
    }

    @Test
    void asChef_archiveApprovedRecipe_returns200AndStatusArchived() throws Exception {
        fixtures.chef("chef.archiver");
        String chefToken = loginAndGetToken("chef.archiver", "Passw0rd!");
        Long id = createAndSelfApproveAsChef(chefToken, "Recipe To Archive");

        mockMvc.perform(post("/recipes/" + id + "/archive").header("Authorization", bearer(chefToken)))
                .andExpect(status().isNoContent());

        assertThat(recipeStatusOf(id)).isEqualTo("ARCHIVED");
    }

    @Test
    void asKitchenStaff_attemptToCreateDraft_returns403() throws Exception {
        fixtures.kitchenStaff("kitchenstaff.createattempt");
        String token = loginAndGetToken("kitchenstaff.createattempt", "Passw0rd!");

        mockMvc.perform(post("/recipes/draft")
                        .header("Authorization", bearer(token))
                        .contentType("application/json")
                        .content(fullRecipeBody("Kitchen Staff Attempt")))
                .andExpect(status().isForbidden());
    }

    @Test
    void asManager_getApprovedRecipesList_returns200ReadOnlyVisibility() throws Exception {
        fixtures.manager("manager.readapproved");
        String token = loginAndGetToken("manager.readapproved", "Passw0rd!");

        mockMvc.perform(get("/recipes")
                        .header("Authorization", bearer(token))
                        .param("pageSize", "10")
                        .param("recipeStatus", "APPROVED"))
                .andExpect(status().isOk());
    }
}
