package com.sipios.service;

import com.sipios.config.ApplicationProperties;
import com.sipios.model.mail.EmailType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * A simple builder that hides the template engine used to generate the html
 */
@Service
@RequiredArgsConstructor
public class MailContentBuilder {
    private final TemplateEngine templateEngine;
    private final ApplicationProperties applicationProperties;

    private Context context = new Context();

    /**
     *
     * @param context The context used to build the email template
     * @return The MailContentBuilder
     */
    public MailContentBuilder withContext(Context context) {
        context.setVariable("logo", applicationProperties.getMail().getImages().getLogo());
        context.setVariable("backgroundColor", applicationProperties.getMail().getBackgroundColor());
        context.setVariable("contact", applicationProperties.getMail().getContact());
        context.setVariable("notificationUrl", applicationProperties.getMail().getNotificationUrl());
        this.context = context;

        return this;
    }

    /**
     * Build the email using the provided context
     *
     * @param type The type of email to build
     * @return The html content of the email
     */
    public String build(EmailType type) {
        return templateEngine.process(type.getTemplatePath(), context);
    }
}
