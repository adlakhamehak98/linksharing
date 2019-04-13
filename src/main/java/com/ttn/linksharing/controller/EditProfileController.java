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

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
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
            return "redirect:/dashboard";
    }

    @RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
    public String submit(@Valid @ModelAttribute User responseData, @RequestParam MultipartFile file, ModelMap model, BindingResult bindingResult, HttpSession session) throws Exception {
        Integer userId = (Integer) session.getAttribute("loggedInUser");
        if (userId != null && userId.equals(responseData.getId())) {
            User user = userService.findById(responseData.getId());
            user.setFirstName(responseData.getFirstName());
            user.setLastName(responseData.getLastName());
            user.setUsername(responseData.getUsername());
            User userCheck = userService.checkUser(responseData.getUsername());
            if ((userCheck != null && userCheck.getId().equals(responseData.getId()))) {
                String filename = userService.storeProfilePic(file);
                user.setFileName(filename != null ? filename : user.getFileName());
                userService.saveUser(user);
                model.addAttribute("user", user);
                return "redirect:/dashboard/editProfile";
            } else {
                bindingResult.addError(new FieldError("user", "username", "Duplicate Username not allowed."));
                if (bindingResult.hasErrors()) {
                    return "redirect:/dashboard/editProfile";
                }
            }
        }
        return "redirect:/dashboard";
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public String changePassword(ModelMap model, @RequestParam String password, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("loggedInUser");
        User user = userService.findById(userId);
        user.setPassword(password);
        userService.saveUser(user);
        model.addAttribute("user", user);
        return "redirect:/dashboard/editProfile";
    }
}
