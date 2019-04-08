package com.ttn.linksharing.controller;

import com.ttn.linksharing.entity.Topic;
import com.ttn.linksharing.entity.User;
import com.ttn.linksharing.service.TopicService;
import com.ttn.linksharing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    TopicService topicService;

    @Autowired
    UserService userService;

    @RequestMapping("dashboard")
    public ModelAndView dashboard() {
        ModelAndView modelAndView = new ModelAndView("Dashboard");
        modelAndView.addObject("topic", new Topic());
        return modelAndView;
    }

    @RequestMapping(value = "/dashboard/createTopic", method = RequestMethod.POST)
    public String createTopic(@ModelAttribute("topic") Topic topic, ModelMap model, HttpSession session){
        Integer userId = (Integer) session.getAttribute("loggedInUser");
        if(userId!=null)
        {User user = userService.findById(userId);
        topic.setUser(user);
        ModelAndView modelAndView = new ModelAndView("Dashboard");
        topicService.createTopic(topic);
        model.addAttribute("topic", topic);
        System.out.println(topic);
        return "redirect:/dashboard";}
        return "redirect:/dashboard";
    }

}
