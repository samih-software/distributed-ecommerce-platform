package com.personal.store;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.store.auth.LoginRequest;
import com.personal.store.users.RegisterUserRequest;
import com.personal.store.users.Role;
import com.personal.store.users.User;
import com.personal.store.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthenticationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {

        userRepository.deleteAll();


        User user = new User();
        user.setEmail("sa@gmail.com");
        user.setName("Samih");
        user.setPassword(passwordEncoder.encode("123456"));
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    @Test
    void testUserLoginSuccess() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("sa@gmail.com");
        request.setPassword("123456");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void login_shouldReturnTokens_whenCredentialsAreValid() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("sa@gmail.com");
        request.setPassword("123456");

        String loginJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(cookie().exists("refreshToken"));
    }



    @Test
    void testUserRegistration() throws Exception {

        RegisterUserRequest request = new RegisterUserRequest();
        request.setName("Thomas");
        request.setEmail("thomas@gmail.com");
        request.setPassword("password123");

        String newUserJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserJson))
                .andExpect(status().isCreated());
    }
}