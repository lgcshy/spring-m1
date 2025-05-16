package com.example.springm1.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger配置类
 */
@Configuration
@ConditionalOnProperty(name = "swagger.enabled", havingValue = "true", matchIfMissing = true)
public class SwaggerConfig {

    @Value("${swagger.title:Spring Boot API}")
    private String title;

    @Value("${swagger.description:Spring Boot REST API Documentation}")
    private String description;

    @Value("${swagger.version:1.0.0}")
    private String version;

    @Value("${swagger.contact.name:Developer}")
    private String contactName;

    @Value("${swagger.contact.email:developer@example.com}")
    private String contactEmail;

    @Value("${swagger.contact.url:https://example.com}")
    private String contactUrl;

    @Value("${swagger.license:Apache 2.0}")
    private String license;

    @Value("${swagger.license-url:https://www.apache.org/licenses/LICENSE-2.0}")
    private String licenseUrl;

    /**
     * OpenAPI配置
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(title)
                        .description(description)
                        .version(version)
                        .contact(new Contact()
                                .name(contactName)
                                .email(contactEmail)
                                .url(contactUrl))
                        .license(new License()
                                .name(license)
                                .url(licenseUrl)))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()));
    }

    /**
     * 创建API密钥方案
     */
    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }
} 