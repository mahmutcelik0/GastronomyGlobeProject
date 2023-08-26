package com.globe.gastronomy.backend.dto;

import lombok.Data;

@Data
public class ForgotPasswordDto {
    private String language;
    private String email;
}
