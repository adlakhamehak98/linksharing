package com.ttn.linksharing.repository;

import com.ttn.linksharing.entity.Resource;
import org.springframework.data.repository.CrudRepository;

public interface ResourceRepository extends CrudRepository<Resource, Integer> {
}
