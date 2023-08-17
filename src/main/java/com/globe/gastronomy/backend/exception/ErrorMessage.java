package com.globe.gastronomy.backend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class ErrorMessage {
    private String errorMessage;
    private HttpStatus errorStatus;
}
