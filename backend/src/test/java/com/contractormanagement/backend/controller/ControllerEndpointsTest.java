package com.contractormanagement.backend.controller;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.contractormanagement.backend.entity.User;
import com.contractormanagement.backend.entity.UserRole;
import com.contractormanagement.backend.repository.UserRepository;
import com.contractormanagement.backend.security.UserPrincipal;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ControllerEndpointsTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private static final String EMAIL = "controller.endpoint.user@example.com";
    private static final String PASSWORD = "StrongPassword123";
    private static final String NAME = "Controller Test User";

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(springSecurity())
            .build();
        userRepository.deleteAll();
    }

    @Test
    @Order(1)
    void healthEndpoint_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("ok"));
    }

    @Test
    @Order(2)
    void userEndpoints_shouldWorkEndToEnd() throws Exception {
        String createPayload = String.format(
            "{\"name\":\"%s\",\"email\":\"%s\",\"password\":\"%s\",\"role\":\"ADMIN\",\"status\":\"ACTIVE\"}",
            NAME, EMAIL, PASSWORD
        );

        mockMvc.perform(post("/api/users/create")
                .with(user("admin@example.com").password("pass").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(createPayload))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("user created successfully")));

        User createdUser = userRepository.findByEmail(EMAIL);
        assertNotNull(createdUser);
        Long userId = createdUser.getId();

        mockMvc.perform(get("/api/users")
                .with(user("admin@example.com").password("pass").roles("ADMIN")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].email").value(EMAIL));

        mockMvc.perform(get("/api/users/{id}", userId)
                .with(user("admin@example.com").password("pass").roles("ADMIN")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value(EMAIL));

        mockMvc.perform(patch("/api/users/update/{id}", userId)
                .with(user("admin@example.com").password("pass").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Controller User\",\"status\":\"INACTIVE\"}"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("user updated successfully")));

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format("{\"email\":\"%s\",\"password\":\"%s\"}", EMAIL, PASSWORD)))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("user login successfully")));

        mockMvc.perform(delete("/api/users/delete/{id}", userId)
                .with(user("admin@example.com").password("pass").roles("ADMIN")))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("is deleted successfully")));
    }

    @Test
    @Order(3)
    void createUser_withDuplicateEmail_shouldReturnConflict() throws Exception {
        String createPayload = String.format(
            "{\"name\":\"%s\",\"email\":\"%s\",\"password\":\"%s\",\"role\":\"ADMIN\",\"status\":\"ACTIVE\"}",
            NAME, EMAIL, PASSWORD
        );

        mockMvc.perform(post("/api/users/create")
                .with(user("admin@example.com").password("pass").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(createPayload))
            .andExpect(status().isOk());

        mockMvc.perform(post("/api/users/create")
                .with(user("admin@example.com").password("pass").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(createPayload))
            .andExpect(status().isConflict())
            .andExpect(content().string(containsString("Email is already registered!")));
    }

    @Test
    @Order(4)
    void getUserById_whenUserDoesNotExist_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/users/{id}", 999999L)
                .with(user("admin@example.com").password("pass").roles("ADMIN")))
            .andExpect(status().isNotFound());
    }

    @Test
    @Order(5)
    void login_withBadCredentials_shouldReturnUnauthorized() throws Exception {
        String createPayload = String.format(
            "{\"name\":\"%s\",\"email\":\"%s\",\"password\":\"%s\",\"role\":\"ADMIN\",\"status\":\"ACTIVE\"}",
            NAME, EMAIL, PASSWORD
        );

        mockMvc.perform(post("/api/users/create")
                .with(user("admin@example.com").password("pass").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(createPayload))
            .andExpect(status().isOk());

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format("{\"email\":\"%s\",\"password\":\"wrongPassword\"}", EMAIL)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(6)
    void anonymousUser_cannotAccessProtectedUserEndpoints() throws Exception {
        mockMvc.perform(get("/api/users"))
            .andExpect(status().isForbidden());
    }

    @Test
    @Order(7)
    void normalUser_cannotCreateAdminUser() throws Exception {
        String createPayload = String.format(
            "{\"name\":\"%s\",\"email\":\"%s\",\"password\":\"%s\",\"role\":\"ADMIN\",\"status\":\"ACTIVE\"}",
            NAME, EMAIL, PASSWORD
        );

        mockMvc.perform(post("/api/users/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createPayload)
                .with(user("user@example.com").password("pass").roles("USER")))
            .andExpect(status().isForbidden());
    }

    @Test
    @Order(8)
    void adminUser_canAccessAllUsers() throws Exception {
        mockMvc.perform(get("/api/users")
                .with(user("admin@example.com").password("pass").roles("ADMIN")))
            .andExpect(status().isOk());
    }

    @Test
    @Order(9)
    void userCanAccessOwnProfileButNotOthers() throws Exception {
        User savedUser = new User();
        savedUser.setEmail("owner@example.com");
        savedUser.setName("Owner");
        savedUser.setPassword_hash("hashed-password");
        savedUser.setRole(UserRole.USER);
        savedUser.setStatus(com.contractormanagement.backend.entity.UserStatus.ACTIVE);
        savedUser = userRepository.save(savedUser);

        mockMvc.perform(get("/api/users/{id}", savedUser.getId())
                .with(user(new UserPrincipal(savedUser))))
            .andExpect(status().isOk());

        User otherUser = new User();
        otherUser.setEmail("other@example.com");
        otherUser.setName("Other");
        otherUser.setPassword_hash("hashed-password");
        otherUser.setRole(UserRole.USER);
        otherUser.setStatus(com.contractormanagement.backend.entity.UserStatus.ACTIVE);
        otherUser = userRepository.save(otherUser);

        mockMvc.perform(get("/api/users/{id}", otherUser.getId())
                .with(user(new UserPrincipal(savedUser))))
            .andExpect(status().isForbidden());
    }
}
