package com.processbyte.landing.newsletter;

import org.springframework.data.repository.CrudRepository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public interface SubscriberRepo extends CrudRepository<Subscriber, BigInteger> {

    Subscriber findByEmail(String email);

    List<Subscriber> findAllByConfirmedFalseAndSubscriptionDateBefore(Date timeThreshold);
}
