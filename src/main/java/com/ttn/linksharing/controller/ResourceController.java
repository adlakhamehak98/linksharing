package com.ttn.linksharing.controller;

import com.ttn.linksharing.entity.*;
import com.ttn.linksharing.enums.Visibility;
import com.ttn.linksharing.service.ReadingItemService;
import com.ttn.linksharing.service.ResourceService;
import com.ttn.linksharing.service.SubscriptionService;
import com.ttn.linksharing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ResourceController {

    @Autowired
    ResourceService resourceService;
    @Autowired
    UserService userService;
    @Autowired
    ReadingItemService readingItemService;
    @Autowired
    SubscriptionService subscriptionService;

    @RequestMapping(value = "resource/{id}", method = RequestMethod.GET)
    public String home(@PathVariable Integer id, Model model, HttpSession session) {
        Resource resource = resourceService.findById(id);
        if(resource != null) {
            Topic topic = resource.getTopic();
            Integer userId = (Integer) session.getAttribute("loggedInUser");
            User user = userId != null ? userService.findById(userId) : null;
            model.addAttribute("linkResource", new LinkResource());
            model.addAttribute("documentResource", new DocumentResource());
            List<Topic> topics1 = resourceService.findTopicsWithMaxResourcesCount();
            model.addAttribute("trendingTopics", topics1.stream()
                    .peek(t -> t.setCurrentUserSubscription(subscriptionService.findByUserAndTopic(user, t)))
                    .collect(Collectors.toList()));
            if (topic != null) {
                if (user != null) {
                    model.addAttribute("user", user);
                    model.addAttribute("subscription",
                            topic.getSubscriptions().stream()
                                    .filter(subscription -> subscription.getUser().getId().equals(user.getId()))
                                    .findFirst()
                                    .orElse(null));
                }
                if (topic.getVisibility() == Visibility.PUBLIC) {
                    model.addAttribute("resource", resource);
                    model.addAttribute("resourceType", resource instanceof LinkResource ? "LinkResource" : "DocumentResource");
                    return "Post";
                }
                if (topic.getVisibility() == Visibility.PRIVATE && user != null
                    && topic.getSubscriptions().stream().map(Subscription::getUser).anyMatch(u -> u.getId().equals(user.getId()))) {
                    model.addAttribute("resource", resource);
                    model.addAttribute("resourceType", resource instanceof LinkResource ? "LinkResource" : "DocumentResource");
                    return "Post";
                } else {
                    return "redirect:/";
                }
            }
        }
        return "404";
    }

    @RequestMapping("/download/{id}")
    public void downloadPDFResource(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @PathVariable("id") Integer id) throws FileNotFoundException {
        Resource resource = resourceService.findById(id);
        if (resource instanceof DocumentResource) {
            DocumentResource documentResource = (DocumentResource) resource;
            Path file = Paths.get(documentResource.getPath());
            if (Files.exists(file)) {
                response.addHeader("Content-Disposition", "attachment; filename=" + file.getFileName());
                try {
                    Files.copy(file, response.getOutputStream());
                    response.getOutputStream().flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                throw new FileNotFoundException();
            }

        }
    }

    public Map markAsRead(@RequestParam Integer id, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("loggedInUser");
        User user = userId != null ? userService.findById(userId) : null;
        Resource resource = id != null ? resourceService.findById(id) : null;
        Map<String, String> map = new HashMap<>();
        if (user != null && resource != null) {
            ReadingItem readingItem = new ReadingItem(user, resource);
            readingItemService.save(readingItem);
            map.put("SUCCESS", "Resource marked as read.");
        } else {
            map.put("ERROR", "No logged in user.");
        }
        return map;
    }

    @RequestMapping(value = "/updateResource", method = RequestMethod.POST)
    @ResponseBody
    public Map updateUserStatus(@RequestParam Integer resourceId, @RequestParam String description, HttpSession session){
        Integer currentUserId = (Integer) session.getAttribute("loggedInUser");
        Map<String, String> result = new HashMap<>();
        User currentUser = currentUserId != null ? userService.findById(currentUserId) : null;
        Resource resource = resourceService != null ? resourceService.findById(resourceId) : null;
        if (currentUser != null && resource != null) {
            resource.setDescription(description);
            resourceService.save(resource);
            result.put("SUCCESS", "Post Description updated.");
        } else {
            result.put("ERROR","Unauthorized Action");
        }
        return result;
    }

    @RequestMapping(value = "/deleteResource/{resourceId}", method = RequestMethod.GET)
    public String deleteResource(@PathVariable Integer resourceId, HttpSession session){
        Integer currentUserId = (Integer) session.getAttribute("loggedInUser");
        Map<String, String> result = new HashMap<>();
        User currentUser = currentUserId != null ? userService.findById(currentUserId) : null;
        Resource resource = resourceService != null ? resourceService.findById(resourceId) : null;
        if (currentUser != null && resource != null && resource.getUser().getId().equals(currentUser.getId())) {
            resourceService.delete(resource);
            return "redirect:/topic/"+resource.getTopic().getId();
        } else {
            return "redirect:/resource/"+resourceId;
        }
    }
}
