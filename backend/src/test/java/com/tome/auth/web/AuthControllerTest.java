package com.tome.auth.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerTest {

  @Autowired private MockMvc mockMvc;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void register_then_me_with_access_token() throws Exception {
    String suffix = UUID.randomUUID().toString().substring(0, 8);
    String email = "user_" + suffix + "@example.com";
    String username = "user_" + suffix;

    String registerBody =
        """
            {
                "email": "%s",
                "password": "Password1!",
                "username": "%s",
                "firstName": "Test",
                "lastName": "User"
            }
            """
            .formatted(email, username);

    MvcResult registerResult =
        mockMvc
            .perform(
                post("/api/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(registerBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.accessToken").isNotEmpty())
            .andExpect(jsonPath("$.refreshToken").isNotEmpty())
            .andExpect(jsonPath("$.account.email").value(email))
            .andExpect(jsonPath("$.account.username").value(username))
            .andReturn();

    JsonNode json = objectMapper.readTree(registerResult.getResponse().getContentAsString());
    String accessToken = json.get("accessToken").asText();

    mockMvc
        .perform(get("/api/me").header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value(email))
        .andExpect(jsonPath("$.username").value(username));
  }

  @Test
  void login_with_wrong_password_is_unauthorized() throws Exception {
    String suffix = UUID.randomUUID().toString().substring(0, 8);
    String email = "login_" + suffix + "@example.com";
    String username = "login_" + suffix;

    String registerBody =
        """
            {
                "email": "%s",
                "password": "Password1!",
                "username": "%s",
                "firstName": "Test",
                "lastName": "User"
            }
            """
            .formatted(email, username);

    mockMvc
        .perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerBody))
        .andExpect(status().isCreated());

    String loginBody =
        """
            {
                "email": "%s",
                "password": "WrongPass1!"
            }
            """
            .formatted(email);

    mockMvc
        .perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(loginBody))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void me_without_token_is_unauthorized() throws Exception {
    mockMvc.perform(get("/api/me")).andExpect(status().isUnauthorized());
  }
}
