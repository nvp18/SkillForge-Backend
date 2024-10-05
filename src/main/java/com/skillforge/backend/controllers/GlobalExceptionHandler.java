package com.skillforge.backend.controllers;

import com.skillforge.backend.dto.ErrorDTO;
import com.skillforge.backend.exception.UserNotAuthenticatedException;
import com.skillforge.backend.exception.UserNotFoundException;
import com.skillforge.backend.utils.ExceptionCodes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotAuthenticatedException.class)
    ResponseEntity<ErrorDTO> handleRequestResourceNotFoundException() {
        ErrorDTO errorDTO = ErrorDTO
                .builder()
                .errorCode(ExceptionCodes.USER_NOT_AUTHENTICATED.toString())
                .errorMessage("User is not authenticated")
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDTO);
    }

    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<ErrorDTO> handleUserNotFoundException() {
        ErrorDTO errorDTO = ErrorDTO
                .builder()
                .errorCode(ExceptionCodes.USER_NOT_FOUND.toString())
                .errorMessage("Username/password is incorrect")
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
    }
}
