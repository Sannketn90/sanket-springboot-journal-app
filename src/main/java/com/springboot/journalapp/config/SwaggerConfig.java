package com.springboot.journalapp.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        SecurityScheme bearerScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .name(securitySchemeName);

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(securitySchemeName);

        Components components = new Components()
                .addSecuritySchemes(securitySchemeName, bearerScheme);

        return new OpenAPI()
                .info(new Info().title("Journal App API").version("1.0"))
                .components(components)
                .addSecurityItem(securityRequirement);
    }
}
