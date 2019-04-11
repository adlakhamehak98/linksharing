package com.ttn.linksharing.controller;

import com.ttn.linksharing.entity.Resource;
import com.ttn.linksharing.entity.User;
import com.ttn.linksharing.repository.UserRepository;
import com.ttn.linksharing.service.ResourceService;
import com.ttn.linksharing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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

import javax.mail.internet.MimeMessage;
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

    @Autowired
    ResourceService resourceService;

    @Autowired
    private JavaMailSender sender;

    List<User> users = new ArrayList<>();

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model) {
        List<Resource> topResources = resourceService.fetchTopFivePublicResources();
        List<Resource> latestResources = resourceService.fetchLatestFivePublicResources();
        model.addAttribute("topResources", topResources);
        model.addAttribute("latestResources", latestResources);
        model.addAttribute("user", new User());
        return "Home";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String submit(@Valid @RequestParam MultipartFile file, @Valid @ModelAttribute("user") User responseData, ModelMap model, BindingResult bindingResult) {
        List<Resource> topResources = resourceService.fetchTopFivePublicResources();
        List<Resource> latestResources = resourceService.fetchLatestFivePublicResources();
        model.addAttribute("topResources", topResources);
        model.addAttribute("latestResources", latestResources);
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
            User userCheck = userService.checkUser(responseData.getUsername());
            userService.storeProfilePic(file);
            if ((userCheck != null)) {
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
        User loginStatus = userService.validateUser(responseData);
        if (loginStatus != null) {
            session.setAttribute("loggedInUser", loginStatus.getId());
            return new ModelAndView("redirect:/dashboard");
        } else {
            ModelAndView modelAndView = new ModelAndView("Home");
            List<Resource> topResources = resourceService.fetchTopFivePublicResources();
            List<Resource> latestResources = resourceService.fetchLatestFivePublicResources();
            modelAndView.addObject("topResources", topResources);
            modelAndView.addObject("latestResources", latestResources);
            modelAndView.addObject("error", "username or password Wrong");
            return modelAndView;

        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpSession session) {
        session.invalidate();
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public String resetPass(String email) throws Exception {
        User user = userService.findUserByEmail(email);
        if (user != null) {
            sendEmail(user);
            return "redirect:/";
        }
        else
            return "ForgetPassword";
        }


    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public ModelAndView reset() throws Exception {
             return new ModelAndView("ForgetPassword");
    }



    private void sendEmail (User user) throws Exception {
        System.out.println("Here>>>>>>>>>>>>>>>>>>>>>>");
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setTo(user.getEmail());
        System.out.println(user.getEmail());
        System.out.println("Here>>>>>>>>>>>>>>>>>>>>>>");

            helper.setText("Hello, your new password is: " + user.getPassword());
            helper.setSubject("Please Find your password here");
            sender.send(message);
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
