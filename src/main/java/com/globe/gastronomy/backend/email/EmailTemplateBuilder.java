package com.globe.gastronomy.backend.email;

import com.globe.gastronomy.backend.model.RawEmailTemplate;

import java.util.Map;

public interface EmailTemplateBuilder {
    void build(Map<String, String> tokens, RawEmailTemplate template);
}
