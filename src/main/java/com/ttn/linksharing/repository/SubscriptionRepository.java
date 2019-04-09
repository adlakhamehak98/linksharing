package com.ttn.linksharing.repository;

import com.ttn.linksharing.entity.Subscription;
import com.ttn.linksharing.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SubscriptionRepository extends CrudRepository<Subscription, Integer> {
    int countAllByUser(User user);
    List<Subscription> findAllByUser(User user);
}