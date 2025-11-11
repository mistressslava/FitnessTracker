package org.example.backend.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepo userRepo;

    @BeforeEach
    void setup() {
        userRepo.deleteAll();
    }

    @Test
    @DirtiesContext
    void register_shouldCreateUserAndReturnToken() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"john","password":"pw"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @DirtiesContext
    void login_shouldReturn404_ifUserNotExists() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {"username":"ghost","password":"pw"}
                    """))
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext
    void login_shouldReturnToken_afterSuccessfulRegister() throws Exception {
        // register
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"john","password":"pw"}
                                """))
                .andExpect(status().isOk());

        // login
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"john","password":"pw"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @DirtiesContext
    void login_shouldReturn401_whenWrongPassword() throws Exception {
        // register
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"john","password":"pw"}
                                """))
                .andExpect(status().isOk());

        // wrong password
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"john","password":"WRONG"}
                                """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DirtiesContext
    void login_shouldReturn404_whenUserDoesNotExist() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {"username":"ghost","password":"pw"}
                    """))
                .andExpect(status().isNotFound())
                .andExpect(status().reason(containsString("User not found")));
    }


}