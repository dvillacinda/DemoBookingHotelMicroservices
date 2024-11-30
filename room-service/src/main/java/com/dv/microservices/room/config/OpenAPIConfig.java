package com.dv.microservices.room.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI roomServiceAPI() {
        return new OpenAPI()
                .info(new Info().title("room Service API")
                        .description("This is the REST API for room Service")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0")));

    }

}
