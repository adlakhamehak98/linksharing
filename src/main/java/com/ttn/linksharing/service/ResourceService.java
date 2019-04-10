package com.ttn.linksharing.service;

import com.ttn.linksharing.entity.Resource;
import com.ttn.linksharing.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceService {
    @Autowired
    ResourceRepository resourceRepository;

    public List<Resource> findTopicsWithMaxResourcesCount(){
        return resourceRepository.findTopByIdOrderByTopicDesc();
    }
}
