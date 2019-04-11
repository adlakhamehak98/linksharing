package com.ttn.linksharing.entity;

import com.ttn.linksharing.enums.Seriousness;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "topic_id"}))
@EntityListeners(AuditingEntityListener.class)
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

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private  LocalDateTime lastModifiedDate;

    public Subscription(User user, Topic topic, Seriousness seriousness) {
        this.user = user;
        this.topic = topic;
        this.seriousness = seriousness;
    }

    public Subscription(){}
}