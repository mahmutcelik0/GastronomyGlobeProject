package com.globe.gastronomy.backend.repository;

import com.globe.gastronomy.backend.model.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Long> {
    Optional<EmailTemplate> findEmailTemplateByTemplateNameAndTemplateLanguage(String emailTemplateName, String templateLanguage);
}
