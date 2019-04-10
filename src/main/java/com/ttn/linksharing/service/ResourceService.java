package com.ttn.linksharing.service;

import com.ttn.linksharing.entity.Topic;
import com.ttn.linksharing.repository.ResourceRepository;
import com.ttn.linksharing.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResourceService {
    @Autowired
    ResourceRepository resourceRepository;

    @Autowired
    TopicRepository topicRepository;

    public List<Topic> findTopicsWithMaxResourcesCount() {
        List<Object[]> topTopics = resourceRepository.findTopByIdOrderByTopicDesc();
        List<Topic> topics = topTopics.stream().map(obj -> (Integer) obj[0]).map(o -> topicRepository.findById(o).get())
                .collect(Collectors.toList());
        return topics;
    }
}
