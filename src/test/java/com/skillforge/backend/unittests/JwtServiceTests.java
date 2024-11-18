package com.skillforge.backend.unittests;

import com.skillforge.backend.config.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTests {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    private String secretKey = "myJwtSecretKeyForTestingPurposesThatShouldBeBase64Encoded";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set the private @Value field jwtSecretKey using reflection
        setPrivateField(jwtService, "jwtSecretKey", secretKey);
    }

    private void setPrivateField(Object targetObject, String fieldName, Object value) {
        try {
            var field = targetObject.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(targetObject, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGenerateToken() {
        String username = "testuser";

        String token = jwtService.generateToken(username);

        assertNotNull(token);
        assertEquals(username, jwtService.extractUserName(token));
    }

    @Test
    void testExtractUserName() {
        String username = "testuser";
        String token = jwtService.generateToken(username);

        String extractedUsername = jwtService.extractUserName(token);

        assertEquals(username, extractedUsername);
    }

    @Test
    void testValidateToken_ValidToken() {
        String username = "testuser";
        when(userDetails.getUsername()).thenReturn(username);

        String token = jwtService.generateToken(username);

        boolean isValid = jwtService.validateToken(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    void testValidateToken_InvalidToken() {
        String username = "testuser";
        when(userDetails.getUsername()).thenReturn(username);

        String token = jwtService.generateToken("wronguser");

        boolean isValid = jwtService.validateToken(token, userDetails);

        assertFalse(isValid);
    }

    @Test
    void testIsTokenExpired_ValidToken() {
        String token = jwtService.generateToken("testuser");

        assertFalse(jwtService.validateToken(token, userDetails));
    }

}
