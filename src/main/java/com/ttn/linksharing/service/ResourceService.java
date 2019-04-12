package com.ttn.linksharing.service;

import com.ttn.linksharing.entity.Resource;
import com.ttn.linksharing.entity.Topic;
import com.ttn.linksharing.enums.Visibility;
import com.ttn.linksharing.repository.ResourceRatingRepository;
import com.ttn.linksharing.repository.ResourceRepository;
import com.ttn.linksharing.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResourceService {
    @Autowired
    ResourceRepository resourceRepository;

    @Autowired
    ResourceRatingRepository resourceRatingRepository;

    @Autowired
    TopicRepository topicRepository;

    public List<Topic> findTopicsWithMaxResourcesCount() {
        List<Object[]> topTopics = resourceRepository.findTopByIdOrderByTopicDesc();
        List<Topic> topics = topTopics.stream().map(obj -> (Integer) obj[0]).map(o -> topicRepository.findById(o).get())
                .collect(Collectors.toList());
        return topics;
    }

    public List<Resource> fetchLatestFivePublicResources() {
        return resourceRepository.findRecentByTopicVisiblity(Visibility.PUBLIC.name(), 5);
    }

    public List<Resource> fetchTopFivePublicResources() {
        List<Integer> resourceIds = resourceRatingRepository.fetchTopResources(Visibility.PUBLIC.name(), 5);
        return resourceRepository.findAllByIdIn(resourceIds);
    }

    public List<Resource> findByTopic(Topic topic) {
        return resourceRepository.findByTopic(topic);
    }

    @Transactional
    public void deleteResources(List<Resource> resourceList) {
        resourceRepository.deleteAll(resourceList);
    }

    public Resource findById(Integer id) {
        Optional<Resource> resourceOptional = resourceRepository.findById(id);
        return resourceOptional.orElse(null);
    }
}
