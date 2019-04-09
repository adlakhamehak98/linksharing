package com.ttn.linksharing.controller;

import com.ttn.linksharing.entity.Subscription;
import com.ttn.linksharing.entity.Topic;
import com.ttn.linksharing.entity.User;
import com.ttn.linksharing.enums.Seriousness;
import com.ttn.linksharing.service.SubscriptionService;
import com.ttn.linksharing.service.TopicService;
import com.ttn.linksharing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class DashboardController {

    @Autowired
    TopicService topicService;

    @Autowired
    UserService userService;

    @Autowired
    SubscriptionService subscriptionService;

    @RequestMapping("dashboard")
    public ModelAndView dashboard(ModelMap model, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("Dashboard");
        modelAndView.addObject("topic", new Topic());
        modelAndView.addObject("subscription", new Subscription());
        Integer userId = (Integer) session.getAttribute("loggedInUser");
        if (userId != null) {
            User user = userService.findById(userId);
            modelAndView.addObject("user", user);
            String name = user.getFirstName() + " " + user.getLastName();
            model.addAttribute("name", name);
            int countSub = subscriptionService.subscriptionsCount(user);
            model.addAttribute("countSub", countSub);
            int countTop = topicService.topicsCount(user);
            model.addAttribute("countTop", countTop);
            Subscription subscription= subscriptionService.subscriptionsPerUser(user);
            String topicName= subscription.getTopic().getName();
            Seriousness seriousness = subscription.getSeriousness();
            model.addAttribute("topicName", topicName);
            model.addAttribute("seriousness", seriousness);

            return modelAndView;
        }
        return modelAndView;
    }

    @RequestMapping(value = "/dashboard/createTopic", method = RequestMethod.POST)
    public String createTopic(@ModelAttribute("topic") Topic topic, ModelMap model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("loggedInUser");
        if (userId != null) {
            User user = userService.findById(userId);
            topic.setUser(user);
            ModelAndView modelAndView = new ModelAndView("Dashboard");
            topicService.createTopic(topic);
            model.addAttribute("topic", topic);
            System.out.println(topic);
            return "redirect:/dashboard";
        }
        return "redirect:/dashboard";
    }


}
