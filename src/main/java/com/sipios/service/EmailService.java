package com.sipios.service;

import com.sipios.config.ApplicationProperties;
import com.sipios.model.mail.EmailType;
import com.sipios.model.user.AppUser;
import com.sipios.model.user.token.ResetPasswordToken;
import com.sipios.model.user.token.ValidateEmailToken;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.text.MessageFormat;

/**
 * Email service that handles creating Mail
 */
@Service
@AllArgsConstructor
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender emailSender;

    private final MailContentBuilder mailContentBuilder;

    private final ApplicationProperties applicationProperties;

    /**
     * Prepares the email content and sends it
     *
     * @param to Email of the recipient
     * @param subject Subject of the email
     * @param context key -> value object that will be provided to the mail builder
     * @param type Email type that will be provided to the email builder
     */
    private void sendTemplateMessage(String to, String from, String subject, Context context, EmailType type) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(from);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(mailContentBuilder.withContext(context).build(type), true);
        };
        try {
            emailSender.send(messagePreparator);
        } catch (MailException e) {
            logger.error(
                MessageFormat.format(
                    "Could not send {1} email to {0}",
                    to,
                    type
                ),
                e
            );
        }
    }

    /**
     * Creates context and send reset password email to user
     *
     * @param user User that requested his password to be reset
     * @param token The reset token to send to the user
     */
    public void sendResetPassword(AppUser user, ResetPasswordToken token) {
        String src = MessageFormat.format(applicationProperties.getResetPassword().getUrl(), token.getId());
        String subject = applicationProperties.getResetPassword().getSubject();
        Context context = new Context();
        context.setVariable("link", src);
        context.setVariable("subject", subject);
        sendTemplateMessage(user.getUsername(), applicationProperties.getResetPassword().getFrom(), subject, context, EmailType.RESET_PASSWORD);
    }

    /**
     * Creates context and send "validation email" email to the user
     *
     * @param user User that is newly created
     * @param token The email validation token to send to the user
     */
    public void sendValidationEmail(AppUser user, ValidateEmailToken token) {
        String src = MessageFormat.format(applicationProperties.getValidationMail().getUrl(), token.getId());
        String subject = applicationProperties.getValidationMail().getSubject();
        Context context = new Context();
        context.setVariable("link", src);
        context.setVariable("subject", subject);
        sendTemplateMessage(user.getUsername(), applicationProperties.getValidationMail().getFrom(), subject, context, EmailType.VALIDATION_EMAIL);
    }
}
