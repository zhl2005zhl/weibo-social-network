package com.weibo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI配置类
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info().title("微博系统API")
                        .description("一个完整的微博系统API，包含用户认证、动态管理、社交互动等功能")
                        .version("v1")
                        .contact(new Contact().name("开发团队").email("contact@example.com").url("https://example.com"))
                        .license(new License().name("MIT License").url("https://opensource.org/licenses/MIT")));
    }
}
