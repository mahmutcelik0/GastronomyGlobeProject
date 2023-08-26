package com.globe.gastronomy.backend.email;

import com.globe.gastronomy.backend.model.RawEmailTemplate;

import java.util.Map;

public abstract class EmailTemplate {

    public RawEmailTemplate getFinalTemplate(Map<String, String> tokens, String language) {
        return buildTemplate(tokens, getTemplate(language));
    }

    abstract RawEmailTemplate getTemplate(String language);

    abstract EmailTemplateBuilder setBuilder();

    public RawEmailTemplate buildTemplate(Map<String, String> tokens, RawEmailTemplate template) {
        EmailTemplateBuilder builder = setBuilder();
        builder.build(tokens, template);
        return template;
    }


}
