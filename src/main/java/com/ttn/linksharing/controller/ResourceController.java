package com.ttn.linksharing.controller;

import com.ttn.linksharing.entity.*;
import com.ttn.linksharing.enums.Visibility;
import com.ttn.linksharing.service.ResourceService;
import com.ttn.linksharing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
public class ResourceController {

    @Autowired
    ResourceService resourceService;
    @Autowired
    UserService userService;

    @RequestMapping(value = "resource/{id}", method = RequestMethod.GET)
    public String home(@PathVariable Integer id, Model model, HttpSession session) {
        Resource resource = resourceService.findById(id);
        Topic topic = resource.getTopic();
        Integer userId = (Integer) session.getAttribute("loggedInUser");
        User user = userId != null ? userService.findById(userId) : null;
        model.addAttribute("linkResource", new LinkResource());
        model.addAttribute("documentResource", new DocumentResource());
        if (topic != null) {
            if (user != null) {
                model.addAttribute("user", user);
                model.addAttribute("subscription",
                        topic.getSubscriptions().stream()
                                .filter(subscription -> subscription.getUser().getId().equals(user.getId()))
                                .findFirst()
                                .orElse(null));
            }
            if (topic.getVisibility() == Visibility.PUBLIC) {
                model.addAttribute("resource", resource);
                model.addAttribute("resourceType", resource instanceof LinkResource ? "LinkResource" : "DocumentResource");
                return "Post";
            }
            if (topic.getVisibility() == Visibility.PRIVATE && user != null
                    && topic.getSubscriptions().stream().map(Subscription::getUser).anyMatch(u -> u.getId().equals(user.getId()))) {
                model.addAttribute("resource", resource);
                model.addAttribute("resourceType", resource instanceof LinkResource ? "LinkResource" : "DocumentResource");
                return "Post";
            } else {
                return "redirect:/";
            }
        }
        return "404";
    }

}
