package org.example.backend.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

        if (repo.findByUsername(user.username()).isEmpty()) {
            throw new UsernameNotFoundException("User not found: " + user.username());
        }

        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.username(), user.password())
            );

            if (authentication.isAuthenticated()) {
                return jwtService.generateToken(user.username());
            }

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid password for user: " + user.username());
        }

        return "Fail";
    }
}
