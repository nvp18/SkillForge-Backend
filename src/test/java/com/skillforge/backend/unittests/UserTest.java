package com.skillforge.backend.unittests;

import com.skillforge.backend.entity.User;
import com.skillforge.backend.utils.ROLES;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .userId("1234")
                .username("testuser")
                .firstName("Test")
                .lastName("User")
                .email("test@example.com")
                .password("password")
                .role(ROLES.ADMIN.toString())
                .build();
    }

    @Test
    void testGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority(ROLES.ADMIN.toString())));
    }

    @Test
    void testIsAccountNonExpired() {
        assertTrue(user.isAccountNonExpired());
    }

    @Test
    void testIsAccountNonLocked() {
        assertTrue(user.isAccountNonLocked());
    }

    @Test
    void testIsCredentialsNonExpired() {
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    void testIsEnabled() {
        assertTrue(user.isEnabled());
    }

    @Test
    void testUserFields() {
        assertEquals("1234", user.getUserId());
        assertEquals("testuser", user.getUsername());
        assertEquals("Test", user.getFirstName());
        assertEquals("User", user.getLastName());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertEquals(ROLES.ADMIN.toString(), user.getRole());
    }

    @Test
    void testBuilderAndSetters() {
        User newUser = User.builder()
                .userId("5678")
                .username("newuser")
                .firstName("New")
                .lastName("User")
                .email("new@example.com")
                .password("newpassword")
                .role("ROLE_ADMIN")
                .build();

        assertEquals("5678", newUser.getUserId());
        assertEquals("newuser", newUser.getUsername());
        assertEquals("New", newUser.getFirstName());
        assertEquals("User", newUser.getLastName());
        assertEquals("new@example.com", newUser.getEmail());
        assertEquals("newpassword", newUser.getPassword());
        assertEquals("ROLE_ADMIN", newUser.getRole());
    }
}
