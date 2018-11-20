package com.processbyte.landing.newsletter;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.util.Date;

@Document
public class Subscriber {

    @Id
    private BigInteger id;
    private String email;
    private Boolean confirmed;
    private String confirmationToken;
    private String unsubscribeToken;
    private Date subscriptionDate;

    public Subscriber(String email, String confirmationToken, String unsubscribeToken) {
        this.email = email;
        this.confirmed = false;
        this.confirmationToken = confirmationToken;
        this.unsubscribeToken = unsubscribeToken;
        this.subscriptionDate = new Date();
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    public String getUnsubscribeToken() {
        return unsubscribeToken;
    }

    public void setUnsubscribeToken(String unsubscribeToken) {
        this.unsubscribeToken = unsubscribeToken;
    }

    public Date getSubscriptionDate() {
        return subscriptionDate;
    }

    public void setSubscriptionDate(Date subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }
}
