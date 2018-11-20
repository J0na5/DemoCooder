package com.processbyte.landing.emailSender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;

@Component
public class EmailSender {

    @Autowired
    public EmailSender(JavaMailSender javaMailSender, TemplateEngine templateEngine, Environment environment) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.environment = environment;
    }

    /**
     * Send an email as plain text
     *
     * @param to String, the email address of the receiver
     * @param subject String, the subject of the email
     * @param text String, the content of the email
     *
     * @return EmailStatus, a status object that contains either a success or an error message
     */
    public EmailStatus sendPlainText(String to, String subject, String text) {
        return sendMail(to, subject, text, false);
    }

    /**
     * Send an email in HTML form
     *
     * @param to String, the email address of the receiver
     * @param subject String, the subject of the email
     * @param templateName String, the path of the template that is used relative to the templates folder
     * @param context Context, a context object containing the necessary variables and locale
     *
     * @return EmailStatus, a status object that contains either a success or an error message
     */
    public EmailStatus sendHtml(String to, String subject, String templateName, Context context) {
        String body = templateEngine.process(templateName, context);
        return sendMail(to, subject, body, true);
    }

    /**
     * Passes the email to the mail server as the noreply user
     *
     * @param to String, the email address of the receiver
     * @param subject String, the subject of the email
     * @param text String, the content of the email
     * @param isHtml Boolean, true if the email contains HTML and vice versa
     *
     * @return EmailStatus, a status object that contains either a success or an error message
     */
    private EmailStatus sendMail(String to, String subject, String text, Boolean isHtml) {
        try {
            MimeMessage mail = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(to);
            helper.setFrom("noreply@processbyte.com", "Processbyte No Reply");
            helper.setSubject(subject);
            helper.setText(text, isHtml);
            javaMailSender.send(mail);
            return new EmailStatus(to, subject, text).success();
        } catch (Exception e) {
            LOGGER.error(String.format("Problem with sending emailSender to: {}, error message: {}", to, e.getMessage()));

            if(environment.getActiveProfiles()[0].equals("development"))
                e.printStackTrace();

            return new EmailStatus(to, subject, text).error(e.getMessage());
        }
    }

    private JavaMailSender javaMailSender;
    private TemplateEngine templateEngine;
    private Environment environment;
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSender.class);
}
