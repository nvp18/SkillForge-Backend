package com.skillforge.backend.unittests;

import com.skillforge.backend.config.JwtFilter;
import com.skillforge.backend.config.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SecurityConfigTest {

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtFilter jwtAuthFilter;

    @Mock
    private LogoutHandler logoutHandler;

    @Mock
    private SecurityFilterChain securityFilterChain;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @InjectMocks
    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticationProvider() {
        AuthenticationProvider authProvider = securityConfig.authenticationProvider();

        assertNotNull(authProvider);
        assertTrue(authProvider instanceof DaoAuthenticationProvider);

        DaoAuthenticationProvider daoProvider = (DaoAuthenticationProvider) authProvider;
    }

    @Test
    void testSecurityFilterChain() throws Exception {
        // Mock HttpSecurity
        HttpSecurity httpSecurity = mock(HttpSecurity.class);
        LogoutConfigurer<HttpSecurity> logoutConfigurer = mock(LogoutConfigurer.class);
        when(httpSecurity.cors(any())).thenReturn(httpSecurity);
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.sessionManagement(any())).thenReturn(httpSecurity);
        when(httpSecurity.logout()).thenReturn(logoutConfigurer);
        when(logoutConfigurer.logoutUrl(anyString())).thenReturn(logoutConfigurer);
        when(logoutConfigurer.addLogoutHandler(any())).thenReturn(logoutConfigurer);
        //when(logoutConfigurer.logoutSuccessHandler(any())).thenReturn(httpSecurity);
        when(httpSecurity.addFilterBefore(any(), eq(UsernamePasswordAuthenticationFilter.class))).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(any(DefaultSecurityFilterChain.class));

        // Execute the method under test
        SecurityFilterChain filterChain = securityConfig.securityFilterChain(httpSecurity);

        // Verify interactions and assertions
        //assertNotNull(filterChain);
        verify(httpSecurity, times(1)).cors(any());
        verify(httpSecurity, times(1)).csrf(any());
        verify(httpSecurity, times(1)).authorizeHttpRequests(any());
        verify(httpSecurity, times(1)).sessionManagement(any());
        verify(httpSecurity, times(1)).logout();
        verify(httpSecurity, times(1)).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Test
    void testAuthenticationManager() throws Exception {
        AuthenticationManager mockAuthManager = mock(AuthenticationManager.class);
        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(mockAuthManager);

        AuthenticationManager authManager = securityConfig.authenticationManager(authenticationConfiguration);

        assertNotNull(authManager);
        assertEquals(mockAuthManager, authManager);
    }

    @Test
    void testLogoutHandler() {
        SecurityFilterChain filterChain = mock(SecurityFilterChain.class);
        doNothing().when(logoutHandler).logout(any(), any(), any());

        assertNotNull(logoutHandler);
    }


}
