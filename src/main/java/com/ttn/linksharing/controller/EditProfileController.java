package com.ttn.linksharing.controller;

import com.ttn.linksharing.entity.Subscription;
import com.ttn.linksharing.entity.Topic;
import com.ttn.linksharing.entity.User;
import com.ttn.linksharing.service.SubscriptionService;
import com.ttn.linksharing.service.TopicService;
import com.ttn.linksharing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class EditProfileController {

    @Autowired
    UserService userService;

    @Autowired
    SubscriptionService subscriptionService;

    @Autowired
    TopicService topicService;

    @RequestMapping("/dashboard/editProfile")
    public String editProfile(Model model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("loggedInUser");
        if (userId != null) {
            User user = userService.findById(userId);
            model.addAttribute("user", user);
            String name = user.getFirstName() + " " + user.getLastName();
            model.addAttribute("name", name);
            int countSub = subscriptionService.subscriptionsCount(user);
            model.addAttribute("countSub", countSub);
            int countTop = topicService.topicsCount(user);
            model.addAttribute("countTop", countTop);
            List<Subscription> subscriptionList = subscriptionService.subscriptionsPerUser(user);
            List<Topic> topics = subscriptionList.stream().map(Subscription::getTopic).collect(Collectors.toList());
            model.addAttribute("topics", topics);
            model.addAttribute("subscriptions", subscriptionList);
            return "EditProfile";
        } else
            return "redirect:/";
    }


}
