package com.gracefullyugly.common.security.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("곱게 못난이, gracefully-ugly")
                .description("못난이 농산물 공동구매 사이트의 API 명세서 입니다.")
                .version("1.0.0"); // API 버전
    }
}
