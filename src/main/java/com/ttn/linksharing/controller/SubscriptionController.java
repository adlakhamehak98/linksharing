package com.ttn.linksharing.controller;

import com.ttn.linksharing.entity.Subscription;
import com.ttn.linksharing.enums.Seriousness;
import com.ttn.linksharing.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class SubscriptionController {

    @Autowired
    SubscriptionService subscriptionService;

    @RequestMapping(value = "/updateSeriousness", method = RequestMethod.POST)
    @ResponseBody
    public Map updateSeriousness(@RequestParam int subscriptionId, @RequestParam Seriousness seriousness, HttpSession session) throws Exception {
        Integer userId = (Integer) session.getAttribute("loggedInUser");
        Map<String,String> map = new HashMap<>();
        if (userId != null) {
            Subscription subscription = subscriptionService.findByid(subscriptionId);
            subscription.setSeriousness(seriousness);
            subscriptionService.saveSubscription(subscription);
            map.put("SUCCESS","Seriousness Updated Successfully");
        }
        map.put("ERROR","No Logged In User");
        return map;
    }
}
