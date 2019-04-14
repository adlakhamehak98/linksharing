package com.ttn.linksharing.controller;

import com.ttn.linksharing.entity.Topic;
import com.ttn.linksharing.entity.User;
import com.ttn.linksharing.service.ResourceService;
import com.ttn.linksharing.service.SubscriptionService;
import com.ttn.linksharing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SearchController {

    @Autowired
    UserService userService;

    @Autowired
    ResourceService resourceService;

    @Autowired
    SubscriptionService subscriptionService;

    @RequestMapping("/search")
    public String search(Model model, HttpSession session) {
        model.addAttribute("topic", new Topic());
        Integer userId = (Integer) session.getAttribute("loggedInUser");
        List<Topic> topics1 = resourceService.findTopicsWithMaxResourcesCount();
        User currentUser = userId != null ? userService.findById(userId) : null;
        if (currentUser != null) {
            User user = userService.findById(userId);
            model.addAttribute("user", user);
            model.addAttribute("trendingTopics", topics1.stream()
                    .peek(topic -> topic.setCurrentUserSubscription(subscriptionService.findByUserAndTopic(user, topic)))
                    .collect(Collectors.toList()));
            return "Search";
        } else
            model.addAttribute("trendingTopics", topics1);
        return "Search";
    }
}
