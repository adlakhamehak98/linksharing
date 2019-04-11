package com.ttn.linksharing.controller;

import com.ttn.linksharing.entity.*;
import com.ttn.linksharing.enums.Visibility;
import com.ttn.linksharing.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Autowired
    ResourceService resourceService;

    @Autowired
    ResourceRatingService resourceRatingService;

    @RequestMapping("dashboard")
    public ModelAndView dashboard(ModelMap model, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("Dashboard");
        modelAndView.addObject("topic", new Topic());
        modelAndView.addObject("subscription", new Subscription());
        modelAndView.addObject("linkResource", new LinkResource());
        modelAndView.addObject("resource", new Resource());
        modelAndView.addObject("documentResource", new DocumentResource());
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
            List<Topic> topics = subscriptionList.stream().map(e -> e.getTopic()).collect(Collectors.toList());
            model.addAttribute("topics", topics);
            model.addAttribute("subscriptions", subscriptionList);
            List<Topic> topics1 = resourceService.findTopicsWithMaxResourcesCount();
            model.addAttribute("trendingTopics", topics1);

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
    public String shareLink(@ModelAttribute("linkResource") LinkResource resource, @ModelAttribute("subscription") Subscription subscription, ModelMap model, HttpSession session) {
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

//    @RequestMapping(value = "/dashboard/sendInvitation", method = RequestMethod.POST)
//    public String createTopic( ModelMap model, HttpSession session) {
//        Integer userId = (Integer) session.getAttribute("loggedInUser");
//        if (userId != null) {
//            User user = userService.findById(userId);
//            ModelAndView modelAndView = new ModelAndView("Dashboard");
//            return "redirect:/dashboard";
//        }
//        return "redirect:/dashboard";
//    }
//
//    private void sendEmail (User user) throws Exception {
//        System.out.println("Herre>>>>>>>>>>>>>>>>>>>>>>");
//        MimeMessage message = sender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message);
//        helper.setTo(user.getEmail());
//        System.out.println(user.getEmail());
//        System.out.println("Herre>>>>>>>>>>>>>>>>>>>>>>");
//
//        helper.setText("Hello your password is: " + user.getPassword());
//        helper.setSubject("Please Find your password here");
//        sender.send(message);
//    }

    @RequestMapping(value = "/dashboard/shareDocument", method = RequestMethod.POST)
    public String shareDocument(@RequestParam MultipartFile file, @ModelAttribute("documentResource") DocumentResource resource, ModelMap model, BindingResult bindingResult, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("loggedInUser");
        if (userId != null) {
            if (file == null || file.isEmpty()) {
                bindingResult.addError(new FieldError("documentResource", "path", "File can't be null."));
                if (bindingResult.hasErrors()) {
                    return "redirect:/dashboard";
                }
            } else {
                User user = userService.findById(userId);
                resource.setUser(user);
                String filePath = documentResourceService.storeDocument(file);
                System.out.println(">>>>>>>>>>>filePath" + filePath);
                if (filePath != null) {
                    resource.setPath(filePath);
                    ModelAndView modelAndView = new ModelAndView("Dashboard");
                    documentResourceService.shareDocument(resource);
                    model.addAttribute("documentResource", resource);
                    return "redirect:/dashboard";
                } else {
                    bindingResult.addError(new FieldError("documentResource", "path", "File can't be uploaded."));
                    return "redirect:/dashboard";
                }
            }
        }
        return "redirect:/dashboard";
    }

    @RequestMapping(value = "/updateVisibility", method = RequestMethod.POST)
    @ResponseBody
    public Map updateVisibility(@RequestParam int topicId, @RequestParam Visibility visibility, HttpSession session) throws Exception {
        Integer userId = (Integer) session.getAttribute("loggedInUser");
        Map<String, String> map = new HashMap<>();
        if (userId != null) {
            Topic topic = topicService.findTopicById(topicId);
            topic.setVisibility(visibility);
            topicService.createTopic(topic);
            map.put("SUCCESS", "Visibility Updated Successfully");
        }
        map.put("ERROR", "No Logged In User");
        return map;
    }

    @RequestMapping(value = "/updateTopicName", method = RequestMethod.POST)
    @ResponseBody
    public Map updateTopicName(@RequestParam int topicId, @RequestParam String topicName, HttpSession session) throws Exception {
        Integer userId = (Integer) session.getAttribute("loggedInUser");
        Map<String, String> map = new HashMap<>();
        if (userId != null) {
            Topic topic = topicService.findTopicById(topicId);
            topic.setName(topicName);
            topicService.createTopic(topic);
            map.put("SUCCESS", "Topic Name Updated Successfully");
        }
        map.put("ERROR", "No Logged In User");
        return map;
    }

    @RequestMapping(value = "/deleteSubscription", method = RequestMethod.POST)
    @ResponseBody
    public Map deleteSubscription(@RequestParam int topicId, HttpSession session) throws Exception {
        Integer userId = (Integer) session.getAttribute("loggedInUser");
        Map<String, String> map = new HashMap<>();
        if (userId != null) {
            Topic topic = topicService.findTopicById(topicId);
            List<Resource> resources = resourceService.findByTopic(topic);
            resourceRatingService.deleteRatingList(resources);
            resourceService.deleteResources(resources);
            subscriptionService.deleteSubscription(topic);
            topicService.deleteTopic(topic);

            map.put("SUCCESS", "Subscription Deleted Successfully");
        }
        map.put("ERROR", "No Logged In User");
        return map;
    }
}
