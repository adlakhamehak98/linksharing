package com.ttn.linksharing.repository;

import com.ttn.linksharing.entity.Resource;
import com.ttn.linksharing.entity.ResourceRating;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResourceRatingRepository extends CrudRepository<ResourceRating, Integer> {

    @Query(value = "Select rr.resource_id from resource_rating as rr join resource as r on rr.resource_id = r.id join topic as t on t.id = r.topic_id where t.visibility= :visibility group by rr.resource_id order by sum(rr.rating) desc limit :maxRecords", nativeQuery = true)
    List<Integer> fetchTopResources(@Param("visibility") String visibility, @Param("maxRecords") Integer limit);

    List<ResourceRating> findByResource(Resource resource);

    void deleteByResourceIn(List<Resource> resourceList);

    void deleteAllByResource(Resource resource);
}
