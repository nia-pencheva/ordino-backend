package com.ordino.domain.recipes.service;

import com.ordino.domain.products.repository.ProductRepository;
import com.ordino.domain.units.repository.UnitRepository;

import java.util.Comparator;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ordino.core.config.mapping.CustomMapper;
import com.ordino.core.config.security.DatabaseUserDetails;
import com.ordino.domain.recipes.logs.archive.service.RecipeArchiveLogService;
import com.ordino.domain.recipes.logs.edits.service.RecipeEditLogService;
import com.ordino.domain.recipes.logs.review.model.entity.RecipeReviewLog;
import com.ordino.domain.recipes.logs.review.repository.RecipeReviewLogRepository;
import com.ordino.domain.recipes.logs.review.service.RecipeReviewLogService;
import com.ordino.domain.recipes.logs.service.RecipeLogService;
import com.ordino.domain.recipes.model.dto.review.ReturnRecipeForRevisionRequestDTO;
import com.ordino.domain.recipes.model.dto.review.SubmitRecipeRequestDTO;
import com.ordino.domain.users.model.entity.User;
import com.ordino.domain.users.repository.UserRepository;
import com.ordino.domain.recipes.categories.model.entity.RecipeCategory;
import com.ordino.domain.recipes.model.dto.RecipeResponseDTO;
import com.ordino.domain.recipes.model.dto.RecipeResponseProductDTO;
import com.ordino.domain.recipes.model.dto.RecipeResponseProductUnitDTO;
import com.ordino.domain.recipes.model.dto.RecipeResponseRecipeCategoryDTO;
import com.ordino.domain.recipes.model.dto.RecipesPageResponseDTO;
import com.ordino.domain.recipes.model.dto.draft.SaveDraftRequestDTO;
import com.ordino.domain.recipes.model.dto.edit.RecipeEditDataResponseDTO;
import com.ordino.domain.recipes.model.dto.edit.RecipeForEditingResponseDTO;
import com.ordino.domain.recipes.model.dto.edit.RecipeForEditingResponseProductCategoriesForSelectDTO;
import com.ordino.domain.recipes.model.dto.edit.RecipeForEditingResponseProductCategoriesForSelectProductDTO;
import com.ordino.domain.recipes.model.dto.edit.RecipeForEditingResponseUnitCategoriesForSelectDTO;
import com.ordino.domain.recipes.model.dto.edit.RecipeForEditingResponseUnitForSelectDTO;
import com.ordino.domain.recipes.model.dto.save.SaveRecipeRequestDTO;
import com.ordino.domain.recipes.model.entity.Recipe;
import com.ordino.domain.recipes.model.entity.RecipeStatus;
import com.ordino.domain.recipes.products.categories.model.entity.RecipeIngredientCategory;
import com.ordino.domain.recipes.products.categories.repository.RecipeIngredientCategoryRepository;
import com.ordino.domain.recipes.products.model.entity.RecipeProduct;
import com.ordino.domain.recipes.repository.RecipeRepository;
import com.ordino.domain.recipes.repository.RecipeStatusRepository;
import com.ordino.domain.units.repository.UnitCategoryRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Map;

