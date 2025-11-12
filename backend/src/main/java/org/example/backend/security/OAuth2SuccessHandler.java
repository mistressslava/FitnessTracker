package org.example.backend.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JWTService jwtService;
    private final UserRepo userRepo;

    @Value("${app.url}")
    private String appURL;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest req, HttpServletResponse res, Authentication auth) throws IOException {

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) auth;
        Map<String, Object> a = token.getPrincipal().getAttributes();

        String username = Stream.of("login", "email", "name")
                .map(k -> (String) a.get(k))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);


        if (username == null || username.isBlank()) {
            res.sendRedirect(appURL + "/login?error=nousername");
            return;
        }

        Users user = userRepo.findByUsername(username)
                .orElseGet(() -> userRepo.save(Users.oauth(username)));

        String jwt = jwtService.generateToken(user.username());

        ResponseCookie cookie = ResponseCookie.from("access_token", jwt)
                .httpOnly(true)
                .secure(true)
                //.secure(false)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofDays(7))
                .build();
        res.addHeader("Set-Cookie", cookie.toString());

        res.sendRedirect(appURL + "/");
    }
}
