package com.ttn.linksharing.entity;

import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

@Data
@Entity
@DiscriminatorValue("DOCUMENT")
public class DocumentResource extends Resource {
    private String path;

    public DocumentResource(User user, Topic topic, String description, List<ResourceRating> resourceRatings, String path) {
        super(user, topic, description, resourceRatings);
        this.path = path;
    }

    public DocumentResource() {
    }
}