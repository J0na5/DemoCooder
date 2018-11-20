package com.processbyte.landing.newsletter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ScheduledTasks {

    @Autowired
    public ScheduledTasks(SubscriberRepo subscriberRepo) {
        this.subscriberRepo = subscriberRepo;
    }

    /*
     * Runs every day in order to delete the unactivated subscribers' data from the DB,
     * if the subscriber didn't confirm their subscription within 5 days.
     */
    @Scheduled(fixedRate = DAY_IN_MS)
    public void cleanupSubscriptions() {
        Date threshold = new Date(System.currentTimeMillis() - (5 * DAY_IN_MS));
        List<Subscriber> unconfirmedSubscribers = subscriberRepo.findAllByConfirmedFalseAndSubscriptionDateBefore(threshold);
        subscriberRepo.deleteAll(unconfirmedSubscribers);
    }

    private SubscriberRepo subscriberRepo;
    private final static long DAY_IN_MS = 1000 * 60 * 60 * 24;
}
