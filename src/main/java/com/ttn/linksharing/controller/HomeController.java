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
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public ModelAndView submit(@ModelAttribute("user") User responseData, ModelMap model) {
        System.out.println("inside method");
        ModelAndView modelAndView = new ModelAndView("Home");
        users.add(responseData);
        userRepository.save(responseData);
        model.addAttribute("user", responseData);
        System.out.println(users);
        List<User> userCheck = userService.checkUser(responseData);
        if (!(userCheck.size() > 0)) {
            userService.saveUser(responseData);
            return new ModelAndView("redirect:/");
        } else {
            return modelAndView.addObject("error", "username already exists");
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView formSuccess(@ModelAttribute("login") User responseData) {
        ModelAndView modelAndView = new ModelAndView("Home");

        User loginStatus = userService.validateUser(responseData);

        if (loginStatus != null) {
                return new ModelAndView("redirect:dashboard");
        } else {

            ModelAndView modelAndView1=new ModelAndView("redirect:/login").addObject("error","Wrong Username Password");
            return new ModelAndView("Home").addObject("error", "username or password Wrong");

        }
       // return modelAndView;
    }

    @RequestMapping("dashboard")
    public ModelAndView dashboard() {
        ModelAndView modelAndView = new ModelAndView("Dashboard");
        return modelAndView;
    }

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
