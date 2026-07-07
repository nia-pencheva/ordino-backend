package com.ordino.domain.users.validation.roles;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.ordino.domain.users.repository.RoleRepository;

import jakarta.validation.ConstraintValidatorContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CompatibleRolesValidatorTest {

    private final RoleRepository roleRepository = mock(RoleRepository.class);
    private final CompatibleRolesValidator validator = new CompatibleRolesValidator();

    private ConstraintValidatorContext contextAllowingCustomViolation() {
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder builder =
                mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        when(context.buildConstraintViolationWithTemplate(any())).thenReturn(builder);
        return context;
    }

    @Test
    void isValid_lineCookAndChefTogether_returnsInvalid() {
        ReflectionTestUtils.setField(validator, "roleRepository", roleRepository);
        when(roleRepository.findIncompatiblePairs(List.of("line cook", "chef")))
                .thenReturn(List.<Object[]>of(new Object[] { "line cook", "chef" }));

        boolean valid = validator.isValid(List.of("line cook", "chef"), contextAllowingCustomViolation());

        assertThat(valid).isFalse();
    }

    @Test
    void isValid_compatibleRoleCombination_returnsValid() {
        ReflectionTestUtils.setField(validator, "roleRepository", roleRepository);
        when(roleRepository.findIncompatiblePairs(List.of("chef", "warehouse manager")))
                .thenReturn(List.of());

        boolean valid = validator.isValid(List.of("chef", "warehouse manager"), contextAllowingCustomViolation());

        assertThat(valid).isTrue();
    }

    @Test
    void isValid_fewerThanTwoRoles_returnsValidWithoutQueryingRepository() {
        boolean valid = validator.isValid(List.of("chef"), contextAllowingCustomViolation());

        assertThat(valid).isTrue();
    }
}
