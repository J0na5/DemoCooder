package com.processbyte.landing.newsletter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Locale;

@Controller
public class SubscriberController {

    @Autowired
    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @RequestMapping(value = "/confirm-newsletter-subscription", method = RequestMethod.GET)
    public String confirmSubscription(@RequestParam(value = "email") String email, @RequestParam("token") String token, Model model) throws InvalidDataException {
        subscriberService.confirmSubscription(email, token);
        model.addAttribute("language", currentLocale.getLanguage());
        return "subscription/confirmation-success";
    }

    @RequestMapping(value = "/unsubscribe-newsletter", method = RequestMethod.GET)
    public String unsubscribe(@RequestParam("email") String email, @RequestParam("token") String token, Model model) throws InvalidDataException{
        subscriberService.unsubscribe(email, token);
        model.addAttribute("language", currentLocale.getLanguage());
        return "subscription/unsubscribe-success";
    }

    @ExceptionHandler(InvalidDataException.class)
    public String handleInvalidDataException(Model model) {
        model.addAttribute("language", currentLocale.getLanguage());
        return "subscription/error";
    }

    private SubscriberService subscriberService;
    private final Locale currentLocale = LocaleContextHolder.getLocale();
}
