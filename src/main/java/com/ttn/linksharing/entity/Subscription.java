package com.ttn.linksharing.entity;

import com.ttn.linksharing.enums.Seriousness;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "topic_id"}))
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    User user;

    @ManyToOne
    Topic topic;

    @Enumerated(EnumType.STRING)
    private Seriousness seriousness;

    public Subscription(User user, Topic topic, Seriousness seriousness) {
        this.user = user;
        this.topic = topic;
        this.seriousness = seriousness;
    }

    public Subscription(){}
}