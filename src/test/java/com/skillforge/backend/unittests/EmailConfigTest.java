package com.skillforge.backend.unittests;

import com.skillforge.backend.config.EmailConfig;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailConfigTest {
    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailConfig emailConfig;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        setPrivateField(emailConfig, "fromEmail", "noreply@skillforge.com");
    }

    private void setPrivateField(Object targetObject, String fieldName, Object value) throws Exception {
        Field field = targetObject.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(targetObject, value);
    }

    @Test
    void testSendEmail() throws Exception {
        String toEmail = "test@example.com";
        String username = "testuser";
        String password = "testpassword";

        MimeMessage mockMimeMessage = mock(MimeMessage.class);
        MimeMessageHelper mockMimeMessageHelper = mock(MimeMessageHelper.class);

        when(javaMailSender.createMimeMessage()).thenReturn(mockMimeMessage);
        doNothing().when(javaMailSender).send(mockMimeMessage);

        // Execute the method
        emailConfig.sendEmail(toEmail, password, username);

        // Verify interactions
        verify(javaMailSender, times(1)).createMimeMessage();
        verify(javaMailSender, times(1)).send(mockMimeMessage);
    }

    @Test
    void testSendEmailCatchBlock() {
        String toEmail = "test@example.com";
        String username = "testuser";
        String password = "testpassword";

        // Simulate exception when creating a MimeMessage
        when(javaMailSender.createMimeMessage()).thenThrow(new RuntimeException("Simulated Exception"));

        assertDoesNotThrow(() -> emailConfig.sendEmail(toEmail, password, username));

        verify(javaMailSender, times(1)).createMimeMessage();
    }

}
