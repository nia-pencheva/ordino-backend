package com.ordino.domain.recipes.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ordino.core.config.mapping.CustomMapper;
import com.ordino.core.config.security.DatabaseUserDetails;
import com.ordino.core.exception.ValidationErrorsException;
import com.ordino.domain.notifications.warehouse.WarehouseNotificationService;
import com.ordino.domain.products.repository.ProductRepository;
import com.ordino.domain.recipes.logs.archive.service.RecipeArchiveLogService;
import com.ordino.domain.recipes.logs.edits.service.RecipeEditLogService;
import com.ordino.domain.recipes.logs.review.model.entity.RecipeReviewLog;
import com.ordino.domain.recipes.logs.review.repository.RecipeReviewLogRepository;
import com.ordino.domain.recipes.logs.review.service.RecipeReviewLogService;
import com.ordino.domain.recipes.logs.service.RecipeLogService;
import com.ordino.domain.recipes.model.dto.draft.SaveDraftRequestDTO;
import com.ordino.domain.recipes.model.dto.review.SubmitRecipeRequestDTO;
import com.ordino.domain.recipes.model.entity.Recipe;
import com.ordino.domain.recipes.model.entity.RecipeStatus;
import com.ordino.domain.recipes.products.categories.repository.RecipeIngredientCategoryRepository;
import com.ordino.domain.recipes.repository.RecipeRepository;
import com.ordino.domain.recipes.repository.RecipeStatusRepository;
import com.ordino.domain.units.repository.UnitCategoryRepository;
import com.ordino.domain.units.repository.UnitRepository;
import com.ordino.domain.users.model.entity.User;
import com.ordino.domain.users.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RecipeServiceImplTest {

    private final UnitRepository unitRepository = mock(UnitRepository.class);
    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final RecipeRepository recipeRepository = mock(RecipeRepository.class);
    private final RecipeStatusRepository recipeStatusRepository = mock(RecipeStatusRepository.class);
    private final RecipeIngredientCategoryRepository recipeIngredientCategoryRepository = mock(RecipeIngredientCategoryRepository.class);
    private final UnitCategoryRepository unitCategoryRepository = mock(UnitCategoryRepository.class);
    private final CustomMapper mapper = new CustomMapper();
    private final RecipeReviewLogRepository recipeReviewLogRepository = mock(RecipeReviewLogRepository.class);
    private final RecipeReviewLogService recipeReviewLogService = mock(RecipeReviewLogService.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final RecipeLogService recipeLogService = mock(RecipeLogService.class);
    private final RecipeEditLogService recipeEditLogService = mock(RecipeEditLogService.class);
    private final RecipeArchiveLogService recipeArchiveLogService = mock(RecipeArchiveLogService.class);
    private final Validator validator = mock(Validator.class);
    private final WarehouseNotificationService warehouseNotificationService = mock(WarehouseNotificationService.class);

    private final RecipeServiceImpl service = new RecipeServiceImpl(
            unitRepository, productRepository, recipeRepository, recipeStatusRepository,
            recipeIngredientCategoryRepository, unitCategoryRepository, mapper,
            recipeReviewLogRepository, recipeReviewLogService, userRepository,
            recipeLogService, recipeEditLogService, recipeArchiveLogService, validator,
            warehouseNotificationService
    );

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    private void authenticateAs(User user) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(new DatabaseUserDetails(user), null, List.of())
        );
    }

    private User userWithId(Long id) {
        User user = new User();
        user.setId(id);
        return user;
    }

    private RecipeStatus status(String name) {
        RecipeStatus status = new RecipeStatus();
        status.setStatus(name);
        return status;
    }

    private Recipe recipeWithStatus(Long id, String statusName, User createdBy) {
        Recipe recipe = new Recipe();
        recipe.setId(id);
        recipe.setTitle("Carbonara");
        recipe.setRecipeStatus(status(statusName));
        recipe.setCreatedBy(createdBy);
        return recipe;
    }

    @Test
    void getRecipeForReview_viaGetReviewableRecipe_statusNotWaitingForApproval_throwsEntityNotFound() {
        User chef = userWithId(1L);
        authenticateAs(chef);

        Recipe recipe = recipeWithStatus(10L, "APPROVED", userWithId(2L));
        when(recipeRepository.findById(10L)).thenReturn(Optional.of(recipe));

        assertThatThrownBy(() -> service.getRecipeForReview(10L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void approveRecipe_viaGetReviewableRecipe_reviewerIsNotCurrentUser_throwsEntityNotFound() {
        User currentChef = userWithId(1L);
        User otherChef = userWithId(99L);
        authenticateAs(currentChef);

        Recipe recipe = recipeWithStatus(10L, "WAITING_FOR_APPROVAL", userWithId(2L));
        when(recipeRepository.findById(10L)).thenReturn(Optional.of(recipe));

        RecipeReviewLog submissionLog = new RecipeReviewLog();
        submissionLog.setReviewer(otherChef);
        when(recipeReviewLogRepository.findFirstByRecipeIdAndRecipeReviewEventEventOrderByCreatedAtDesc(10L, "SUBMITTED_FOR_APPROVAL"))
                .thenReturn(Optional.of(submissionLog));

        assertThatThrownBy(() -> service.approveRecipe(10L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void approveRecipe_viaGetReviewableRecipe_correctStatusAndReviewer_succeedsAndSetsApproved() throws Exception {
        User currentChef = userWithId(1L);
        authenticateAs(currentChef);

        Recipe recipe = recipeWithStatus(10L, "WAITING_FOR_APPROVAL", userWithId(2L));
        when(recipeRepository.findById(10L)).thenReturn(Optional.of(recipe));

        RecipeReviewLog submissionLog = new RecipeReviewLog();
        submissionLog.setReviewer(currentChef);
        when(recipeReviewLogRepository.findFirstByRecipeIdAndRecipeReviewEventEventOrderByCreatedAtDesc(10L, "SUBMITTED_FOR_APPROVAL"))
                .thenReturn(Optional.of(submissionLog));

        when(validator.validate(any())).thenReturn(Set.of());
        when(recipeStatusRepository.findByStatus("APPROVED")).thenReturn(Optional.of(status("APPROVED")));

        service.approveRecipe(10L);

        assertThat(recipe.getRecipeStatus().getStatus()).isEqualTo("APPROVED");
        assertThat(recipe.getActive()).isTrue();
        assertThat(recipe.getApprovedBy()).isEqualTo(currentChef);
        verify(recipeReviewLogService).createLog(recipe, currentChef, "APPROVED");
        verify(warehouseNotificationService).sendNewProductsInRecipeNotification(recipe);
    }

    @Test
    void unarchiveRecipe_titleCollidesWithCurrentlyActiveRecipe_throwsValidationExceptionOnTitleField() {
        User chef = userWithId(1L);
        authenticateAs(chef);

        Recipe recipe = recipeWithStatus(10L, "ARCHIVED", userWithId(2L));
        when(recipeRepository.findById(10L)).thenReturn(Optional.of(recipe));
        when(recipeRepository.existsByTitleAndActiveTrue("Carbonara")).thenReturn(true);

        assertThatThrownBy(() -> service.unarchiveRecipe(10L))
                .isInstanceOf(com.ordino.core.exception.ValidationException.class)
                .satisfies(e -> assertThat(((com.ordino.core.exception.ValidationException) e).getField()).isEqualTo("title"));
    }

    @Test
    void unarchiveRecipe_noTitleCollision_setsStatusApprovedAndActiveTrue() throws Exception {
        User chef = userWithId(1L);
        authenticateAs(chef);

        Recipe recipe = recipeWithStatus(10L, "ARCHIVED", userWithId(2L));
        when(recipeRepository.findById(10L)).thenReturn(Optional.of(recipe));
        when(recipeRepository.existsByTitleAndActiveTrue("Carbonara")).thenReturn(false);
        when(recipeStatusRepository.findByStatus("APPROVED")).thenReturn(Optional.of(status("APPROVED")));

        service.unarchiveRecipe(10L);

        assertThat(recipe.getRecipeStatus().getStatus()).isEqualTo("APPROVED");
        assertThat(recipe.getActive()).isTrue();
        verify(recipeArchiveLogService).createLog(recipe, chef, "RETURNED_TO_APPROVED");
    }

    @Test
    void saveDraft_recipeStatusNeitherDraftNorReturnedForRevision_throwsEntityNotFound() {
        User lineCook = userWithId(1L);
        authenticateAs(lineCook);

        Recipe recipe = recipeWithStatus(10L, "APPROVED", lineCook);
        when(recipeRepository.findById(10L)).thenReturn(Optional.of(recipe));

        assertThatThrownBy(() -> service.saveDraft(10L, new SaveDraftRequestDTO()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void submitForApproval_recipeStatusNeitherDraftNorReturnedForRevision_throwsEntityNotFound() {
        User lineCook = userWithId(1L);
        authenticateAs(lineCook);

        Recipe recipe = recipeWithStatus(10L, "APPROVED", lineCook);
        when(recipeRepository.findById(10L)).thenReturn(Optional.of(recipe));

        SubmitRecipeRequestDTO dto = new SubmitRecipeRequestDTO();
        dto.setReviewerId(2L);

        assertThatThrownBy(() -> service.submitForApproval(10L, dto))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void selfApproveRecipe_recipeFailsValidateForApproval_throwsValidationErrorsException() {
        User chef = userWithId(1L);
        authenticateAs(chef);

        Recipe recipe = recipeWithStatus(10L, "DRAFT", chef);
        when(recipeRepository.findById(10L)).thenReturn(Optional.of(recipe));

        @SuppressWarnings("unchecked")
        ConstraintViolation<Object> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);
        when(path.toString()).thenReturn("title");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("Title is required");
        when(validator.validate(any())).thenReturn(Set.of(violation));

        assertThatThrownBy(() -> service.selfApproveRecipe(10L))
                .isInstanceOf(ValidationErrorsException.class);
    }
}
