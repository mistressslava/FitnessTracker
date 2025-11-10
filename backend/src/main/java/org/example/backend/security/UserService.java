package org.example.backend.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepo repo;

    private final JWTService jwtService;

    final AuthenticationManager authManager;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public UserService(UserRepo repo, JWTService jwtService, AuthenticationManager authManager) {
        this.repo = repo;
        this.jwtService = jwtService;
        this.authManager = authManager;
    }

    public Users register(Users user) {
        return repo.save(user.withPassword(encoder.encode(user.password())));
    }

    public String verify(Users user) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.username(), user.password()));

        if(authentication.isAuthenticated())
            return jwtService.generateToken(user.username());

        return "Fail";
    }
}
