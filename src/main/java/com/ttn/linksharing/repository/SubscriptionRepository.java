package com.ttn.linksharing.repository;

import com.ttn.linksharing.entity.Subscription;
import org.springframework.data.repository.CrudRepository;

public interface SubscriptionRepository extends CrudRepository<Subscription, Integer> {
}
