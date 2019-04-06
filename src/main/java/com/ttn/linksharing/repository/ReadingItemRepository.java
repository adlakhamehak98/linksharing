package com.ttn.linksharing.repository;

import com.ttn.linksharing.entity.ReadingItem;
import org.springframework.data.repository.CrudRepository;

public interface ReadingItemRepository extends CrudRepository<ReadingItem, Integer> {
}
