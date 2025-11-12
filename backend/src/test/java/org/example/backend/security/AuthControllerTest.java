package org.example.backend.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void me_shouldReturn401_whenNoAuthentication() throws Exception {
        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void me_shouldReturnUserInfo_forUsernamePasswordAuth() throws Exception {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                "alice",
                "N/A",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        mockMvc.perform(get("/api/auth/me").with(authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("alice"))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"))

                .andExpect(jsonPath("$.provider").doesNotExist())
                .andExpect(jsonPath("$.email").doesNotExist())
                .andExpect(jsonPath("$.name").doesNotExist())
                .andExpect(jsonPath("$.login").doesNotExist());
    }

    @Test
    void me_shouldReturnOauthAttributes_forOauth2Auth() throws Exception {
        Map<String, Object> attrs = Map.of(
                "email", "user@example.com",
                "name", "Yaroslava",
                "login", "mistressslava"
        );
        OAuth2User oAuth2User = new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority("OIDC_USER")),
                attrs,
                "name"
        );

        var oauthAuth = new OAuth2AuthenticationToken(
                oAuth2User,
                List.of(new SimpleGrantedAuthority("OIDC_USER")),
                "github"
        );

        mockMvc.perform(get("/api/auth/me").with(authentication(oauthAuth)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                // базові поля
                .andExpect(jsonPath("$.username").value(oAuth2User.getName()))
                .andExpect(jsonPath("$.roles[0]").value("OIDC_USER"))
                // специфічні OAuth2
                .andExpect(jsonPath("$.provider").value("github"))
                .andExpect(jsonPath("$.email").value("user@example.com"))
                .andExpect(jsonPath("$.name").value("Yaroslava"))
                .andExpect(jsonPath("$.login").value("mistressslava"));
    }

    @Test
    @WithMockUser
    void csrfToken_shouldReturnRequestAttribute_whenAuthenticated() throws Exception {
        var token = new DefaultCsrfToken("X-CSRF-TOKEN", "_csrf", "abc123");

        mockMvc.perform(get("/api/auth/csrf-token")
                        .requestAttr("_csrf", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("abc123"));
    }

}