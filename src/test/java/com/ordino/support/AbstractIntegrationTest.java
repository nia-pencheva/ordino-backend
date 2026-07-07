package com.ordino.support;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordino.domain.auth.model.dto.LoginRequestDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public abstract class AbstractIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected TestDataFactory fixtures;

    /** Logs in via the real /login endpoint and returns the access token. */
    protected String loginAndGetToken(String username, String password) throws Exception {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setUsername(username);
        dto.setPassword(password);

        String responseBody = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode json = objectMapper.readTree(responseBody);
        return json.get("token").asText();
    }

    /** Logs in via the real /login endpoint and returns the full parsed response body. */
    protected JsonNode login(String username, String password) throws Exception {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setUsername(username);
        dto.setPassword(password);

        String responseBody = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(responseBody);
    }

    protected String bearer(String token) {
        return "Bearer " + token;
    }

    /**
     * MockMvc's default requests leave {@code getServletPath()} empty (the full path lands in
     * {@code getPathInfo()} instead), unlike a real container's "/" default-servlet mapping.
     * {@code PasswordChangedFilter.shouldNotFilter} keys off {@code getServletPath()}, so tests
     * that must exercise its `/login`/`/refresh`/`/change-password` exemption while authenticated
     * need this to make the mock request match real deployment behavior.
     */
    protected static RequestPostProcessor withRealServletPath() {
        return request -> {
            request.setServletPath(request.getRequestURI());
            return request;
        };
    }
}
