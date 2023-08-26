package com.globe.gastronomy.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "RAW_EMAIL_TEMPLATE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RawEmailTemplate {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "template_id")
    private Long id;

    private String templateName;

    private String templateLanguage;
    @Column(name = "template_content",length = 5000)
    private String templateContent;
}