import com.ordino.core.exception.ValidationErrorsException;
import com.ordino.domain.recipes.model.dto.save.SaveRecipeRequestCategoryDTO;
import com.ordino.domain.recipes.model.dto.save.SaveRecipeRequestProductDTO;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RecipeService {
    private final UnitRepository unitRepository;
    private final ProductRepository productRepository;
    private final RecipeRepository recipeRepository;
    private final RecipeStatusRepository recipeStatusRepository;
    private final RecipeIngredientCategoryRepository recipeIngredientCategoryRepository;
    private final UnitCategoryRepository unitCategoryRepository;
    private final CustomMapper mapper;
    private final RecipeReviewLogRepository recipeReviewLogRepository;
    private final RecipeReviewLogService recipeReviewLogService;
    private final UserRepository userRepository;
    private final RecipeLogService recipeLogService;
    private final RecipeEditLogService recipeEditLogService;
    private final RecipeArchiveLogService recipeArchiveLogService;
    private final Validator validator;

    public RecipesPageResponseDTO getRecipes(String searchTitle, Integer page, Integer pageSize, String recipeStatus, Long recipeCategoryId) throws EntityNotFoundException {
        RecipeStatus status = recipeStatusRepository.findByStatus(recipeStatus)
                                                        .orElseThrow(() -> new EntityNotFoundException("Recipe status not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        DatabaseUserDetails principal = (DatabaseUserDetails) authentication.getPrincipal();

        boolean ownRecipesOnly = switch (status.getStatus()) {
            case "DISCARDED" -> {
                if (!principal.hasAnyAuthority("line cook", "chef")) throw new EntityNotFoundException("Recipe status not found");
                yield false;
            }
            case "APPROVED" -> false;
            case "ARCHIVED" -> {
                if (!principal.hasAnyAuthority("line cook", "chef", "manager")) throw new EntityNotFoundException("Recipe status not found");
                yield false;
            }
            case "DRAFT" -> {
                if (!principal.hasAnyAuthority("line cook", "chef")) throw new EntityNotFoundException("Recipe status not found");
                yield true;
            }
            case "RETURNED_FOR_REVISION" -> {
                if (!principal.hasAnyAuthority("line cook")) throw new EntityNotFoundException("Recipe status not found");
                yield true;
            }
            case "WAITING_FOR_APPROVAL" -> {
                if (!principal.hasAnyAuthority("line cook", "chef")) throw new EntityNotFoundException("Recipe status not found");
                yield principal.hasAnyAuthority("line cook");
            }
            default -> throw new EntityNotFoundException("Recipe status not found");
        };

        boolean reviewerOnly = "WAITING_FOR_APPROVAL".equals(status.getStatus()) && principal.hasAnyAuthority("chef");

        Integer pageNumber = page != null ? page - 1 : 0;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        Long currentUserId = (ownRecipesOnly || reviewerOnly) ? principal.getUser().getId() : null;

        Long statusId = status.getId();

        Page<Recipe> recipesPage;
        if (searchTitle == null && recipeCategoryId == null) {
            if (ownRecipesOnly) {
                recipesPage = recipeRepository.findByRecipeStatusIdAndCreatedById(statusId, currentUserId, pageRequest);
            } else if (reviewerOnly) {
                recipesPage = recipeRepository.findByRecipeStatusIdAndReviewerId(statusId, currentUserId, pageRequest);
            } else {
                recipesPage = recipeRepository.findByRecipeStatusId(statusId, pageRequest);
            }
        } else if (searchTitle == null) {
            if (ownRecipesOnly) {
                recipesPage = recipeRepository.findByRecipeStatusIdAndCreatedByIdAndRecipeCategoriesId(statusId, currentUserId, recipeCategoryId, pageRequest);
            } else if (reviewerOnly) {
                recipesPage = recipeRepository.findByRecipeStatusIdAndReviewerIdAndRecipeCategoriesId(statusId, currentUserId, recipeCategoryId, pageRequest);
            } else {
                recipesPage = recipeRepository.findByRecipeStatusIdAndRecipeCategoriesId(statusId, recipeCategoryId, pageRequest);
            }
        } else if (recipeCategoryId == null) {
            if (ownRecipesOnly) {
                recipesPage = recipeRepository.searchByTitleAndStatusIdAndCreatedById(searchTitle, statusId, currentUserId, pageRequest);
            } else if (reviewerOnly) {
                recipesPage = recipeRepository.searchByTitleAndStatusIdAndReviewerId(searchTitle, statusId, currentUserId, pageRequest);
            } else {
                recipesPage = recipeRepository.searchByTitleAndStatusId(searchTitle, statusId, pageRequest);
            }
        } else {
            if (ownRecipesOnly) {
                recipesPage = recipeRepository.searchByTitleAndStatusIdAndCreatedByIdAndCategoryId(searchTitle, statusId, currentUserId, recipeCategoryId, pageRequest);
            } else if (reviewerOnly) {
                recipesPage = recipeRepository.searchByTitleAndStatusIdAndReviewerIdAndCategoryId(searchTitle, statusId, currentUserId, recipeCategoryId, pageRequest);
            } else {
                recipesPage = recipeRepository.searchByTitleAndStatusIdAndCategoryId(searchTitle, statusId, recipeCategoryId, pageRequest);
            }
        }

        RecipesPageResponseDTO responseDTO = new RecipesPageResponseDTO();

        responseDTO.setRecipes(
            recipesPage
                .stream()
                .map(recipe -> mapRecipeResponse(recipe))
                .toList()
        );

        responseDTO.setTotalElements(recipesPage.getTotalElements());
        responseDTO.setTotalPages(recipesPage.getTotalPages());

        return responseDTO;
    }

    private RecipeResponseDTO mapRecipeResponse(Recipe recipe) {
        RecipeResponseDTO dto = mapper.map(recipe, RecipeResponseDTO.class);

        dto.setProducts(
            recipe.getRecipeProducts()
                .stream()
                .sorted(Comparator.comparing(RecipeProduct::getPosition))
                .map(recipeProduct -> {
                    RecipeResponseProductDTO productDTO = new RecipeResponseProductDTO();

                    productDTO.setId(recipeProduct.getProduct().getId());
                    productDTO.setName(recipeProduct.getProduct().getName());
                    productDTO.setQuantity(recipeProduct.getQuantity());
                    productDTO.setPosition(recipeProduct.getPosition());
                    productDTO.setUnit(mapper.map(recipeProduct.getUnit(), RecipeResponseProductUnitDTO.class));

                    return productDTO;
                })
                .toList()
        );

        dto.setRecipeCategories(
            recipe.getRecipeCategories()
                .stream()
                .map(rc -> {
                    RecipeResponseRecipeCategoryDTO categoryDTO = new RecipeResponseRecipeCategoryDTO();
                    categoryDTO.setId(rc.getId());
                    categoryDTO.setName(rc.getCategory());
                    return categoryDTO;
                })
                .toList()
        );

        return dto;
    }

    @PreAuthorize("hasAnyAuthority('chef', 'line cook')")
    public Long createDraft(SaveDraftRequestDTO dto) throws EntityNotFoundException {
        Recipe recipe = new Recipe();

        recipe.setRecipeStatus(recipeStatusRepository.findByStatus("DRAFT").orElseThrow(() -> new EntityNotFoundException("Recipes status not found")));
        recipe.setCreatedBy(((DatabaseUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser());
        recipe.setActive(null);
        applyDraftData(recipe, dto);

        return recipe.getId();
    }

    public RecipeResponseDTO getRecipe(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                                        .orElseThrow(() -> new EntityNotFoundException("Recipe not found"));

        DatabaseUserDetails principal = (DatabaseUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long currentUserId = principal.getUser().getId();

        String statusName = recipe.getRecipeStatus().getStatus();
        boolean canAccess = switch (statusName) {
            case "APPROVED" -> true;
            case "ARCHIVED" -> principal.hasAnyAuthority("line cook", "chef", "manager");
            case "DRAFT" -> principal.hasAnyAuthority("line cook", "chef") && recipe.getCreatedBy().getId().equals(currentUserId);
            case "RETURNED_FOR_REVISION" -> principal.hasAnyAuthority("line cook") && recipe.getCreatedBy().getId().equals(currentUserId);
            case "WAITING_FOR_APPROVAL" ->
                (principal.hasAnyAuthority("line cook") && recipe.getCreatedBy().getId().equals(currentUserId))
                || (principal.hasAnyAuthority("chef") && recipeReviewLogRepository
                    .findFirstByRecipeIdAndRecipeReviewEventEventOrderByCreatedAtDesc(id, "SUBMITTED_FOR_APPROVAL")
                    .map(log -> log.getReviewer().getId().equals(currentUserId))
                    .orElse(false));
            case "DISCARDED" -> principal.hasAnyAuthority("line cook", "chef");
            default -> false;
        };

        if (!canAccess) throw new EntityNotFoundException("Recipe not found");

        RecipeResponseDTO dto = mapRecipeResponse(recipe);
        dto.setCreatedByFullName(recipe.getCreatedBy().getFullName());
        dto.setCreatedByCurrentUser(recipe.getCreatedBy().getId().equals(currentUserId));
        if (recipe.getApprovedBy() != null) {
            dto.setApprovedByFullName(recipe.getApprovedBy().getFullName());
        }
        dto.setRevisionNotes(recipeLogService.getRevisionNotes(id));
        return dto;
    }

    @PreAuthorize("hasAnyAuthority('chef', 'line cook')")
    public RecipeEditDataResponseDTO getEditData() {
        RecipeEditDataResponseDTO responseDTO = new RecipeEditDataResponseDTO();

        responseDTO.setProductCategories(
            recipeIngredientCategoryRepository.findAll().stream()
                .filter(c -> c.getParentCategory() == null)
                .map(c -> mapProductCategoryForSelect(c))
                .toList()
        );

        responseDTO.setUnitCategories(
            unitCategoryRepository.findAll().stream()
                .map(uc -> {
                    RecipeForEditingResponseUnitCategoriesForSelectDTO dto = new RecipeForEditingResponseUnitCategoriesForSelectDTO();
                    dto.setId(uc.getId());
                    dto.setCategory(uc.getCategory());
                    dto.setUnits(
                        uc.getUnits().stream()
                            .map(u -> mapper.map(u, RecipeForEditingResponseUnitForSelectDTO.class))
                            .toList()
                    );
                    return dto;
                })
                .toList()
        );

        return responseDTO;
    }

    @PreAuthorize("hasAnyAuthority('chef', 'line cook')")
    public RecipeForEditingResponseDTO getRecipeForEditing(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                                        .orElseThrow(() -> new EntityNotFoundException("Recipe not found"));

        DatabaseUserDetails principal = (DatabaseUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long currentUserId = principal.getUser().getId();

        if (!recipe.getCreatedBy().getId().equals(currentUserId)) {
            throw new EntityNotFoundException("Recipe not found");
        }

        String statusName = recipe.getRecipeStatus().getStatus();
        boolean allowed = (principal.hasAnyAuthority("line cook") && Set.of("DRAFT", "RETURNED_FOR_REVISION").contains(statusName))
                       || (principal.hasAnyAuthority("chef") && Set.of("DRAFT", "APPROVED").contains(statusName));

        if (!allowed) {
            throw new EntityNotFoundException("Recipe not found");
        }

        RecipeForEditingResponseDTO responseDTO = new RecipeForEditingResponseDTO();
        responseDTO.setRecipe(mapRecipeResponse(recipe));

        responseDTO.setProductCategories(
            recipeIngredientCategoryRepository.findAll().stream()
                .filter(c -> c.getParentCategory() == null)
                .map(c -> mapProductCategoryForSelect(c))
                .toList()
        );

        responseDTO.setUnitCategories(
            unitCategoryRepository.findAll().stream()
                .map(uc -> {
                    RecipeForEditingResponseUnitCategoriesForSelectDTO dto = new RecipeForEditingResponseUnitCategoriesForSelectDTO();
                    dto.setId(uc.getId());
                    dto.setCategory(uc.getCategory());
                    dto.setUnits(
                        uc.getUnits().stream()
                            .map(u -> mapper.map(u, RecipeForEditingResponseUnitForSelectDTO.class))
                            .toList()
                    );
                    return dto;
                })
                .toList()
        );

        return responseDTO;
    }

    private RecipeForEditingResponseProductCategoriesForSelectDTO mapProductCategoryForSelect(RecipeIngredientCategory category) {
        RecipeForEditingResponseProductCategoriesForSelectDTO dto = new RecipeForEditingResponseProductCategoriesForSelectDTO();
        dto.setId(category.getId());
        dto.setCategory(category.getCategory());

        dto.setSubCategories(
            category.getSubCategories().stream()
                .map(sub -> mapProductCategoryForSelect(sub))
                .toList()
        );

        dto.setProducts(
            category.getProducts().stream()
                .map(p -> mapper.map(p, RecipeForEditingResponseProductCategoriesForSelectProductDTO.class))
                .toList()
        );

        return dto;
    }

    @PreAuthorize("hasAnyAuthority('chef', 'line cook')")
    public void saveDraft(Long id, SaveDraftRequestDTO dto) throws EntityNotFoundException {
        Recipe recipe = recipeRepository.findById(id)
                                        .orElseThrow(() -> new EntityNotFoundException("Recipe not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = ((DatabaseUserDetails) authentication.getPrincipal()).getUser().getId();

        if (!recipe.getCreatedBy().getId().equals(currentUserId))
            throw new EntityNotFoundException("Recipe not found");

        if (!Set.of("DRAFT", "RETURNED_FOR_REVISION").contains(recipe.getRecipeStatus().getStatus()))
            throw new EntityNotFoundException("Recipe not found");

        applyDraftData(recipe, dto);
    }

    private void applyDraftData(Recipe recipe, SaveDraftRequestDTO dto) {
        recipe.setTitle(dto.getTitle());
        recipe.setPreparationTime(dto.getPreparationTime());
        recipe.setServings(dto.getServings());
        recipe.setInstructions(dto.getInstructions());
        recipe.setNotes(dto.getNotes());
        recipe.setDescription(dto.getDescription());

        recipe.getRecipeProducts().clear();
        recipe.getRecipeCategories().clear();
        recipeRepository.saveAndFlush(recipe);

        recipe.getRecipeProducts().addAll(
            dto.getRecipeProducts()
                .stream()
                .map(productDTO -> {
                    RecipeProduct product = new RecipeProduct();

                    product.setRecipe(recipe);

                    product.setProduct(
                        productRepository.findById(productDTO.getProductId()).orElseThrow(() -> new EntityNotFoundException("Product not found"))
                    );

                    product.setPosition(productDTO.getPosition());
                    product.setQuantity(productDTO.getQuantity());

                    if (productDTO.getUnitId() != null) {
                        product.setUnit(
                            unitRepository.findById(productDTO.getUnitId()).orElseThrow(() -> new EntityNotFoundException("Unit not found"))
                        );
                    }

                    return product;
                })
                .toList()
        );

        recipe.getRecipeCategories().addAll(
            dto.getRecipeCategories()
                .stream()
                .map(categoryDTO -> mapper.map(categoryDTO, RecipeCategory.class))
                .toList()
        );

        recipeRepository.save(recipe);
    }

    @PreAuthorize("hasAnyAuthority('chef', 'line cook')")
    public void deleteDraft(Long id) throws EntityNotFoundException {
        Recipe recipe = recipeRepository.findById(id)
                                        .orElseThrow(() -> new EntityNotFoundException("Recipe not found"));  

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = ((DatabaseUserDetails) authentication.getPrincipal()).getUser().getId();
        
        if (!recipe.getCreatedBy().getId().equals(currentUserId)) {
            throw new EntityNotFoundException("Recipe not found");
        }

        if(!"DRAFT".equals(recipe.getRecipeStatus().getStatus())) 
            throw new EntityNotFoundException("Recipe not found");

        recipeRepository.delete(recipe);
    }

    @PreAuthorize("hasAuthority('line cook')")
    public void submitForApproval(Long id, SubmitRecipeRequestDTO dto) throws EntityNotFoundException {
        try {
            Recipe recipe = recipeRepository.findById(id)
                                            .orElseThrow(() -> new EntityNotFoundException("Recipe not found"));

            Long currentUserId = ((DatabaseUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getId();
            if (!recipe.getCreatedBy().getId().equals(currentUserId))
                throw new EntityNotFoundException("Recipe not found");

            String statusName = recipe.getRecipeStatus().getStatus();
            if (!Set.of("DRAFT", "RETURNED_FOR_REVISION").contains(statusName))
                throw new EntityNotFoundException("Recipe not found");

            User reviewer = userRepository.findById(dto.getReviewerId())
                .orElseThrow(() -> new EntityNotFoundException("Reviewer not found"));

            recipe.setRecipeStatus(recipeStatusRepository.findByStatus("WAITING_FOR_APPROVAL")
                .orElseThrow(() -> new EntityNotFoundException("Status not found")));
            recipeRepository.save(recipe);

            recipeReviewLogService.createLog(recipe, reviewer, "SUBMITTED_FOR_APPROVAL");
        } catch (JsonProcessingException e) {
            throw new ValidationException();
        }
        
    }

    @PreAuthorize("hasAuthority('chef')")
    public RecipeResponseDTO getRecipeForReview(Long id) throws EntityNotFoundException {
        Recipe recipe = getReviewableRecipe(id);
        return mapRecipeResponse(recipe);
    }

    @PreAuthorize("hasAuthority('chef')")
    public void returnRecipeForRevision(Long id, ReturnRecipeForRevisionRequestDTO dto) throws EntityNotFoundException {
        try {
            Recipe recipe = getReviewableRecipe(id);
            User currentUser = ((DatabaseUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

            recipe.setRecipeStatus(recipeStatusRepository.findByStatus("RETURNED_FOR_REVISION")
                .orElseThrow(() -> new EntityNotFoundException("Status not found")));
            recipeRepository.save(recipe);

            recipeReviewLogService.createLog(recipe, currentUser, "RETURNED_FOR_REVISION", dto.getReturnNotes());
        } catch (JsonProcessingException e) {
            throw new ValidationException();
        }
    }

    @PreAuthorize("hasAuthority('chef')")
    public void discardRecipe(Long id) throws EntityNotFoundException {
        try {
            Recipe recipe = getReviewableRecipe(id);
            User currentUser = ((DatabaseUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

            recipe.setRecipeStatus(recipeStatusRepository.findByStatus("DISCARDED")
                .orElseThrow(() -> new EntityNotFoundException("Status not found")));
            recipe.setActive(null);
            recipeRepository.save(recipe);

            recipeReviewLogService.createLog(recipe, currentUser, "DISCARDED");
        } catch (JsonProcessingException e) {
            throw new ValidationException();
        }
    }

    @PreAuthorize("hasAuthority('chef')")
    public void selfApproveRecipe(Long id) throws EntityNotFoundException {
        try {
            Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recipe not found"));

            User currentUser = ((DatabaseUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

            if (!recipe.getCreatedBy().getId().equals(currentUser.getId()))
                throw new EntityNotFoundException("Recipe not found");

            String statusName = recipe.getRecipeStatus().getStatus();
            if (!Set.of("DRAFT", "RETURNED_FOR_REVISION").contains(statusName))
                throw new EntityNotFoundException("Recipe not found");

            validateForApproval(recipe);

            recipe.setRecipeStatus(recipeStatusRepository.findByStatus("APPROVED")
                .orElseThrow(() -> new EntityNotFoundException("Status not found")));
            recipe.setActive(true);
            recipe.setApprovedBy(currentUser);
            recipeRepository.save(recipe);

            recipeReviewLogService.createLog(recipe, currentUser, "APPROVED");
        } catch (JsonProcessingException e) {
            throw new ValidationException();
        }
    }

    @PreAuthorize("hasAuthority('chef')")
    public void approveRecipe(Long id) throws EntityNotFoundException {
        try {
            Recipe recipe = getReviewableRecipe(id);
            User currentUser = ((DatabaseUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

            validateForApproval(recipe);

            recipe.setRecipeStatus(recipeStatusRepository.findByStatus("APPROVED")
                .orElseThrow(() -> new EntityNotFoundException("Status not found")));
            recipe.setActive(true);
            recipe.setApprovedBy(currentUser);
            recipeRepository.save(recipe);

            recipeReviewLogService.createLog(recipe, currentUser, "APPROVED");
        } catch (JsonProcessingException e) {
            throw new ValidationException();
        }

    }

    private void validateForApproval(Recipe recipe) {
        SaveRecipeRequestDTO dto = new SaveRecipeRequestDTO();
        dto.setTitle(recipe.getTitle());
        dto.setPreparationTime(recipe.getPreparationTime());
        dto.setServings(recipe.getServings());
        dto.setInstructions(recipe.getInstructions());
        dto.setNotes(recipe.getNotes());
        dto.setDescription(recipe.getDescription());

        dto.setRecipeProducts(recipe.getRecipeProducts().stream()
            .map(p -> {
                SaveRecipeRequestProductDTO pd = new SaveRecipeRequestProductDTO();
                pd.setProductId(p.getProduct().getId());
                pd.setPosition(p.getPosition());
                pd.setQuantity(p.getQuantity());
                pd.setUnitId(p.getUnit().getId());
                return pd;
            }).toList());

        dto.setRecipeCategories(recipe.getRecipeCategories().stream()
            .map(c -> {
                SaveRecipeRequestCategoryDTO cd = new SaveRecipeRequestCategoryDTO();
                cd.setRecipeCategoryId(c.getId());
                return cd;
            }).toList());

        Set<ConstraintViolation<SaveRecipeRequestDTO>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            List<Map<String, String>> errors = violations.stream()
                .map(v -> Map.of("field", v.getPropertyPath().toString(), "message", v.getMessage()))
                .toList();
            throw new ValidationErrorsException(errors);
        }
    }

    private Recipe getReviewableRecipe(Long id) throws EntityNotFoundException {
        Recipe recipe = recipeRepository.findById(id)
                                        .orElseThrow(() -> new EntityNotFoundException("Recipe not found"));

        if (!"WAITING_FOR_APPROVAL".equals(recipe.getRecipeStatus().getStatus()))
            throw new EntityNotFoundException("Recipe not found");

        Long currentUserId = ((DatabaseUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getId();

        RecipeReviewLog latestSubmission = recipeReviewLogRepository
            .findFirstByRecipeIdAndRecipeReviewEventEventOrderByCreatedAtDesc(id, "SUBMITTED_FOR_APPROVAL")
            .orElseThrow(() -> new EntityNotFoundException("No review submission found for this recipe"));

        if (!latestSubmission.getReviewer().getId().equals(currentUserId))
            throw new EntityNotFoundException("Recipe not found");

        return recipe;
    }

    @PreAuthorize("hasAnyAuthority('chef')")
    public void saveRecipe(Long id, SaveRecipeRequestDTO dto) throws EntityNotFoundException {
        try {
            Recipe recipe = recipeRepository.findById(id)
                                            .orElseThrow(() -> new EntityNotFoundException("Recipe not found"));

            if (!"APPROVED".equals(recipe.getRecipeStatus().getStatus()))
                throw new EntityNotFoundException("Recipe not found");

            User currentUser = ((DatabaseUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

            String oldSnapshot = recipeLogService.createRecipeSnapshot(recipe);

            recipe.setTitle(dto.getTitle());
            recipe.setPreparationTime(dto.getPreparationTime());
            recipe.setServings(dto.getServings());
            recipe.setInstructions(dto.getInstructions());
            recipe.setNotes(dto.getNotes());
            recipe.setDescription(dto.getDescription());

            recipe.getRecipeProducts().clear();
            recipe.getRecipeCategories().clear();
            recipeRepository.saveAndFlush(recipe);

            recipe.getRecipeProducts().addAll(
                dto.getRecipeProducts().stream()
                    .map(productDTO -> {
                        RecipeProduct product = new RecipeProduct();

                        product.setRecipe(recipe);

                        product.setProduct(
                            productRepository.findById(productDTO.getProductId()).orElseThrow(() -> new EntityNotFoundException("Product not found"))
                        );

                        product.setPosition(productDTO.getPosition());
                        product.setQuantity(productDTO.getQuantity());

                        product.setUnit(
                            unitRepository.findById(productDTO.getUnitId()).orElseThrow(() -> new EntityNotFoundException("Unit not found"))
                        );

                        return product;
                    })
                    .toList()
            );

            recipe.getRecipeCategories().addAll(
                dto.getRecipeCategories().stream()
                    .map(categoryDTO -> mapper.map(categoryDTO, RecipeCategory.class))
                    .toList()
            );

            recipeRepository.save(recipe);

            Recipe updatedRecipe = recipeRepository.findById(id)
                                    .orElseThrow(() -> new EntityNotFoundException("Recipe not found"));

            String newSnapshot = recipeLogService.createRecipeSnapshot(updatedRecipe);

            recipeEditLogService.createLog(oldSnapshot, newSnapshot, updatedRecipe, currentUser);
        } catch (JsonProcessingException e) {
            throw new ValidationException();
        }
    }

    @PreAuthorize("hasAuthority('chef')")
    public void archiveRecipe(Long id) throws EntityNotFoundException {
        try {
            Recipe recipe = recipeRepository.findById(id)
                                            .orElseThrow(() -> new EntityNotFoundException("Recipe not found"));

            if (!"APPROVED".equals(recipe.getRecipeStatus().getStatus()))
                throw new EntityNotFoundException("Recipe not found");

            if (!recipe.getMenuSectionRecipes().isEmpty())
                throw new com.ordino.core.exception.ValidationException("recipeId", "Recipe is currently in a menu");

            User currentUser = ((DatabaseUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

            recipe.setRecipeStatus(recipeStatusRepository.findByStatus("ARCHIVED")
                .orElseThrow(() -> new EntityNotFoundException("Status not found")));
            recipe.setActive(null);
            recipeRepository.save(recipe);

            recipeArchiveLogService.createLog(recipe, currentUser, "ARCHIVED");
        } catch (JsonProcessingException e) {
            throw new ValidationException();
        }
    }

    @PreAuthorize("hasAuthority('chef')")
    public void unarchiveRecipe(Long id) throws EntityNotFoundException {
        try {
            Recipe recipe = recipeRepository.findById(id)
                                            .orElseThrow(() -> new EntityNotFoundException("Recipe not found"));

            if (!"ARCHIVED".equals(recipe.getRecipeStatus().getStatus()))
                throw new EntityNotFoundException("Recipe not found");

            if (recipeRepository.existsByTitleAndActiveTrue(recipe.getTitle()))
                throw new com.ordino.core.exception.ValidationException("title", "A recipe with this title is already approved");

            User currentUser = ((DatabaseUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

            recipe.setRecipeStatus(recipeStatusRepository.findByStatus("APPROVED")
                .orElseThrow(() -> new EntityNotFoundException("Status not found")));
            recipe.setActive(true);
            recipeRepository.save(recipe);

            recipeArchiveLogService.createLog(recipe, currentUser, "RETURNED_TO_APPROVED");
        } catch (JsonProcessingException e) {
            throw new ValidationException();
        }
    }
}
