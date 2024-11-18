package com.skillforge.backend.unittests;

import com.skillforge.backend.controllers.UserController;
import com.skillforge.backend.dto.ChangePasswordDTO;
import com.skillforge.backend.dto.GenericDTO;
import com.skillforge.backend.dto.UserDTO;
import com.skillforge.backend.service.impl.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    public UserControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName("testUser");
        userDTO.setPassword("password123");
        Map<String, Object> mockToken = new HashMap<>();
        mockToken.put("token", "mockToken");

        when(userService.login("testUser", "password123")).thenReturn(mockToken);

        ResponseEntity<Map<String, Object>> response = userController.login(userDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockToken, response.getBody());
        verify(userService, times(1)).login("testUser", "password123");
    }

    @Test
    void testCreateUser() {
        UserDTO userDTO = new UserDTO();
        UserDTO mockCreatedUser = new UserDTO();

        when(userService.createUser(userDTO)).thenReturn(mockCreatedUser);

        ResponseEntity<UserDTO> response = userController.createUser(userDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockCreatedUser, response.getBody());
        verify(userService, times(1)).createUser(userDTO);
    }

    @Test
    void testGetUserProfile() {
        Principal mockPrincipal = mock(Principal.class);
        UserDTO mockUserProfile = new UserDTO();

        when(userService.getUserProfile(mockPrincipal)).thenReturn(mockUserProfile);

        ResponseEntity<UserDTO> response = userController.getUserProfile(mockPrincipal);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockUserProfile, response.getBody());
        verify(userService, times(1)).getUserProfile(mockPrincipal);
    }

    @Test
    void testUpdateUserProfile() {
        UserDTO userDTO = new UserDTO();
        Principal mockPrincipal = mock(Principal.class);
        UserDTO mockUpdatedUser = new UserDTO();

        when(userService.updateProfile(userDTO, mockPrincipal)).thenReturn(mockUpdatedUser);

        ResponseEntity<UserDTO> response = userController.updateUserProfile(userDTO, mockPrincipal);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockUpdatedUser, response.getBody());
        verify(userService, times(1)).updateProfile(userDTO, mockPrincipal);
    }

    @Test
    void testChangePassword() {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        Principal mockPrincipal = mock(Principal.class);
        GenericDTO mockResponse = new GenericDTO();

        when(userService.changePassword(changePasswordDTO, mockPrincipal)).thenReturn(mockResponse);

        ResponseEntity<GenericDTO> response = userController.changePassword(changePasswordDTO, mockPrincipal);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
        verify(userService, times(1)).changePassword(changePasswordDTO, mockPrincipal);
    }

    @Test
    void testGetAllEmployees() {
        List<UserDTO> mockEmployees = Arrays.asList(new UserDTO(), new UserDTO());

        when(userService.getAllEmployees()).thenReturn(mockEmployees);

        ResponseEntity<List<UserDTO>> response = userController.getAllEmployees();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockEmployees, response.getBody());
        verify(userService, times(1)).getAllEmployees();
    }
}
