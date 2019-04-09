package com.ttn.linksharing.repository;

import com.ttn.linksharing.entity.Subscription;
import com.ttn.linksharing.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface SubscriptionRepository extends CrudRepository<Subscription, Integer> {
    int countAllByUser(User user);
    Subscription findAllByUser(User user);
}
