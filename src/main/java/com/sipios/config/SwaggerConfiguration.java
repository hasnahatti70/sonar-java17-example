package com.sipios.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Used to add Swagger Configuration
 *
 * The swagger is not provided in the production profile
 */
@Configuration
@EnableSwagger2
@Profile("!production")
public class SwaggerConfiguration {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .tags(
                new Tag("users", "Endpoints that impact users"),
                new Tag("auth", "Security endpoints"),
                new Tag("protected", "Endpoints that are protected"),
                new Tag("unprotected", "Endpoints that are not protected")
            )
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.any())
            .build();
    }
}
