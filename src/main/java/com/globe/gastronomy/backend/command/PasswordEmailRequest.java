package com.globe.gastronomy.backend.command;


public class PasswordEmailRequest extends EmailRequest {
    public PasswordEmailRequest(String from, String recipients, String subject, String content) {
        super(from, recipients, subject, content);
    }
}
