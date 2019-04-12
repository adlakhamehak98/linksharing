package com.ttn.linksharing.entity;

import lombok.Data;

import javax.persistence.Entity;
import java.util.List;


@Data
@Entity
public class LinkResource extends Resource {
    String url;

    public LinkResource(User user, Topic topic, String description, List<ResourceRating> resourceRatings, String url) {
        super(user, topic, description, resourceRatings);
        this.url = url;
    }

    public LinkResource() {

    }
}