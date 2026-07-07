package com.ordino.domain.users;

import com.fasterxml.jackson.databind.JsonNode;
import com.ordino.domain.users.model.entity.User;
import com.ordino.domain.users.repository.UserRepository;
import com.ordino.support.AbstractIntegrationTest;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserManagementIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @org.junit.jupiter.api.Test
    void asAdmin_createUser_returns200AndUserPersistedInDatabase() throws Exception {
        fixtures.admin("admin.createuser");
        fixtures.role("chef");
        String token = loginAndGetToken("admin.createuser", "Passw0rd!");

        String body = """
                {"username":"new_chef_1","fullName":"New Chef","email":"new.chef.1@test.local","phoneNumber":"+35912345671","roles":["chef"]}""";

        mockMvc.perform(post("/users")
                        .header("Authorization", bearer(token))
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk());

        User persisted = userRepository.findByUsername("new_chef_1").orElseThrow();
        assertThat(persisted.getFullName()).isEqualTo("New Chef");
        assertThat(persisted.getEmail()).isEqualTo("new.chef.1@test.local");
        assertThat(persisted.getRoles()).extracting("role").containsExactly("chef");
    }

    @org.junit.jupiter.api.Test
    void asAdmin_resetUserPassword_forcesPasswordChangeRequiredOnNextLogin() throws Exception {
        fixtures.admin("admin.resetpass");
        User target = fixtures.chef("chef.getsreset");
        String adminToken = loginAndGetToken("admin.resetpass", "Passw0rd!");

        String responseBody = mockMvc.perform(post("/users/" + target.getId() + "/reset-password")
                        .header("Authorization", bearer(adminToken)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String newPassword = objectMapper.readTree(responseBody).get("newPassword").asText();

        JsonNode loginResponse = login("chef.getsreset", newPassword);
        assertThat(loginResponse.get("passwordChangeRequired").asBoolean()).isTrue();
    }

    @org.junit.jupiter.api.Test
    void asAdmin_deleteUser_returns204AndUserNoLongerCanLogin() throws Exception {
        fixtures.admin("admin.deleteuser");
        User target = fixtures.chef("chef.getsdeleted");
        String adminToken = loginAndGetToken("admin.deleteuser", "Passw0rd!");

        mockMvc.perform(delete("/users/" + target.getId())
                        .header("Authorization", bearer(adminToken)))
                .andExpect(status().isNoContent());

        JsonNode loginAfterDelete = login("chef.getsdeleted", "Passw0rd!");
        assertThat(loginAfterDelete.get("error").asText()).isEqualTo("INVALID_CREDENTIALS");
    }

    @ParameterizedTest
    @ValueSource(strings = {"line cook", "chef", "kitchen staff", "warehouse manager", "manager"})
    void asNonAdminRoles_createUser_returns403(String role) throws Exception {
        String username = "nonadmin_" + role.replace(" ", "_");
        fixtures.user(username, "Passw0rd!", false, role);
        String token = loginAndGetToken(username, "Passw0rd!");

        String body = """
                {"username":"should_not_be_created","fullName":"Nope","email":"nope@test.local","phoneNumber":"+35912345699","roles":["chef"]}""";

        mockMvc.perform(post("/users")
                        .header("Authorization", bearer(token))
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isForbidden());
    }
}
