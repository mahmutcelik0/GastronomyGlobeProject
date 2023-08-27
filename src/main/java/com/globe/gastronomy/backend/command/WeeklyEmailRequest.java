package com.globe.gastronomy.backend.command;

public class WeeklyEmailRequest extends EmailRequest{
    public WeeklyEmailRequest(String from, String recipients, String subject, String content) {
        super(from, recipients, subject, content);
    }
}
