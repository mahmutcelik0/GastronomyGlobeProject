package com.globe.gastronomy.backend.email;

import com.globe.gastronomy.backend.constants.EmailConstants;
import com.globe.gastronomy.backend.model.RawEmailTemplate;
import com.globe.gastronomy.backend.utils.LogUtil;

import java.util.Map;

public class WeeklyEmailTemplateBuilder implements EmailTemplateBuilder{
    private final String firstName = EmailConstants.FIRSTNAME.getStr();
    private final String lastName = EmailConstants.LASTNAME.getStr();

    @Override
    public void build(Map<String, String> tokens, RawEmailTemplate template) {
        try {
            template.setTemplateContent(template.getTemplateContent().replace(firstName, tokens.get(firstName)));
            template.setTemplateContent(template.getTemplateContent().replace(lastName, tokens.get(lastName)));
        } catch (Exception e) {
            LogUtil.printLog("TOKEN DOES NOT EXIST", PasswordEmailTemplateBuilder.class);
        }
    }
}
