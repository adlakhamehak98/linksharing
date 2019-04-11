package com.ttn.linksharing.controller;

import com.ttn.linksharing.entity.DocumentResource;
import com.ttn.linksharing.entity.LinkResource;
import com.ttn.linksharing.entity.Resource;
import com.ttn.linksharing.entity.Subscription;
import com.ttn.linksharing.entity.Topic;
import com.ttn.linksharing.entity.User;
import com.ttn.linksharing.enums.Visibility;
import com.ttn.linksharing.service.DocumentResourceService;
import com.ttn.linksharing.service.LinkResourceService;
import com.ttn.linksharing.service.TopicService;
import com.ttn.linksharing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TopicController {

	@Autowired
	TopicService topicService;

	@Autowired
	UserService userService;

	@Autowired
	LinkResourceService linkResourceService;

	@Autowired
	DocumentResourceService documentResourceService;

	@RequestMapping(value = "topic/{id}", method = RequestMethod.GET)
	public String home(@PathVariable Integer id, Model model, HttpSession session) {
		Topic topic = topicService.findTopicById(id);
		Integer userId = (Integer) session.getAttribute("loggedInUser");
		User user = userId != null ? userService.findById(userId) : null;
		model.addAttribute("linkResource", new LinkResource());
		model.addAttribute("documentResource", new DocumentResource());
		if(topic != null){
			if ( user != null) {
				model.addAttribute("user", user);
				model.addAttribute("subscription",
						topic.getSubscriptions().stream()
								.filter(subscription -> subscription.getUser().getId().equals(user.getId()))
								.findFirst()
								.orElse(null));
			}
			if(topic.getVisibility() == Visibility.PUBLIC){
				model.addAttribute("topic", topic);
				return "Topic";
			}
			if(topic.getVisibility() == Visibility.PRIVATE && user != null
			   && topic.getSubscriptions().stream().map(Subscription::getUser).anyMatch(u -> u.getId().equals(user.getId()))){
				model.addAttribute("topic", topic);
				return "Topic";
			} else {
				return "redirect:/";
			}
		}
		return "404";
	}

	@RequestMapping(value = "/topic/shareLink", method = RequestMethod.POST)
	public String shareLink(@ModelAttribute("linkResource") LinkResource resource, @ModelAttribute("subscription") Subscription subscription, ModelMap model, HttpSession session) {
		Integer userId = (Integer) session.getAttribute("loggedInUser");
		if (userId != null) {
			User user = userService.findById(userId);
			resource.setUser(user);
			linkResourceService.shareLink(resource);
			model.addAttribute("linkResource", resource);
			return "redirect:/topic/"+resource.getTopic().getId();
		}
		return "redirect:/";
	}

	@RequestMapping(value = "/topic/shareDocument", method = RequestMethod.POST)
	public String shareDocument(@RequestParam MultipartFile file, @ModelAttribute("documentResource") DocumentResource resource, ModelMap model, BindingResult bindingResult, HttpSession session) {
		Integer userId = (Integer) session.getAttribute("loggedInUser");
		if (userId != null) {
			if (file == null || file.isEmpty()) {
				bindingResult.addError(new FieldError("documentResource", "path", "File can't be null."));
				if (bindingResult.hasErrors()) {
					return "redirect:/topic/"+resource.getTopic().getId();
			}
			} else {
				User user = userService.findById(userId);
				resource.setUser(user);
				String filePath = documentResourceService.storeDocument(file);
				if (filePath != null) {
					resource.setPath(filePath);
					documentResourceService.shareDocument(resource);
					return "redirect:/topic/"+resource.getTopic().getId();
				} else {
					bindingResult.addError(new FieldError("documentResource", "path", "File can't be uploaded."));
					return "redirect:/topic/"+resource.getTopic().getId();
				}
			}
		}
		return "redirect:/";
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
}
