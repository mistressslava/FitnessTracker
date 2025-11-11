package org.example.backend.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/api/auth/register")
    public Map<String, String> register(@RequestBody Users user) {
        Users saved = service.register(user);
        // одразу згенеруй токен після реєстрації (або логіном)
        String jwt = service.verify(new Users(null, saved.username(), user.password()));
        if ("Fail".equals(jwt)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        return Map.of("token", jwt);
    }

    @PostMapping("/api/auth/login")
    public Map<String, String> login(@RequestBody Users user) {
        String jwt = service.verify(user);
        if ("Fail".equals(jwt)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        return Map.of("token", jwt);
    }
}
