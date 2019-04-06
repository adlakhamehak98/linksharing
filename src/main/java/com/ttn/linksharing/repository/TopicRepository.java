package com.ttn.linksharing.repository;

import com.ttn.linksharing.entity.Topic;
import org.springframework.data.repository.CrudRepository;

public interface TopicRepository extends CrudRepository<Topic, Integer> {
}
