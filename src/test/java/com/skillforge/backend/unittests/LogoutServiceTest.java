package com.skillforge.backend.unittests;

import com.skillforge.backend.config.LogoutService;
import com.skillforge.backend.entity.UserToken;
import com.skillforge.backend.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LogoutServiceTest {

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private LogoutService logoutService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogout_NoAuthorizationHeader() {
        when(request.getHeader("Authorization")).thenReturn(null);

        logoutService.logout(request, response, authentication);

        verify(tokenRepository, never()).findByToken(anyString());
    }

    @Test
    void testLogout_InvalidAuthorizationHeader() {
        when(request.getHeader("Authorization")).thenReturn("InvalidHeader");

        logoutService.logout(request, response, authentication);

        verify(tokenRepository, never()).findByToken(anyString());
    }

    @Test
    void testLogout_ValidTokenButNotInRepository() {
        String token = "validToken";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(tokenRepository.findByToken(token)).thenReturn(Optional.empty());

        logoutService.logout(request, response, authentication);

        verify(tokenRepository, times(1)).findByToken(token);
        verify(tokenRepository, never()).save(any(UserToken.class));
    }

    @Test
    void testLogout_ValidTokenInRepository() {
        String token = "validToken";
        UserToken userToken = new UserToken();
        userToken.setExpired(false);
        userToken.setRevoked(false);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(userToken));

        logoutService.logout(request, response, authentication);

        verify(tokenRepository, times(1)).findByToken(token);
        verify(tokenRepository, times(1)).save(userToken);
        assertTrue(userToken.isExpired());
        assertTrue(userToken.isRevoked());
    }
}
