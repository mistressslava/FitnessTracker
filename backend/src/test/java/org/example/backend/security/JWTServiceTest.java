package org.example.backend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JWTServiceTest {

    @Test
    void generateToken_containsSubjectAndNotExpiredYet() {
        JWTService service = new JWTService();

        String token = service.generateToken("alice");

        assertEquals("alice", service.extractUserName(token));

        Date now = new Date();
        var claims = Jwts.parser()
                .verifyWith(extractKeyViaReflection(service))
                .build()
                .parseSignedClaims(token)
                .getPayload();

        Date issuedAt = claims.getIssuedAt();
        Date exp = claims.getExpiration();

        assertNotNull(issuedAt);
        assertNotNull(exp);

        assertTrue(Math.abs(issuedAt.getTime() - now.getTime()) < Duration.ofSeconds(5).toMillis());

        long diffMillis = exp.getTime() - now.getTime();
        assertTrue(diffMillis > Duration.ofMinutes(55).toMillis() &&
                        diffMillis < Duration.ofMinutes(65).toMillis());
    }

    @Test
    void extractUserName_returnsSubject() {
        JWTService service = new JWTService();
        String token = service.generateToken("bob");
        assertEquals("bob", service.extractUserName(token));
    }

    @Test
    void validateToken_true_whenUserMatchesAndNotExpired() {
        JWTService service = new JWTService();
        String token = service.generateToken("carol");

        UserDetails ud = User.withUsername("carol").password("x").authorities("ROLE_USER").build();
        assertTrue(service.validateToken(token, ud));
    }

    @Test
    void validateToken_false_whenUserDoesNotMatch() {
        JWTService service = new JWTService();
        String token = service.generateToken("dave");

        UserDetails ud = User.withUsername("not-dave").password("x").authorities("ROLE_USER").build();
        assertFalse(service.validateToken(token, ud));
    }

    @Test
    void extractUserName_throwsOnMalformedToken() {
        JWTService service = new JWTService();
        assertThrows(RuntimeException.class, () -> {
            service.extractUserName("not-a-jwt");
        });
    }

    @Test
    void validateToken_false_whenExpired() {
        JWTService service = new JWTService();

        SecretKey key = extractKeyViaReflection(service);

        Date now = new Date();
        Date issuedAt = new Date(now.getTime() - Duration.ofHours(2).toMillis());
        Date expiredAt = new Date(now.getTime() - Duration.ofHours(1).toMillis());

        String expiredToken = Jwts.builder()
                .subject("eve")
                .issuedAt(issuedAt)
                .expiration(expiredAt)
                .signWith(key)
                .compact();

        UserDetails ud = User.withUsername("eve").password("x").authorities("ROLE_USER").build();
        assertFalse(service.validateToken(expiredToken, ud));
    }

    @Test
    void generateToken_producesVerifiableSignatureWithInternalSecret() {
        JWTService service = new JWTService();
        String token = service.generateToken("mallory");

        assertDoesNotThrow(() ->
                Jwts.parser()
                        .verifyWith(extractKeyViaReflection(service))
                        .build()
                        .parseSignedClaims(token)
        );
    }

    private SecretKey extractKeyViaReflection(JWTService service) {
        String base64 = (String) ReflectionTestUtils.getField(service, "secretKey");
        assertNotNull(base64);
        byte[] keyBytes = Decoders.BASE64.decode(base64);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
