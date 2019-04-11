package com.ttn.linksharing.service;

import com.ttn.linksharing.entity.ReadingItem;
import com.ttn.linksharing.repository.ReadingItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReadingItemService {

    @Autowired
    ReadingItemRepository readingItemRepository;

    public List<ReadingItem> findUnreadResorces(boolean isRead){
        return readingItemRepository.findAllByIsRead(isRead);
    }
}
