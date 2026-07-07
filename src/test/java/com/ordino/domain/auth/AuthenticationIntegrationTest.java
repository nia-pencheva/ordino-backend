package com.ordino.domain.auth;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.JsonNode;
import com.ordino.domain.auth.model.dto.ChangePasswordRequestDTO;
import com.ordino.domain.auth.model.dto.RefreshRequestDTO;
import com.ordino.domain.auth.model.entity.RefreshToken;
import com.ordino.domain.auth.repository.RefreshTokenRepository;
import com.ordino.domain.users.model.entity.User;
import com.ordino.support.AbstractIntegrationTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthenticationIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Test
    void login_correctCredentials_returns200WithAccessAndRefreshToken() throws Exception {
        fixtures.chef("chef.login.ok");

        JsonNode response = login("chef.login.ok", "Passw0rd!");

        assertThat(response.get("token").asText()).isNotBlank();
        assertThat(response.get("refreshToken").asText()).isNotBlank();
        assertThat(response.get("passwordChangeRequired").asBoolean()).isFalse();
    }

    @Test
    void login_wrongPassword_returns401InvalidCredentials() throws Exception {
        fixtures.chef("chef.login.wrongpass");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"chef.login.wrongpass","password":"WrongPassword1!"}"""))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("INVALID_CREDENTIALS"));
    }

    @Test
    void login_nonExistentUsername_returns401InvalidCredentials() throws Exception {
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"does.not.exist","password":"Passw0rd!"}"""))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("INVALID_CREDENTIALS"));
    }

    @Test
    void login_userWithNullPasswordChangedAt_returns200WithPasswordChangeRequiredTrue() throws Exception {
        fixtures.user("chef.needschange", "Passw0rd!", true, "chef");

        JsonNode response = login("chef.needschange", "Passw0rd!");

        assertThat(response.get("passwordChangeRequired").asBoolean()).isTrue();
    }

    @Test
    void refresh_validRefreshToken_returnsNewRotatedAccessAndRefreshToken() throws Exception {
        fixtures.chef("chef.refresh.ok");
        JsonNode loginResponse = login("chef.refresh.ok", "Passw0rd!");
        String originalRefreshToken = loginResponse.get("refreshToken").asText();

        RefreshRequestDTO dto = new RefreshRequestDTO();
        dto.setRefreshToken(originalRefreshToken);

        String responseBody = mockMvc.perform(post("/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode refreshed = objectMapper.readTree(responseBody);
        assertThat(refreshed.get("token").asText()).isNotBlank();
        assertThat(refreshed.get("refreshToken").asText()).isNotBlank().isNotEqualTo(originalRefreshToken);
        assertThat(refreshTokenRepository.findByToken(originalRefreshToken)).isEmpty();
    }

    @Test
    void refresh_invalidRefreshToken_returns401InvalidRefreshToken() throws Exception {
        RefreshRequestDTO dto = new RefreshRequestDTO();
        dto.setRefreshToken(UUID.randomUUID().toString());

        mockMvc.perform(post("/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("INVALID_REFRESH_TOKEN"));
    }

    @Test
    void refresh_expiredRefreshToken_returns401AndDeletesExpiredTokenRow() throws Exception {
        User user = fixtures.chef("chef.refresh.expired");

        RefreshToken expired = new RefreshToken();
        expired.setToken(UUID.randomUUID().toString());
        expired.setUser(user);
        expired.setExpiresAt(Instant.now().minusSeconds(60));
        refreshTokenRepository.save(expired);

        RefreshRequestDTO dto = new RefreshRequestDTO();
        dto.setRefreshToken(expired.getToken());

        mockMvc.perform(post("/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("REFRESH_TOKEN_EXPIRED"));

        assertThat(refreshTokenRepository.findByToken(expired.getToken())).isEmpty();
    }

    @Test
    void logout_validRefreshToken_deletesTokenFromDatabase() throws Exception {
        fixtures.chef("chef.logout.ok");
        JsonNode loginResponse = login("chef.logout.ok", "Passw0rd!");
        String refreshToken = loginResponse.get("refreshToken").asText();

        RefreshRequestDTO dto = new RefreshRequestDTO();
        dto.setRefreshToken(refreshToken);

        mockMvc.perform(post("/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());

        assertThat(refreshTokenRepository.findByToken(refreshToken)).isEmpty();
    }

    @Test
    void logout_thenReusingSameRefreshToken_returns401InvalidRefreshToken() throws Exception {
        fixtures.chef("chef.logout.reuse");
        JsonNode loginResponse = login("chef.logout.reuse", "Passw0rd!");
        String refreshToken = loginResponse.get("refreshToken").asText();

        RefreshRequestDTO logoutDto = new RefreshRequestDTO();
        logoutDto.setRefreshToken(refreshToken);
        mockMvc.perform(post("/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logoutDto)))
                .andExpect(status().isNoContent());

        RefreshRequestDTO refreshDto = new RefreshRequestDTO();
        refreshDto.setRefreshToken(refreshToken);
        mockMvc.perform(post("/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("INVALID_REFRESH_TOKEN"));
    }

    @Test
    void changePassword_whenPasswordChangeRequired_succeedsAndClearsRequiredFlagForSubsequentRequests() throws Exception {
        fixtures.user("chef.mustchange", "Passw0rd!", true, "chef");
        String token = loginAndGetToken("chef.mustchange", "Passw0rd!");

        ChangePasswordRequestDTO dto = new ChangePasswordRequestDTO();
        dto.setNewPassword("NewPassw0rd!1");
        dto.setConfirmPassword("NewPassw0rd!1");

        mockMvc.perform(post("/change-password")
                        .with(withRealServletPath())
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());

        JsonNode afterChange = login("chef.mustchange", "NewPassw0rd!1");
        assertThat(afterChange.get("passwordChangeRequired").asBoolean()).isFalse();
    }

    @Test
    void accessProtectedEndpoint_missingAuthorizationHeader_returns401() throws Exception {
        mockMvc.perform(get("/recipe-categories"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("UNAUTHORIZED"));
    }

    @Test
    void accessProtectedEndpoint_malformedJwt_returns401InvalidToken() throws Exception {
        mockMvc.perform(get("/recipe-categories")
                        .header("Authorization", bearer("not-a-valid-jwt")))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("INVALID_TOKEN"));
    }

    @Test
    void anyAuthenticatedUser_passwordChangeRequired_blockedFromAllEndpointsExceptChangePassword_returns403() throws Exception {
        fixtures.user("chef.blocked", "Passw0rd!", true, "chef");
        String token = loginAndGetToken("chef.blocked", "Passw0rd!");

        mockMvc.perform(get("/recipe-categories")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("PASSWORD_CHANGE_REQUIRED"));
    }
}
