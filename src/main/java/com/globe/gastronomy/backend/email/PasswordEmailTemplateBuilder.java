package com.globe.gastronomy.backend.email;

import com.globe.gastronomy.backend.constants.EmailConstants;
import com.globe.gastronomy.backend.model.RawEmailTemplate;
import com.globe.gastronomy.backend.utils.LogUtil;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PasswordEmailTemplateBuilder implements EmailTemplateBuilder {
    private final String firstName = EmailConstants.FIRSTNAME.getStr();
    private final String lastName = EmailConstants.LASTNAME.getStr();
    private final String passwordRefreshLink = EmailConstants.PASSWORD_REFRESH_LINK.getStr();


    @Override
    public void build(Map<String, String> tokens, RawEmailTemplate template) {
        try {
            template.setTemplateContent(template.getTemplateContent().replace(firstName, tokens.get(firstName)));
            template.setTemplateContent(template.getTemplateContent().replace(lastName, tokens.get(lastName)));
            template.setTemplateContent(template.getTemplateContent().replace(passwordRefreshLink, tokens.get(passwordRefreshLink)));
        } catch (Exception e) {
            LogUtil.printLog("TOKEN DOES NOT EXIST", PasswordEmailTemplateBuilder.class);
        }
    }
}
