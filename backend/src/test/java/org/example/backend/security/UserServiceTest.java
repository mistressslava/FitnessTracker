package org.example.backend.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepo userRepo;
    private JWTService jwtService;
    private AuthenticationManager authManager;
    private UserService userService;

    @BeforeEach
    void setup() {
        userRepo = mock(UserRepo.class);
        jwtService = mock(JWTService.class);
        authManager = mock(AuthenticationManager.class);
        userService = new UserService(userRepo, jwtService, authManager);
    }

    @Test
    void register_shouldEncodePasswordAndSave() {
        // GIVEN
        Users input = new Users(null, "john", "plain");
        when(userRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // WHEN
        Users saved = userService.register(input);

        // THEN
        verify(userRepo).save(any());
        assertEquals("john", saved.username());
        assertNotEquals("plain", saved.password(), "пароль має бути захешований");
    }

    @Test
    void verify_shouldReturnJwt_whenAuthenticationOk() {
        // GIVEN
        when(userRepo.findByUsername("john")).thenReturn(Optional.of(new Users("id","john","enc")));
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(authManager.authenticate(any())).thenReturn(auth);
        when(jwtService.generateToken("john")).thenReturn("jwt-123");


        // WHEN
        String token = userService.verify(new Users(null, "john", "pw"));

        // THEN
        assertEquals("jwt-123", token);
    }

    @Test
    void verify_shouldReturnFail_whenAuthenticationRejected() {
        // GIVEN
        when(userRepo.findByUsername("john")).thenReturn(Optional.of(new Users("id","john","enc")));
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(false);
        when(authManager.authenticate(any())).thenReturn(auth);

        // WHEN
        String token = userService.verify(new Users(null, "john", "bad"));

        // THEN
        assertEquals("Fail", token);
    }

    @Test
    void verify_shouldThrow_whenUserNotFound() {
        when(userRepo.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userService.verify(new Users(null, "ghost", "pw")));
    }


}