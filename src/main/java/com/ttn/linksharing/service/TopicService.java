package com.ttn.linksharing.service;

import com.ttn.linksharing.entity.Topic;
import com.ttn.linksharing.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TopicService {
    @Autowired
    TopicRepository topicRepository;
    List<Topic> topicList = new ArrayList<>();

    public void createTopic(Topic topic){
        topicRepository.save(topic);

    }
}
