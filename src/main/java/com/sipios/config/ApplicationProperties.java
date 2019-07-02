package com.sipios.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * This class helps define the properties that can be loaded in the application.
 * Spring Boot will fill the values based on the property files / environment variables
 *
 * @see <a href="https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html#boot-features-external-config-conversion">https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html#boot-features-external-config-conversion</a>
 */
@Component
@ConfigurationProperties("application")
@Data
public class ApplicationProperties {
    private MailConfiguration mail;
    private ResetPassword resetPassword;
    private ValidationMail validationMail;

    @Data
    public static class MailConfiguration {
        private Images images;
        private String backgroundColor;
        private String contact;
        private String notificationUrl;
    }

    @Data
    public static class ResetPassword {
        private String url;
        private String from;
        private String subject;
        private Duration resetTokenDuration;
    }

    @Data
    public static class ValidationMail {
        private String url;
        private String from;
        private String subject;
        private Duration validationTokenDuration;
    }

    @Data
    public static class Images {
        private String logo;
    }
}
