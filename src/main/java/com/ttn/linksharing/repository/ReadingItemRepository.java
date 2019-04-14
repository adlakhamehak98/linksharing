package com.ttn.linksharing.repository;

import com.ttn.linksharing.entity.ReadingItem;
import com.ttn.linksharing.entity.Resource;
import com.ttn.linksharing.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReadingItemRepository extends CrudRepository<ReadingItem, Integer> {
    List<ReadingItem> findAllByIsReadAndUser(Boolean isRead, User user);

    List<ReadingItem> findByResourceIn(List<Resource> resources);


    void deleteByResourceIn(List<Resource> resourceList);

    void deleteAllByResource(Resource resource);
}
