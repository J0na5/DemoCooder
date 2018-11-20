package com.processbyte.landing.newsletter;

import com.processbyte.landing.emailValidation.ValidEmail;

import javax.validation.constraints.NotEmpty;

public class SubscriberForm {

    @ValidEmail
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
