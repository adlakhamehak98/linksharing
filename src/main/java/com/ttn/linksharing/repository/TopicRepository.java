package com.ttn.linksharing.repository;

import com.ttn.linksharing.entity.Topic;
import com.ttn.linksharing.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TopicRepository extends CrudRepository<Topic, Integer> {
    List<Topic> findAll();
    int countAllByUser(User user);
}
