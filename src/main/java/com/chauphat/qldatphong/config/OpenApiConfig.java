package com.chauphat.qldatphong.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("QL Đặt Phòng API")
                .version("v1")
                .description("Spring Boot API cho DB ql_dat_phong (SQL Server)")
        );
    }
}
