package com.springboot.journalapp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI myCustomInfo() {
        return new OpenAPI().info(new Info()
                        .title("Journal APP API's")
                        .description("By Sannket")
                        .version("Spring Boot 2.7"));

    }
}
