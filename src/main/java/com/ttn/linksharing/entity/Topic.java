package com.ttn.linksharing.entity;

import com.ttn.linksharing.enums.Visibility;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "user_id"}))
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToOne
    User user;

    @Enumerated(EnumType.STRING)
    Visibility visibility;

    @OneToMany(mappedBy = "topic")
    private List<Resource> resource;

    @OneToMany(mappedBy = "topic")
    private List<Subscription> subscriptions;

    public Topic(String name, User user, Visibility visibility) {
        this.name = name;
        this.user = user;
        this.visibility = visibility;
    }

    public Topic() {
    }
}
