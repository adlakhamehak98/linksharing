package com.ttn.linksharing.controller;

import com.ttn.linksharing.entity.User;
import com.ttn.linksharing.repository.UserRepository;
import com.ttn.linksharing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    List<User> users = new ArrayList<>();

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model) {
        model.addAttribute("user", new User());
        return "Home";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String submit(@ModelAttribute("user") User user, ModelMap model) {
        System.out.println("inside method");
        users.add(user);
        userRepository.save(user);
        model.addAttribute("user", user);
        System.out.println(users);
        return "RegistrationDataShow";
    }

//    @RequestMapping("dashboard")
//    public ModelAndView dashboard() {
//        ModelAndView modelAndView = new ModelAndView("Dashboard");
//        return modelAndView;
//    }
//
//    @RequestMapping("topic")
//    public ModelAndView topic() {
//        ModelAndView modelAndView = new ModelAndView("Topic");
//        return modelAndView;
//    }
//
//    @RequestMapping("post")
//    public ModelAndView post() {
//        ModelAndView modelAndView = new ModelAndView("Post");
//        return modelAndView;
//    }
//
//    @RequestMapping("search")
//    public ModelAndView search() {
//        ModelAndView modelAndView = new ModelAndView("Search");
//        return modelAndView;
//    }
//
//    @RequestMapping("editProfile")
//    public ModelAndView editProfile() {
//        ModelAndView modelAndView = new ModelAndView("Edit Profile");
//        return modelAndView;
//    }
}
