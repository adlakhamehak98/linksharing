package com.ttn.linksharing.controller;

import com.ttn.linksharing.entity.Topic;
import com.ttn.linksharing.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    TopicService topicService;


    @RequestMapping("dashboard")
    public ModelAndView dashboard() {
        ModelAndView modelAndView = new ModelAndView("Dashboard");
        return modelAndView;
    }

    @RequestMapping("/dashboard/createTopic")
    public String createTopic(Model model){
        model.addAttribute("topic", new Topic() );
        topicService.createTopic();
        return "redirect:/dashboard";
    }

}
