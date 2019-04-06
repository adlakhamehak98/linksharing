package com.ttn.linksharing.controller;

import com.ttn.linksharing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
    @Autowired
    UserService userService;

    @RequestMapping("/")
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("Home");
        userService.userDataSave();
        return modelAndView;
    }

    @RequestMapping("dashboard")
    public ModelAndView dashboard() {
        ModelAndView modelAndView = new ModelAndView("Dashboard");
        return modelAndView;
    }

    @RequestMapping("topic")
    public ModelAndView topic() {
        ModelAndView modelAndView = new ModelAndView("Topic");
        return modelAndView;
    }

    @RequestMapping("post")
    public ModelAndView post() {
        ModelAndView modelAndView = new ModelAndView("Post");
        return modelAndView;
    }

    @RequestMapping("search")
    public ModelAndView search() {
        ModelAndView modelAndView = new ModelAndView("Search");
        return modelAndView;
    }

    @RequestMapping("editProfile")
    public ModelAndView editProfile() {
        ModelAndView modelAndView = new ModelAndView("Edit Profile");
        return modelAndView;
    }
}
