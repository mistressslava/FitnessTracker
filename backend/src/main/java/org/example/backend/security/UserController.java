package org.example.backend.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        String jwt = service.verify(new Users(null, saved.username(), user.password()));
        return Map.of("token", jwt);
    }

    @PostMapping("/api/auth/login")
    public Map<String, String> login(@RequestBody Users user) {
        try {
            String jwt = service.verify(user);
            return Map.of("token", jwt);
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}