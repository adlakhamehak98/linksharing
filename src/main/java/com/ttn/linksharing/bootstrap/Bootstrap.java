package com.ttn.linksharing.bootstrap;

import com.ttn.linksharing.entity.*;
import com.ttn.linksharing.enums.Seriousness;
import com.ttn.linksharing.enums.Visibility;
import com.ttn.linksharing.repository.ResourceRepository;
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

    @Autowired
    ResourceRepository resourceRepository;

    @EventListener(ApplicationStartedEvent.class)
    public void init() {
        Long userCount = userRepository.count();
        if (userCount == 0) {
            addUsers();
            addTopics();
            addSubscription();
            addResource();
        }
        System.out.println("Your Application is up and running");
    }

    private void addUsers() {
        userRepository.save(new User("User1", "Dummy", "User1.dummy@tothenew.com", "user1", "Abc123@", "user1.png"));
        userRepository.save(new User("User2", "Dummy", "User2.dummy@tothenew.com", "user2", "Abc123@", "user2.png"));
        userRepository.save(new User("User3", "Dummy", "User3.dummy@tothenew.com", "user3", "Abc123@", "user3.png"));
        userRepository.save(new User("User4", "Dummy", "User4.dummy@tothenew.com", "user4", "Abc123@", "user4.png"));
        userRepository.save(new User("User5", "Dummy", "User5.dummy@tothenew.com", "user5", "Abc123@", "user5.png"));
    }

    private void addTopics() {
        Iterator<User> users = userRepository.findAll().iterator();
        while (users.hasNext()) {
            User user = users.next();
            topicRepository.save(new Topic(user.getFirstName() + " Topic1", user, Visibility.PRIVATE));
            topicRepository.save(new Topic(user.getFirstName() + " Topic2", user, Visibility.PRIVATE));
            topicRepository.save(new Topic(user.getFirstName() + " Topic3", user, Visibility.PUBLIC));
        }
    }

    private void addSubscription() {
        Iterator<User> users = userRepository.findAll().iterator();
        List<Topic> topics = topicRepository.findAll();
        while (users.hasNext()) {
            User user = users.next();
            topics.forEach(topic -> {
                if (topic.getUser().getId().equals(user.getId())) {
                    subscriptionRepository.save(new Subscription(user, topic, Seriousness.VERY_SERIOUS));
                } else {
                    subscriptionRepository.save(new Subscription(user, topic, Seriousness.SERIOUS));
                }
            });
        }
    }

    private void addResource() {
        Iterator<User> users = userRepository.findAll().iterator();
        List<Topic> topics = topicRepository.findAll();
        List<ResourceRating> resourceRatings = new ArrayList<>();
        while (users.hasNext()) {
            User user = users.next();
            int id = user.getId();
            if (id < 3)
                topics.forEach(topic -> resourceRepository.save(new DocumentResource(user, topic, resourceRatings, "/home/abc.txt")));
            else
                topics.forEach(topic -> resourceRepository.save(new LinkResource(user, topic, resourceRatings, "https://www.javatpoint.com/java-tutorial")));
        }
    }
}