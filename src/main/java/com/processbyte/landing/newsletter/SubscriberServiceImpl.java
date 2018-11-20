package com.processbyte.landing.newsletter;

import com.processbyte.landing.emailSender.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import javax.validation.constraints.NotNull;
import java.util.Locale;
import java.util.UUID;

@Service
public class SubscriberServiceImpl implements SubscriberService { //todo: Unit & integration Tests, error handling when mail and db connections fail

    @Autowired
    public SubscriberServiceImpl(Environment environment, MessageSource messageSource, EmailSender emailSender, SubscriberRepo subscriberRepo) {
        this.environment = environment;
        this.messageSource = messageSource;
        this.emailSender = emailSender;
        this.subscriberRepo = subscriberRepo;
    }

    /**
     * Subscribes someone to the newsletter.
     * If the email address is already signed up but not confirmed a new confirmation email will be sent.
     * If the email address is already signed ip and confirmed a SubscriberExistsException will be thrown.
     *
     * @param subscriberForm SubscriberForm, the dto for the new newsletter subscriber
     *
     * @throws SubscriberExistsException throws exception if there already exists a confirmed subscriber with the same email address
     */
    @Override
    public void addSubscriber(@NotNull SubscriberForm subscriberForm) throws SubscriberExistsException {
        final String email = subscriberForm.getEmail();
        final Subscriber subscriber = subscriberRepo.findByEmail(email);

        if(subscriber == null)
            createSubscriber(email);
        else if(!subscriber.getConfirmed())
            sendConfirmationEmail(email, subscriber.getConfirmationToken());
        else
            throw new SubscriberExistsException("There already exists a subscriber with this token");
    }

    /**
     * Confirms the subscription to the newsletter
     *
     * @param email String, the email of the subscriber that is confirmed
     * @param confirmationToken String, the corresponding confirmation token
     *
     * @throws InvalidDataException, throws an InvalidDateException if the newsletter subscriber does not exists or if the confirmation token is invalid
     */
    @Override
    public void confirmSubscription(@NotNull String email, @NotNull String confirmationToken) throws InvalidDataException {
        final Subscriber subscriber = subscriberRepo.findByEmail(email);

        // Check if the subscriber exists
        if(subscriber == null)
            throw new InvalidDataException("Subscriber does not exist");

        // Check if the token is valid
        if(!subscriber.getConfirmationToken().equals(confirmationToken))
            throw new InvalidDataException("Invalid token");

        subscriber.setConfirmed(true);
        subscriberRepo.save(subscriber);
    }

    /**
     * Unsubscribe a newsletter subscriber from the newsletter
     *
     * @param email String, the email address of the subscriber that needs to be removed from the subscription list
     * @param unsubscribeToken String, the token to unsubscribe the subscriber from the newsletter
     *
     * @throws InvalidDataException, if the token does not match the email address an InvalidDataException will be thrown
     */
    @Override
    public void unsubscribe(@NotNull String email, @NotNull String unsubscribeToken) throws InvalidDataException {
        Subscriber subscriber = subscriberRepo.findByEmail(email);

        // Check if the subscriber exists
        if(subscriber == null)
            throw new InvalidDataException("Subscriber does not exist");

        // Check if the token is valid
        if(!subscriber.getUnsubscribeToken().equals(unsubscribeToken))
            throw new InvalidDataException("Invalid token");

        subscriberRepo.delete(subscriber);
    }

    /**
     * Create new subscriber with a given email address
     *
     * @param email String, the email address of the new subscriber
     */
    private void createSubscriber(@NotNull String email) {
        final String confirmationToken = UUID.randomUUID().toString();
        final String unsubscribeToken = UUID.randomUUID().toString();

        Subscriber subscriber = new Subscriber(email, confirmationToken, unsubscribeToken);
        subscriberRepo.save(subscriber);

        sendConfirmationEmail(email, confirmationToken);
    }

    /**
     * Sends an email to the new newsletter subscriber so he/she can confirm his/her subscription
     *
     * @param email String, the email address of th new subscriber
     * @param confirmationToken String, the random confirmation token
     */
    private void sendConfirmationEmail(@NotNull String email, @NotNull String confirmationToken) {
        Context context = new Context();
        Locale currentLocale = LocaleContextHolder.getLocale();
        String confirmationUrl = constructConfirmationUrl(currentLocale, email, confirmationToken);
        String subject = messageSource.getMessage("email.newsletter.title", null, currentLocale);

        context.setLocale(LocaleContextHolder.getLocale());
        context.setVariable("confirmationUrl", confirmationUrl);

        emailSender.sendHtml(email, subject, "subscription/confirmation-email", context);
    }

    /**
     * Constructs the confirmation url, based on the environment settings and locale
     *
     * @param locale Locale, the locale that the user selected
     * @param email String, the email address of the user
     * @param confirmationToken String, the random confirmation token
     *
     * @return String, the complete URL that confirms the user's subscription to the newsletter
     */
    @NotNull
    private String constructConfirmationUrl(@NotNull Locale locale, @NotNull String email, @NotNull String confirmationToken) {
        String baseUrl;

        switch (locale.toString()) {
            case "nl_BE":
                baseUrl = environment.getProperty("processbyte.url.nlbe");
                break;
            case "en_UK":
                baseUrl = environment.getProperty("processbyte.url.engb");
                break;
            default:
                baseUrl = environment.getProperty("processbyte.url.enus");
                break;
        }

        return baseUrl + "/confirm-newsletter-subscription?email=" + email + "&token=" + confirmationToken;
    }

    private Environment environment;
    private MessageSource messageSource;
    private EmailSender emailSender;
    private SubscriberRepo subscriberRepo;
}
