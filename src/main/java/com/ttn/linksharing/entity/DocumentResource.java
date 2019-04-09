package com.ttn.linksharing.entity;

import lombok.Data;

import javax.persistence.Entity;
import java.util.List;

@Data
@Entity
public class DocumentResource extends Resource {
    private String path;

    public DocumentResource(User user, Topic topic, List<ResourceRating> resourceRatings, String path) {
        super(user, topic, resourceRatings);
        this.path = path;
    }

    public  DocumentResource(){}
}