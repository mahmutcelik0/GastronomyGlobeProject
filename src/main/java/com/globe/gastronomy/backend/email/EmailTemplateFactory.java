package com.globe.gastronomy.backend.email;

import com.globe.gastronomy.backend.model.RawEmailTemplate;

import java.util.Map;

public class EmailTemplateFactory {

    public static RawEmailTemplate generateTemplate(EmailTemplate templateType, Map<String, String> tokens, String language) {
        return templateType.getFinalTemplate(tokens, language);
    }
}
