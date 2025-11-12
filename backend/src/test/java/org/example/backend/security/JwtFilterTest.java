package org.example.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class JwtFilterTest {

    @Mock
    JWTService jwtService;

    @Mock
    UserDetailsService userDetailsService;

    @InjectMocks
    JwtFilter filter;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private UserDetails user() {
        return new User("alice", "nopass",
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    void noAuthHeader_noCookies_chainContinues_noAuth() throws ServletException, IOException {
        var req = new MockHttpServletRequest();
        var res = new MockHttpServletResponse();
        FilterChain chain = spy(new MockFilterChain());

        filter.doFilter(req, res, chain);

        verify(chain, times(1)).doFilter(req, res);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verifyNoInteractions(jwtService, userDetailsService);
    }

    @Test
    void bearerToken_valid_setsAuthentication() throws Exception {
        var req = new MockHttpServletRequest();
        var res = new MockHttpServletResponse();
        var chain = new MockFilterChain();

        req.addHeader("Authorization", "Bearer token123");

        when(jwtService.extractUserName("token123")).thenReturn("alice");
        when(userDetailsService.loadUserByUsername("alice")).thenReturn(user());
        when(jwtService.validateToken("token123", user())).thenReturn(true);

        filter.doFilter(req, res, chain);

        var auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isNotNull();
        assertThat(auth.getName()).isEqualTo("alice");
        assertThat(auth.getAuthorities()).extracting("authority").containsExactly("ROLE_USER");

        assertThat(auth.getDetails()).isInstanceOf(WebAuthenticationDetails.class);

        verify(jwtService).extractUserName("token123");
        verify(jwtService).validateToken("token123", user());
        verify(userDetailsService).loadUserByUsername("alice");
    }

    @Test
    void bearerToken_invalid_doesNotAuthenticate() throws Exception {
        var req = new MockHttpServletRequest();
        var res = new MockHttpServletResponse();
        var chain = new MockFilterChain();

        req.addHeader("Authorization", "Bearer badToken");

        when(jwtService.extractUserName("badToken")).thenReturn("alice");
        when(userDetailsService.loadUserByUsername("alice")).thenReturn(user());
        when(jwtService.validateToken("badToken", user())).thenReturn(false);

        filter.doFilter(req, res, chain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void tokenFromCookie_valid_setsAuthentication() throws Exception {
        var req = new MockHttpServletRequest();
        var res = new MockHttpServletResponse();
        var chain = new MockFilterChain();

        req.setCookies(new Cookie("access_token", "cookieJwt"));

        when(jwtService.extractUserName("cookieJwt")).thenReturn("alice");
        when(userDetailsService.loadUserByUsername("alice")).thenReturn(user());
        when(jwtService.validateToken("cookieJwt", user())).thenReturn(true);

        filter.doFilter(req, res, chain);

        var auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isNotNull();
        assertThat(auth.getName()).isEqualTo("alice");
    }

    @Test
    void nonBearerAuthorizationHeader_ignored() throws Exception {
        var req = new MockHttpServletRequest();
        var res = new MockHttpServletResponse();
        var chain = new MockFilterChain();

        req.addHeader("Authorization", "Basic abc123");

        filter.doFilter(req, res, chain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verifyNoInteractions(jwtService, userDetailsService);
    }

    @Test
    void alreadyAuthenticated_context_notOverwritten() throws Exception {
        var existing = new UsernamePasswordAuthenticationToken("bob", "x",
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(existing);

        var req = new MockHttpServletRequest();
        var res = new MockHttpServletResponse();
        var chain = new MockFilterChain();

        req.addHeader("Authorization", "Bearer token123");

        filter.doFilter(req, res, chain);

        var auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isSameAs(existing);
        verifyNoInteractions(jwtService, userDetailsService);
    }
}
