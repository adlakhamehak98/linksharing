package com.ttn.linksharing.bootstrap;

import com.ttn.linksharing.entity.Resource;
import com.ttn.linksharing.entity.Subscription;
import com.ttn.linksharing.entity.Topic;
import com.ttn.linksharing.entity.User;
import com.ttn.linksharing.enums.Seriousness;
import com.ttn.linksharing.enums.Visibility;
import com.ttn.linksharing.repository.SubscriptionRepository;
import com.ttn.linksharing.repository.TopicRepository;
import com.ttn.linksharing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class Bootstrap {
    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TopicRepository topicRepository;

    @EventListener(ApplicationStartedEvent.class)
    public void init() {
        Iterator<Subscription> iterable = subscriptionRepository.findAll().iterator();
        if (!iterable.hasNext()) {
            User user1 = new User("Kirti", "Chauhan", "kirti.chauhan@tothenew.com", "kirtich", "Kirti1997@", "profilepic.png");
            userRepository.save(user1);
            Topic topic1 = new Topic("Grails", user1, Visibility.PRIVATE);
            topicRepository.save(topic1);
            Subscription subscription = new Subscription(user1, topic1, Seriousness.CASUAL);

            subscriptionRepository.save(subscription);
        }
        System.out.println("Your Application is up and running");
    }
}
