package com.ttn.linksharing.repository;

import com.ttn.linksharing.entity.Resource;
import com.ttn.linksharing.entity.Topic;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResourceRepository extends CrudRepository<Resource, Integer> {
    List<Resource> findAll();

    @Query(value = "select topic_id, COUNT(id) from resource group by topic_id order by count(id) desc limit 5",nativeQuery = true)
    List<Object[]> findTopByIdOrderByTopicDesc();

    @Query(value = "SELECT res.* FROM resource AS res JOIN topic AS t ON res.topic_id = t.id WHERE t.visibility = :visibility ORDER BY res.created_date DESC LIMIT :maxRecords", nativeQuery = true)
    List<Resource> findRecentByTopicVisiblity(@Param("visibility") String visibility, @Param("maxRecords") Integer limit);

    List<Resource> findAllByIdIn(List<Integer> ids);

    List<Resource> findByTopic(Topic topic);
}
