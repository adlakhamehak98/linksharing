package com.ttn.linksharing.controller;

import com.ttn.linksharing.entity.User;
import com.ttn.linksharing.repository.UserRepository;
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

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String submit(@Valid @RequestParam MultipartFile file, @Valid @ModelAttribute("user") User responseData, ModelMap model, BindingResult bindingResult) {
        System.out.println("to register::::::::::::::::::");
        if (file == null || file.isEmpty()) {
            bindingResult.addError(new FieldError("user", "fileName", "File cant be null."));
            if (bindingResult.hasErrors()) {
                return "Home";
            }
        } else {
            ModelAndView modelAndView = new ModelAndView("Home");
            users.add(responseData);
            userRepository.save(responseData);
            model.addAttribute("user", responseData);
            System.out.println(users);
            List<User> userCheck = userService.checkUser(responseData);
            userService.storeProfilePic(file);
            if ((userCheck.size() > 0)) {
                userService.saveUser(responseData);
                return "Home";
            } else {
                bindingResult.addError(new FieldError("user", "username", "Duplicate Username not allowed."));
                if (bindingResult.hasErrors()) {
                    return "Home";
                }
                return "Home";
            }
        }
        return "Home";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView formSuccess(@ModelAttribute("user") User responseData, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("Home");

        User loginStatus = userService.validateUser(responseData);

        if (loginStatus != null) {
            session.setAttribute("loggedInUser", loginStatus.getId());
            return new ModelAndView("redirect:/dashboard");
        } else {

            ModelAndView modelAndView1 = new ModelAndView("redirect:/login").addObject("error", "Wrong Username Password");
            return new ModelAndView("Home").addObject("error", "username or password Wrong");

        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpSession session) {
        session.invalidate();
        return new ModelAndView("redirect:/");
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
