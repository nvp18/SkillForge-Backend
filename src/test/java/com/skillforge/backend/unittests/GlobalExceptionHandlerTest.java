package com.skillforge.backend.unittests;

import com.skillforge.backend.controllers.GlobalExceptionHandler;
import com.skillforge.backend.dto.ErrorDTO;
import com.skillforge.backend.utils.ExceptionCodes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    void testHandleRequestResourceNotFoundException() {
        ResponseEntity<ErrorDTO> response = globalExceptionHandler.handleRequestResourceNotFoundException();

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ExceptionCodes.USER_NOT_AUTHENTICATED.toString(), response.getBody().getErrorCode());
        assertEquals("User is not authenticated", response.getBody().getErrorMessage());
    }

    @Test
    void testHandleUserNotFoundException() {
        ResponseEntity<ErrorDTO> response = globalExceptionHandler.handleUserNotFoundException();

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ExceptionCodes.USER_NOT_FOUND.toString(), response.getBody().getErrorCode());
        assertEquals("Username/password is incorrect", response.getBody().getErrorMessage());
    }

    @Test
    void testHandleInternalServerException() {
        ResponseEntity<ErrorDTO> response = globalExceptionHandler.handleInternalServerException();

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ExceptionCodes.INTERNAL_SERVER_ERROR.toString(), response.getBody().getErrorCode());
        assertEquals("INTERNAL SERVER ERROR", response.getBody().getErrorMessage());
    }

    @Test
    void testHandleResourceNotFoundException() {
        ResponseEntity<ErrorDTO> response = globalExceptionHandler.handleResourceNotFoundException();

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ExceptionCodes.RESOURCE_NOT_FOUND.toString(), response.getBody().getErrorCode());
        assertEquals("Resource Not Found", response.getBody().getErrorMessage());
    }

    @Test
    void testHandleGenericException() {
        ResponseEntity<ErrorDTO> response = globalExceptionHandler.handleGenericException();

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ExceptionCodes.GENERIC_EXCEPTION.toString(), response.getBody().getErrorCode());
        assertEquals("Generic Exception", response.getBody().getErrorMessage());
    }
}
