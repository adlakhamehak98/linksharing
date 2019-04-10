package com.ttn.linksharing.repository;

import com.ttn.linksharing.entity.Resource;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ResourceRepository extends CrudRepository<Resource, Integer> {
    List<Resource> findAll();

    @Query(value = "select topic_id, COUNT(id) from resource group by topic_id order by count(id) desc limit 5",nativeQuery = true)
    List<Object[]> findTopByIdOrderByTopicDesc();
}
