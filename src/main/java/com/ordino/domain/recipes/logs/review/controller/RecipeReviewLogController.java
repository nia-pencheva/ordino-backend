package com.ordino.domain.recipes.logs.review.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ordino.domain.recipes.logs.review.model.dto.RecipeReviewApprovedLogEntryResponseDTO;
import com.ordino.domain.recipes.logs.review.model.dto.RecipeReviewDiscardedLogEntryResponseDTO;
import com.ordino.domain.recipes.logs.review.model.dto.RecipeReviewReturnedForRevisionLogEntryResponseDTO;
import com.ordino.domain.recipes.logs.review.model.dto.RecipeReviewSubmittedForApprovalLogEntryResponseDTO;
import com.ordino.domain.recipes.logs.review.service.RecipeReviewLogService;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/recipes/log/review")
@AllArgsConstructor
@Validated
public class RecipeReviewLogController {
    private RecipeReviewLogService reviewLogService;

    @GetMapping("/submitted-for-approval/{id}")
    public ResponseEntity<RecipeReviewSubmittedForApprovalLogEntryResponseDTO> getSubmittedForApprovalLogEntry(@PathVariable @Positive Long id) {
        return ResponseEntity.ok().body(reviewLogService.getSubmittedForApprovalLogEntry(id));
    }

    @GetMapping("/returned-for-revision/{id}")
    public ResponseEntity<RecipeReviewReturnedForRevisionLogEntryResponseDTO> getReturnedForRevisionLogEntry(@PathVariable @Positive Long id) {
        return ResponseEntity.ok().body(reviewLogService.getReturnedForRevisionLogEntry(id));
    }

    @GetMapping("/discarded/{id}")
    public ResponseEntity<RecipeReviewDiscardedLogEntryResponseDTO> getDiscardedLogEntry(@PathVariable @Positive Long id) {
        return ResponseEntity.ok().body(reviewLogService.getDiscardedLogEntry(id));
    }

    @GetMapping("/approved/{id}")
    public ResponseEntity<RecipeReviewApprovedLogEntryResponseDTO> getApprovedLogEntry(@PathVariable @Positive Long id) {
        return ResponseEntity.ok().body(reviewLogService.getApprovedLogEntry(id));
    }
}
