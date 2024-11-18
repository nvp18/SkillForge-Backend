package com.skillforge.backend.unittests;
import com.skillforge.backend.config.EmailConfig;
import com.skillforge.backend.config.JwtService;
import com.skillforge.backend.dto.ChangePasswordDTO;
import com.skillforge.backend.dto.GenericDTO;
import com.skillforge.backend.dto.UserDTO;
import com.skillforge.backend.entity.User;
import com.skillforge.backend.entity.UserToken;
import com.skillforge.backend.exception.*;
import com.skillforge.backend.exception.InternalServerException;
import com.skillforge.backend.exception.ResourceNotFoundException;
import com.skillforge.backend.exception.UserNotFoundException;
import com.skillforge.backend.repository.TokenRepository;
import com.skillforge.backend.repository.UserRepository;
import com.skillforge.backend.service.impl.UserService;
import com.skillforge.backend.utils.ROLES;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.Principal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private EmailConfig emailConfig;

    @InjectMocks
    private UserService userService;

    private BCryptPasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        encoder = new BCryptPasswordEncoder(12);
    }

    @Test
    void testLogin_ValidCredentials() {
        String username = "testuser";
        String password = "testpass";
        User user = new User();
        user.setUsername(username);
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(Boolean.TRUE);
        when(userRepository.findUserRole(username)).thenReturn(ROLES.EMPLOYEE.toString());
        when(userRepository.findByUsername(username)).thenReturn(user);
        when(jwtService.generateToken(username)).thenReturn("jwtToken");

        Map<String, Object> result = userService.login(username, password);

        assertNotNull(result);
        assertEquals(ROLES.EMPLOYEE.toString(), result.get("Role"));
        assertEquals("jwtToken", result.get("Token"));
        verify(tokenRepository, times(1)).save(any(UserToken.class));
    }

    @Test
    void testLogin_ValidCredentials2() {
        String username = "testuser";
        String password = "testpass";
        User user = new User();
        user.setUserId("123");
        user.setUsername(username);
        Authentication authentication = mock(Authentication.class);
        UserToken userToken = new UserToken();
        List<UserToken> userTokens = Arrays.asList(userToken);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(Boolean.TRUE);
        when(userRepository.findUserRole(username)).thenReturn(ROLES.EMPLOYEE.toString());
        when(userRepository.findByUsername(username)).thenReturn(user);
        when(tokenRepository.findAllValidTokens("123")).thenReturn(userTokens);
        when(jwtService.generateToken(username)).thenReturn("jwtToken");

        Map<String, Object> result = userService.login(username, password);

        assertNotNull(result);
        assertEquals(ROLES.EMPLOYEE.toString(), result.get("Role"));
        assertEquals("jwtToken", result.get("Token"));
        verify(tokenRepository, times(1)).save(any(UserToken.class));
    }

    @Test
    void testLogin_ValidCredentialsTokenError() {
        String username = "testuser";
        String password = "testpass";
        User user = new User();
        user.setUserId("123");
        user.setUsername(username);
        Authentication authentication = mock(Authentication.class);
        UserToken userToken = new UserToken();
        List<UserToken> userTokens = Arrays.asList(userToken);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(Boolean.TRUE);
        when(userRepository.findUserRole(username)).thenReturn(ROLES.EMPLOYEE.toString());
        when(userRepository.findByUsername(username)).thenReturn(user);
        when(tokenRepository.findAllValidTokens("123")).thenReturn(userTokens);
        when(tokenRepository.saveAll(userTokens)).thenThrow(new RuntimeException());
        when(jwtService.generateToken(username)).thenReturn("jwtToken");

        assertThrows(InternalServerException.class, () -> userService.login(username, password));
    }

    @Test
    void testLogin_InvalidCredentials() {
        when(authenticationManager.authenticate(any())).thenThrow(BadCredentialsException.class);
        assertThrows(UserNotFoundException.class, () -> userService.login("wronguser", "wrongpass"));
    }

    @Test
    void testLogin_NotAuthenticated() {
        String username = "testuser";
        String password = "testpass";
        User user = new User();
        user.setUsername(username);
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(Boolean.FALSE);
        assertThrows(UserNotAuthenticatedException.class, () -> userService.login(username, password));
    }

    @Test
    void testCreateUser_Success() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName("newuser");
        userDTO.setEmail("test@example.com");

        User user = new User();
        user.setUsername("newuser");
        user.setEmail("test@example.com");
        user.setRole(ROLES.EMPLOYEE.toString());

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO result = userService.createUser(userDTO);

        assertNotNull(result);
        verify(emailConfig, times(1)).sendEmail(eq("test@example.com"), anyString(), eq("newuser"));
    }

    @Test
    void testCreateUser_InternalServerException() {
        UserDTO userDTO = new UserDTO();
        when(userRepository.save(any(User.class))).thenThrow(RuntimeException.class);
        assertThrows(InternalServerException.class, () -> userService.createUser(userDTO));
    }

    @Test
    void testGetUserProfile_Success() {
        // Create a mock user
        User user = new User();
        user.setUsername("testuser");

        // Use UsernamePasswordAuthenticationToken directly as the principal
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);

        // No need to mock findByUsername here since the method gets the user from the principal
        UserDTO userDTO = userService.getUserProfile(principal);

        // Assert the result
        assertNotNull(userDTO);
        assertEquals("testuser", userDTO.getUserName());
    }

    @Test
    void testGetUserProfile_ResourceNotFoundException() {
        // Use UsernamePasswordAuthenticationToken directly
        Principal principal = new UsernamePasswordAuthenticationToken(null, null);

        // Expect ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserProfile(principal));
    }

    @Test
    void testGetUserProfile_InternalServerException() {

        assertThrows(InternalServerException.class, () -> userService.getUserProfile(null));
    }

    @Test
    void testUpdateProfile_Success() {
        // Create a mock user
        User user = new User();
        user.setFirstName("UpdatedFirstName");

        // Use UsernamePasswordAuthenticationToken directly as the principal
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);

        // Mock repository save method
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Prepare UserDTO for updating
        UserDTO updatedDTO = new UserDTO();
        updatedDTO.setFirstName("UpdatedFirstName");

        // Call the updateProfile method
        UserDTO result = userService.updateProfile(updatedDTO, principal);

        // Assert the result
        assertNotNull(result);
        assertEquals("UpdatedFirstName", result.getFirstName());
    }

    @Test
    void testUpdateProfile_Failure() {
        // Create a mock user
        User user = new User();
        user.setFirstName("UpdatedFirstName");

        // Use UsernamePasswordAuthenticationToken directly as the principal
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);

        // Mock repository save method
        when(userRepository.save(any(User.class))).thenReturn(user);


        assertThrows(InternalServerException.class, () -> userService.updateProfile(null, principal));

    }

    @Test
    void testChangePassword_Success() {
        // Create a mock user
        User user = new User();
        user.setPassword(encoder.encode("currentPassword"));

        // Use UsernamePasswordAuthenticationToken directly as the principal
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);

        // Mock repository save method
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Prepare ChangePasswordDTO
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setCurrentPassword("currentPassword");
        changePasswordDTO.setNewPassword("newPassword");
        changePasswordDTO.setConfirmPassword("newPassword");

        // Call the changePassword method
        GenericDTO result = userService.changePassword(changePasswordDTO, principal);

        // Assert the result
        assertNotNull(result);
        assertEquals("Password Changed Successfully", result.getMessage());
    }

    @Test
    void testChangePassword_GenericException() {
        // Create a mock user
        User user = new User();
        user.setPassword(encoder.encode("currentPassword"));

        // Use UsernamePasswordAuthenticationToken directly
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);

        // Prepare the ChangePasswordDTO
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setCurrentPassword("wrongPassword"); // This won't match
        changePasswordDTO.setNewPassword("newPassword");
        changePasswordDTO.setConfirmPassword("newPassword");

        // Expect GenericException due to incorrect current password
        assertThrows(GenericException.class, () -> userService.changePassword(changePasswordDTO, principal));
    }

    @Test
    void testChangePassword_GenericException2() {
        // Create a mock user
        User user = new User();
        user.setPassword(encoder.encode("currentPassword"));

        // Use UsernamePasswordAuthenticationToken directly
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);

        // Prepare the ChangePasswordDTO
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setCurrentPassword("currentPassword"); // This won't match
        changePasswordDTO.setNewPassword("newPassword1");
        changePasswordDTO.setConfirmPassword("newPassword");

        // Expect GenericException due to incorrect current password
        assertThrows(GenericException.class, () -> userService.changePassword(changePasswordDTO, principal));
    }

    @Test
    void testChangePassword_InteralServerException() {
        assertThrows(InternalServerException.class, () -> userService.changePassword(null, null));
    }

    @Test
    void testGetAllEmployees() {
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setUsername("Employee1");
        user1.setEmail("test@example.com");
        User user2 = new User();
        user2.setUsername("Employee1");
        user2.setEmail("test@example.com");
        users.add(user1);
        users.add(user2);

        when(userRepository.findAllEmployees()).thenReturn(users);

        List<UserDTO> result = userService.getAllEmployees();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetAllEmployeesException() {

        when(userRepository.findAllEmployees()).thenThrow(new RuntimeException());

        assertThrows(InternalServerException.class, () -> userService.getAllEmployees());
    }
}
