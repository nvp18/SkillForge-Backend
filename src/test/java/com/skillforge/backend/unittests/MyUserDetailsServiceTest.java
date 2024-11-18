package com.skillforge.backend.unittests;

import com.skillforge.backend.entity.User;
import com.skillforge.backend.repository.UserRepository;
import com.skillforge.backend.service.impl.MyUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MyUserDetailsServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MyUserDetailsService myUserDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_Success() {
        // Mock user entity
        User mockUser = new User();
        mockUser.setUsername("testuser");
        mockUser.setPassword("password123");

        when(userRepository.findByUsername("testuser")).thenReturn(mockUser);

        // Call the method
        UserDetails userDetails = myUserDetailsService.loadUserByUsername("testuser");

        // Assertions
        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void testLoadUserByUsername_UsernameNotFoundException() {
        // Mock repository to return null
        when(userRepository.findByUsername("nonexistentuser")).thenReturn(null);

        // Expect UsernameNotFoundException
        assertThrows(UsernameNotFoundException.class, () -> myUserDetailsService.loadUserByUsername("nonexistentuser"));

        verify(userRepository, times(1)).findByUsername("nonexistentuser");
    }
}
