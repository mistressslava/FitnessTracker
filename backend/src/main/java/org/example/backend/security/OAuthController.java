package org.example.backend.security;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class OAuthController {

    private final JWTService jwtService;
    private final UserRepo userRepo;

    public OAuthController(JWTService jwtService, UserRepo userRepo) {
        this.jwtService = jwtService;
        this.userRepo = userRepo;
    }

    @GetMapping("/success")
    public ResponseEntity<?> success(Authentication authentication) {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        Map<String, Object> attrs = oauthToken.getPrincipal().getAttributes();

        String username = (String) (attrs.get("login") != null
                ? attrs.get("login")
                : attrs.get("name"));

        if (username == null) {
            return ResponseEntity.badRequest().body("No username found from provider");
        }

        userRepo.findByUsername(username)
                .orElseGet(() -> userRepo.save(Users.oauth(username)));

        String token = jwtService.generateToken(username);

        return ResponseEntity.ok(Map.of(
                "token", token,
                "username", username
        ));
    }
}
