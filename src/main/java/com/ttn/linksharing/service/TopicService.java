package com.ttn.linksharing.service;

import com.ttn.linksharing.entity.Topic;
import com.ttn.linksharing.entity.User;
import com.ttn.linksharing.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TopicService {
    @Autowired
    TopicRepository topicRepository;

    public void createTopic(Topic topic){
        topicRepository.save(topic);
    }

    public int topicsCount(User user){
        return topicRepository.countAllByUser(user);
    }

    public Topic findTopicById(int id){
        return topicRepository.findById(id);
    }

    @Transactional
    public void deleteTopic(Topic topic){
        topicRepository.delete(topic);
    }
}
