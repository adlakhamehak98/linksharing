package com.ttn.linksharing.bootstrap;

import com.ttn.linksharing.entity.*;
import com.ttn.linksharing.enums.Seriousness;
import com.ttn.linksharing.enums.Visibility;
import com.ttn.linksharing.repository.*;
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

    @Autowired
    ResourceRatingRepository resourceRatingRepository;

    @Autowired
    ReadingItemRepository readingItemRepository;

    @EventListener(ApplicationStartedEvent.class)
    public void init() {
        Long userCount = userRepository.count();
        if (userCount == 0) {
            addUsers();
            addTopics();
            addSubscription();
            addResource();
            addResourceRating();
            addReadingItem();
        }

        System.out.println("Your Application is up and running");
    }

    private void addUsers() {
        userRepository.save(new User("User1", "Dummy", "mehak.adlakha@tothenew.com", "user1", "Abc123@", "user1.png"));
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
                topics.forEach(topic -> resourceRepository.save(new DocumentResource(user, topic, "This is abc.txt.", resourceRatings, "/home/abc.txt")));
            else
                topics.forEach(topic -> resourceRepository.save(new LinkResource(user, topic, "Study java here.", resourceRatings, "https://www.javatpoint.com/java-tutorial")));
        }
    }

    private void addResourceRating() {
        User user1 = userRepository.findById(1);
        User user2 = userRepository.findById(2);
        User user3 = userRepository.findById(3);
        Resource resource1 = resourceRepository.findById(5).orElse(null);
        Resource resource2 = resourceRepository.findById(10).orElse(null);
        Resource resource3 = resourceRepository.findById(8).orElse(null);
        Resource resource4 = resourceRepository.findById(20).orElse(null);
        Resource resource5 = resourceRepository.findById(12).orElse(null);
        Resource resource6 = resourceRepository.findById(6).orElse(null);
        Resource resource7 = resourceRepository.findById(30).orElse(null);
        Resource resource8 = resourceRepository.findById(41).orElse(null);
        Resource resource9 = resourceRepository.findById(55).orElse(null);
        if (resource1 != null) {
            resourceRatingRepository.save(new ResourceRating(user1, resource1, 4));
            resourceRatingRepository.save(new ResourceRating(user2, resource1, 3));
        }
        if (resource2 != null) {
            resourceRatingRepository.save(new ResourceRating(user1, resource2, 4));
            resourceRatingRepository.save(new ResourceRating(user2, resource2, 3));
        }
        if (resource3 != null) {
            resourceRatingRepository.save(new ResourceRating(user1, resource3, 4));
            resourceRatingRepository.save(new ResourceRating(user2, resource3, 3));
        }
        if (resource4 != null) {
            resourceRatingRepository.save(new ResourceRating(user3, resource4, 5));
            resourceRatingRepository.save(new ResourceRating(user2, resource4, 4));
        }
        if (resource5 != null) {
            resourceRatingRepository.save(new ResourceRating(user1, resource5, 2));
            resourceRatingRepository.save(new ResourceRating(user2, resource5, 3));
        }
        if (resource6 != null) {
            resourceRatingRepository.save(new ResourceRating(user3, resource6, 2));
            resourceRatingRepository.save(new ResourceRating(user2, resource6, 1));
        }
        if (resource7 != null) {
            resourceRatingRepository.save(new ResourceRating(user1, resource7, 3));
            resourceRatingRepository.save(new ResourceRating(user2, resource7, 3));
        }
        if (resource8 != null) {
            resourceRatingRepository.save(new ResourceRating(user1, resource8, 1));
            resourceRatingRepository.save(new ResourceRating(user3, resource8, 2));
        }
        if (resource9 != null) {
            resourceRatingRepository.save(new ResourceRating(user3, resource9, 5));
            resourceRatingRepository.save(new ResourceRating(user2, resource9, 5));
        }
    }

    private void addReadingItem() {
        Iterator<User> users = userRepository.findAll().iterator();
        List<Resource> resources = resourceRepository.findAll();
        while (users.hasNext()) {
            User user = users.next();
            resources.forEach(resource -> readingItemRepository.save(new ReadingItem(user, resource)));
        }
    }
}