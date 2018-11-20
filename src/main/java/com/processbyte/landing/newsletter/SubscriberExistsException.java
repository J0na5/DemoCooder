package com.processbyte.landing.newsletter;

public class SubscriberExistsException extends Exception {

    public SubscriberExistsException(String message) {
        super(message);
    }
}
