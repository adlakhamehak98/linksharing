package com.ttn.linksharing.entity;

import com.ttn.linksharing.enums.Visibility;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "user_id"}))
@EntityListeners(AuditingEntityListener.class)
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

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    public Topic(String name, User user, Visibility visibility) {
        this.name = name;
        this.user = user;
        this.visibility = visibility;
    }

    public Topic() {
    }
}
