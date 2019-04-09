package com.ttn.linksharing.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Data
@Entity
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    User user;

    @ManyToOne
    private Topic topic;

    private String description;

    @OneToMany(mappedBy = "resource")
    private List<ResourceRating> resourceRatings = new ArrayList<>();

    public Resource(User user, Topic topic, String description, List<ResourceRating> resourceRatings) {
        this.user = user;
        this.topic = topic;
        this.description = description;
        this.resourceRatings = resourceRatings;
    }

    public Resource() {
    }
}
