package com.ttn.linksharing.controller;

import com.ttn.linksharing.entity.*;
import com.ttn.linksharing.enums.Seriousness;
import com.ttn.linksharing.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    @Autowired
    TopicService topicService;

    @Autowired
    UserService userService;

    @Autowired
    SubscriptionService subscriptionService;

    @Autowired
    LinkResourceService linkResourceService;

    @Autowired
    DocumentResourceService documentResourceService;

    @RequestMapping("dashboard")
    public ModelAndView dashboard(ModelMap model, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("Dashboard");
        modelAndView.addObject("topic", new Topic());
        modelAndView.addObject("subscription", new Subscription());
        modelAndView.addObject("linkResource", new LinkResource());
        Integer userId = (Integer) session.getAttribute("loggedInUser");
        if (userId != null) {
            User user = userService.findById(userId);
            modelAndView.addObject("user", user);
            String name = user.getFirstName() + " " + user.getLastName();
            model.addAttribute("name", name);
            int countSub = subscriptionService.subscriptionsCount(user);
            model.addAttribute("countSub", countSub);
            int countTop = topicService.topicsCount(user);
            model.addAttribute("countTop", countTop);
            List<Subscription> subscriptionList = subscriptionService.subscriptionsPerUser(user);
            List<Topic> topics = subscriptionList.stream().map(e->e.getTopic()).collect(Collectors.toList());
            List<Seriousness> seriousness = subscriptionList.stream().map(Subscription::getSeriousness).collect(Collectors.toList());
            model.addAttribute("topics", topics);
            model.addAttribute("seriousness", seriousness);
            model.addAttribute("subscriptions", subscriptionList);

            return modelAndView;
        }
        return modelAndView;
    }

    @RequestMapping(value = "/dashboard/createTopic", method = RequestMethod.POST)
    public String createTopic(@ModelAttribute("topic") Topic topic, ModelMap model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("loggedInUser");
        if (userId != null) {
            User user = userService.findById(userId);
            topic.setUser(user);
            ModelAndView modelAndView = new ModelAndView("Dashboard");
            topicService.createTopic(topic);
            model.addAttribute("topic", topic);
            System.out.println(topic);
            return "redirect:/dashboard";
        }
        return "redirect:/dashboard";
    }

    @RequestMapping(value = "/dashboard/shareLink", method = RequestMethod.POST)
    public  String shareLink(@ModelAttribute("linkResource") LinkResource resource,@ModelAttribute("subscription") Subscription subscription, ModelMap model, HttpSession session){
        Integer userId = (Integer) session.getAttribute("loggedInUser");
        if (userId != null) {
            User user = userService.findById(userId);
            resource.setUser(user);
            ModelAndView modelAndView = new ModelAndView("Dashboard");
            linkResourceService.shareLink(resource);
            model.addAttribute("linkResource", resource);
            return "redirect:/dashboard";
        }
        return "redirect:/dashboard";
    }

    @RequestMapping(value = "/dashboard/shareDocument", method = RequestMethod.POST)
    public  String shareDocument(@ModelAttribute("documentResource") DocumentResource resource, ModelMap model, HttpSession session){
        Integer userId = (Integer) session.getAttribute("loggedInUser");
        if (userId != null) {
            User user = userService.findById(userId);
            resource.setUser(user);
            ModelAndView modelAndView = new ModelAndView("Dashboard");
            documentResourceService.shareDocument(resource);
            model.addAttribute("documentResource", resource);
            return "redirect:/dashboard";
        }
        return "redirect:/dashboard";
    }

}
