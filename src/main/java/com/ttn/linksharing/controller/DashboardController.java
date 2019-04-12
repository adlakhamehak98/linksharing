package com.ttn.linksharing.controller;

import com.ttn.linksharing.entity.*;
import com.ttn.linksharing.enums.Seriousness;
import com.ttn.linksharing.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
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

    @Autowired
    ResourceService resourceService;

    @Autowired
    ResourceRatingService resourceRatingService;

    @Autowired
    ReadingItemService readingItemService;

    @Autowired
    private JavaMailSender sender;

    @RequestMapping("dashboard")
    public String dashboard(ModelMap model, HttpSession session) {
        model.addAttribute("topic", new Topic());
        model.addAttribute("subscription", new Subscription());
        model.addAttribute("linkResource", new LinkResource());
        model.addAttribute("resource", new Resource());
        model.addAttribute("readItem", new ReadingItem());
        model.addAttribute("documentResource", new DocumentResource());
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
            List<Topic> topics1 = resourceService.findTopicsWithMaxResourcesCount();
            model.addAttribute("trendingTopics", topics1);
            List<ReadingItem> readingItemList = readingItemService.findUnreadResourcesPerUser(false, user);
            model.addAttribute("readingItems", readingItemList);

            return "Dashboard";
        }
        return "redirect:/";
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

    @RequestMapping(value = "/dashboard/sendInvitation", method = RequestMethod.POST)
    public String createTopic(@RequestParam Integer topicId, @RequestParam String email, HttpSession session) throws Exception {
        Integer userId = (Integer) session.getAttribute("loggedInUser");

        User user = userId != null ? userService.findById(userId) : null;
        Topic topic = topicService.findTopicById(topicId);
        sendEmail(email, topic, user);

        return "redirect:/dashboard";
    }

    private void sendEmail(String email, Topic topic, User user) throws Exception {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(email);
        String url = "http://localhost:8080/dashboard/topicSubscription/" + topic.getId();
        helper.setText("Hello, you have received an invitation" + (user != null ? " from " + user.getFirstName() : "") + " for the topic" + topic.getName() + "." +
                " Click on the url to subscribe to the topic, " + url + ".");
        helper.setSubject("A New Invitation" + (user != null ? " from " + user.getFirstName() : "") + ".");
        sender.send(message);
    }

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

    @RequestMapping(value = "/dashboard/topicSubscription/{id}", method = RequestMethod.GET)
    public String subscribeTopic(@PathVariable Integer id, HttpSession session) {
        Topic topic = topicService.findTopicById(id);
        Integer userId = (Integer) session.getAttribute("loggedInUser");
        User user = userId != null ? userService.findById(userId) : null;
        if (topic != null && user != null) {
            Subscription subscription = subscriptionService.findByUserAndTopic(user, topic);
            if (subscription == null) {
                subscriptionService.saveSubscription(new Subscription(user, topic, Seriousness.CASUAL));
            }
            return "redirect:/topic/" + topic.getId();
        } else {
            return "redirect:/";
        }
    }
}
