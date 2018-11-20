package com.processbyte.landing.newsletter;

public interface SubscriberService {

    void addSubscriber(SubscriberForm subscriberForm) throws SubscriberExistsException;

    void confirmSubscription(String email, String confirmationToken) throws InvalidDataException;

    void unsubscribe(String email, String unsubscribeToken) throws InvalidDataException;
}
