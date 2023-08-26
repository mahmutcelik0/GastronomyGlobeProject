package com.globe.gastronomy.backend.repository;

import com.globe.gastronomy.backend.model.RawEmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailTemplateRepository extends JpaRepository<RawEmailTemplate, Long> {
    Optional<RawEmailTemplate> findEmailTemplateByTemplateNameAndTemplateLanguage(String emailTemplateName, String templateLanguage);
}
