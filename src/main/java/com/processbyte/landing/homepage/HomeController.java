package com.processbyte.landing.homepage;

import com.processbyte.landing.newsletter.SubscriberExistsException;
import com.processbyte.landing.newsletter.SubscriberForm;
import com.processbyte.landing.newsletter.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Locale;

@Controller
public class HomeController {

    @Autowired
    public HomeController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView home() {
        SubscriberForm subscriberForm = new SubscriberForm();
        return homePage(subscriberForm, null);
    }

    @RequestMapping(value = "/subscribers", method = RequestMethod.POST)
    public ModelAndView addSubscriber(@ModelAttribute("subscriber") @Valid SubscriberForm subscriberForm, BindingResult bindingResult) {
        if(!bindingResult.hasErrors()) {
            try {
                subscriberService.addSubscriber(subscriberForm);
                return homePage(new SubscriberForm(), true);
            } catch (SubscriberExistsException e) {
                String[] codes = {"AlreadyExists.subscriber.email"};
                String[] args = {};
                FieldError error = new FieldError("subscriber", "email", subscriberForm.getEmail(), true, codes, args, "subscriber already exists");
                bindingResult.addError(error);
            }
        }
        return homePage(subscriberForm, false);
    }

    @NotNull
    private ModelAndView homePage(@NotNull SubscriberForm subscriberForm, Boolean subscriptionSuccess) {
        ModelAndView homepage = new ModelAndView("homepage");
        Locale currentLocale = LocaleContextHolder.getLocale();

        if(subscriptionSuccess != null)
            homepage.addObject("subscriptionSuccess", subscriptionSuccess);

        homepage.addObject("locale", currentLocale.toString());
        homepage.addObject("language", currentLocale.getLanguage());
        homepage.addObject("subscriber", subscriberForm);
        return homepage;
    }

    private SubscriberService subscriberService;
}
