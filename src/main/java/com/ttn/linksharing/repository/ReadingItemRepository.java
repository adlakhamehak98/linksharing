package com.ttn.linksharing.repository;

import com.ttn.linksharing.entity.ReadingItem;
import com.ttn.linksharing.entity.Resource;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReadingItemRepository extends CrudRepository<ReadingItem, Integer> {
    List<ReadingItem> findAllByIsRead(boolean isRead);
}
