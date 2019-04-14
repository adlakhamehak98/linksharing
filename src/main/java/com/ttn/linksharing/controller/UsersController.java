package com.ttn.linksharing.controller;

import com.ttn.linksharing.entity.DocumentResource;
import com.ttn.linksharing.entity.LinkResource;
import com.ttn.linksharing.entity.Subscription;
import com.ttn.linksharing.entity.Topic;
import com.ttn.linksharing.entity.User;
import com.ttn.linksharing.service.TopicService;
import com.ttn.linksharing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class UsersController {

    @Autowired
    UserService userService;
    @Autowired
    TopicService topicService;

    @RequestMapping("users")
    public String users(ModelMap model, HttpSession session) {
        model.addAttribute("topic", new Topic());
        model.addAttribute("documentResource", new DocumentResource());
        model.addAttribute("linkResource", new LinkResource());
        Integer userId = (Integer) session.getAttribute("loggedInUser");
        User user = userId != null ? userService.findById(userId) : null;
        if (user != null && user.getIsAdmin()) {
            List<User> users = userService.findAllUsers();
            List<Topic> topics = topicService.findAll();
            model.addAttribute("topics", topics);
            model.addAttribute("users", users);
            model.addAttribute("user", user);
            return "Users";
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/updateUserStatus", method = RequestMethod.POST)
    @ResponseBody
    public Map updateUserStatus(@RequestParam Integer userId, @RequestParam boolean active, HttpSession session){
        Integer currentUserId = (Integer) session.getAttribute("loggedInUser");
        Map<String, String> result = new HashMap<>();
        User currentUser = currentUserId != null ? userService.findById(currentUserId) : null;
        User user = userId != null ? userService.findById(userId) : null;
        if (currentUser != null && user != null && currentUser.getIsAdmin()) {
            user.setIsActive(active);
            userService.saveUser(user);
            result.put("SUCCESS", active ? "User activated." : "User deactivated.");
        } else {
            result.put("ERROR","Unauthorized Action");
        }
        return result;
    }

}
