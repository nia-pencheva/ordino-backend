package com.ordino.domain.recipes.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidInstructionsValidator implements ConstraintValidator<ValidInstructions, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;

        JsonNode root;
        try {
            root = objectMapper.readTree(value);
        } catch (Exception e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Invalid instructions format")
                   .addConstraintViolation();
            return false;
        }

        if (!root.isArray() || root.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("At least one step is required")
                   .addConstraintViolation();
            return false;
        }

        boolean valid = true;
        for (int i = 0; i < root.size(); i++) {
            String text = root.get(i).path("text").asText("");
            if (text.isBlank()) {
                if (valid) {
                    context.disableDefaultConstraintViolation();
                    valid = false;
                }
                context.buildConstraintViolationWithTemplate("Step must not be blank")
                       .addPropertyNode(null)
                           .inIterable().atIndex(i)
                       .addPropertyNode("text")
                       .addConstraintViolation();
            }
        }

        return valid;
    }
}