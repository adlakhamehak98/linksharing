package com.ttn.linksharing.controller;

import com.ttn.linksharing.entity.User;
import com.ttn.linksharing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.List;

@Controller
public class UsersController {

    @Autowired
    UserService userService;

    @RequestMapping("users")
    public String users(ModelMap model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("loggedInUser");
        if (userId != null) {
            List<User> users = userService.findAllUsers();
            System.out.println(users);
            model.addAttribute("users", users);
            return "Users";
        }
        return "redirect:/";
    }

}
