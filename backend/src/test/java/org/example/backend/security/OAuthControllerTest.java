package org.example.backend.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OAuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepo userRepo;

    @BeforeEach
    void setup() {
        userRepo.deleteAll();
    }

    @Test
    void success_shouldCreateUser_whenMissing_andReturnToken_usingLogin() throws Exception {
        mockMvc.perform(get("/success")
                        .with(oauth2Login()
                                .attributes(a -> a.put("login", "octocat"))
                                .authorities(new SimpleGrantedAuthority("ROLE_USER"))
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("octocat"))
                .andExpect(jsonPath("$.token").isString());

        assertThat(userRepo.findByUsername("octocat")).isPresent();
    }

    @Test
    void success_shouldUseNameFallback_whenLoginMissing() throws Exception {
        mockMvc.perform(get("/success")
                        .with(oauth2Login()
                                .attributes(a -> a.put("name", "John Smith"))
                                .authorities(new SimpleGrantedAuthority("ROLE_USER"))
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("John Smith"))
                .andExpect(jsonPath("$.token").isString());

        assertThat(userRepo.findByUsername("John Smith")).isPresent();
    }

    @Test
    void success_shouldUseEmailFallback_whenLoginAndNameMissing() throws Exception {
        mockMvc.perform(get("/success")
                        .with(oauth2Login()
                                .attributes(a -> a.put("email", "user@example.com"))
                                .authorities(new SimpleGrantedAuthority("ROLE_USER"))
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user@example.com"))
                .andExpect(jsonPath("$.token").isString());

        assertThat(userRepo.findByUsername("user@example.com")).isPresent();
    }

    @Test
    void success_shouldPreferLogin_overName_whenBothPresent() throws Exception {
        mockMvc.perform(get("/success")
                        .with(oauth2Login()
                                .attributes(a -> {
                                    a.put("login", "gh-login");
                                    a.put("name", "Pretty Name");
                                })
                                .authorities(new SimpleGrantedAuthority("ROLE_USER"))
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("gh-login"))
                .andExpect(jsonPath("$.token").isString());

        assertThat(userRepo.findByUsername("gh-login")).isPresent();
        assertThat(userRepo.findByUsername("Pretty Name")).isNotPresent();
    }

    @Test
    void success_shouldReturn400_whenNoLoginNameOrEmail() throws Exception {
        mockMvc.perform(get("/success")
                        .with(oauth2Login()
                                .attributes(a -> a.put("id", "12345")) // irrelevant attribute only
                                .authorities(new SimpleGrantedAuthority("ROLE_USER"))
                        ))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No username found from provider"));
    }

    @Test
    void success_shouldReturn400_whenUsernameBlank() throws Exception {
        mockMvc.perform(get("/success")
                        .with(oauth2Login()
                                .attributes(a -> a.put("name", "   ")) // blanks
                                .authorities(new SimpleGrantedAuthority("ROLE_USER"))
                        ))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No username found from provider"));
    }

    @Test
    void success_shouldNotDuplicateUser_onSecondLogin() throws Exception {
        // first login creates the user
        mockMvc.perform(get("/success")
                        .with(oauth2Login()
                                .attributes(a -> a.put("login", "octo"))
                                .authorities(new SimpleGrantedAuthority("ROLE_USER"))
                        ))
                .andExpect(status().isOk());

        long countBefore = userRepo.count();

        // second login should not create another
        mockMvc.perform(get("/success")
                        .with(oauth2Login()
                                .attributes(a -> a.put("login", "octo"))
                                .authorities(new SimpleGrantedAuthority("ROLE_USER"))
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("octo"))
                .andExpect(jsonPath("$.token").isString());

        assertThat(userRepo.count()).isEqualTo(countBefore);
    }

}