package com.ttn.linksharing.service;

import com.ttn.linksharing.entity.Subscription;
import com.ttn.linksharing.entity.User;
import com.ttn.linksharing.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService {
    @Autowired
    SubscriptionRepository subscriptionRepository;

    public int subscriptionsCount(User user){
        return subscriptionRepository.countAllByUser(user);
    }

    public List<Subscription> subscriptionsPerUser(User user){
        return subscriptionRepository.findAllByUser(user);
    }
}
