package com.ttn.linksharing.service;

import com.ttn.linksharing.entity.Resource;
import com.ttn.linksharing.entity.ResourceRating;
import com.ttn.linksharing.repository.ResourceRatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ResourceRatingService {

    @Autowired
    ResourceRatingRepository resourceRatingRepository;

    @Transactional
    public void deleteRatingList(List<Resource> resourceList){
        resourceRatingRepository.deleteByResourceIn(resourceList);
    }
}