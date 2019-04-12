package com.ttn.linksharing.controller;

import com.ttn.linksharing.entity.Resource;
import com.ttn.linksharing.entity.Subscription;
import com.ttn.linksharing.entity.Topic;
import com.ttn.linksharing.enums.Seriousness;
import com.ttn.linksharing.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SubscriptionController {

    @Autowired
    SubscriptionService subscriptionService;

    @Autowired
    TopicService topicService;

    @Autowired
    ResourceService resourceService;

    @Autowired
    ResourceRatingService resourceRatingService;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/updateSeriousness", method = RequestMethod.POST)
    @ResponseBody
    public Map updateSeriousness(@RequestParam int subscriptionId, @RequestParam Seriousness seriousness, HttpSession session) throws Exception {
        Integer userId = (Integer) session.getAttribute("loggedInUser");
        Map<String, String> map = new HashMap<>();
        if (userId != null) {
            Subscription subscription = subscriptionService.findByid(subscriptionId);
            subscription.setSeriousness(seriousness);
            subscriptionService.saveSubscription(subscription);
            map.put("SUCCESS", "Seriousness UpdatedSuccessfully");
        } else {
            map.put("ERROR", "No Logged In User");
        }

        return map;
    }

    @RequestMapping(value = "/deleteSubscription", method = RequestMethod.POST)
    @ResponseBody
    public Map deleteSubscription(@RequestParam int topicId, HttpSession session) throws Exception {
        Integer userId = (Integer) session.getAttribute("loggedInUser");
        Map<String, String> map = new HashMap<>();
        if (userId != null) {
            Topic topic = topicService.findTopicById(topicId);
            List<Resource> resources = resourceService.findByTopic(topic);
            resourceRatingService.deleteRatingList(resources);
            resourceService.deleteResources(resources);
            subscriptionService.deleteSubscription(topic);
            topicService.deleteTopic(topic);

            map.put("SUCCESS", "Subscription Deleted Successfully");
        }
        map.put("ERROR", "No Logged In User");
        return map;
    }
}
