package com.ttn.linksharing.repository;

import com.ttn.linksharing.entity.Resource;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ResourceRepository extends CrudRepository<Resource, Integer> {
    List<Resource> findAll();
}
