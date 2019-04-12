package com.ttn.linksharing.service;

import com.ttn.linksharing.entity.ReadingItem;
import com.ttn.linksharing.entity.Resource;
import com.ttn.linksharing.entity.User;
import com.ttn.linksharing.repository.ReadingItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReadingItemService {

    @Autowired
    ReadingItemRepository readingItemRepository;

    public List<ReadingItem> findUnreadResourcesPerUser(Boolean isRead, User user) {
        return readingItemRepository.findAllByIsReadAndUser(isRead, user);
    }

    public void deleteReadingItemPerUser(List<Resource> resources){
        List<ReadingItem> readingItemList = readingItemRepository.findByResourceIn(resources);
        readingItemRepository.deleteAll(readingItemList);
    }
}
