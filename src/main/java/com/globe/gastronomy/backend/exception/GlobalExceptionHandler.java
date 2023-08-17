package com.globe.gastronomy.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ErrorMessage> userNotFoundHandler(UserNotFoundException exception){
        return new ResponseEntity<>(new ErrorMessage(exception.getMessage(), HttpStatus.NOT_FOUND),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> globalExceptionHandler(Exception exception){
        return new ResponseEntity<>(new ErrorMessage(exception.getMessage(),HttpStatus.BAD_REQUEST),HttpStatus.BAD_REQUEST);
    }
}