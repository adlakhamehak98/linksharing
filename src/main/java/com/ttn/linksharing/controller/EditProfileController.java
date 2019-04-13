package com.ttn.linksharing.controller;

import com.ttn.linksharing.entity.User;
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

@Controller
public class EditProfileController {

    @Autowired
    UserService userService;

    List<User> users = new ArrayList<>();

    @RequestMapping("/dashboard/editProfile")
    public String editProfile(Model model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("loggedInUser");
        if (userId != null) {
            User user = userService.findById(userId);
            model.addAttribute("user", user);
            return "EditProfile";
        } else
            return "redirect:/";
    }

    @RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
    public String submit(@RequestParam MultipartFile file, @Valid @ModelAttribute("user") User responseData, ModelMap model, BindingResult bindingResult) {
            ModelAndView modelAndView = new ModelAndView("EditProfile");
            users.add(responseData);
            userService.saveUser(responseData);
            model.addAttribute("user", responseData);
            System.out.println(users);
            User userCheck = userService.checkUser(responseData.getUsername());
            userService.storeProfilePic(file);
            if ((userCheck != null)) {
                userService.saveUser(responseData);
                return "/dashboard/editProfile";
            } else {
                bindingResult.addError(new FieldError("user", "username", "Duplicate Username not allowed."));
                if (bindingResult.hasErrors()) {
                    return "/dashboard/editProfile";
                }
                return "/dashboard/editProfile";
            }
        }

}
