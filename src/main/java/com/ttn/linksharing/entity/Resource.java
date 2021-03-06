package com.ttn.linksharing.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Data
@Entity
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "resource_type")
@EntityListeners(AuditingEntityListener.class)
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    User user;

    @ManyToOne
    private Topic topic;

    @Column(name = "resource_type", insertable = false, updatable = false)
    private String resourceType;

    private String description;

    @OneToMany(mappedBy = "resource")
    private List<ResourceRating> resourceRatings = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @Transient
    private String resourceUrl;

    public Resource(User user, Topic topic, String description, List<ResourceRating> resourceRatings) {
        this.user = user;
        this.topic = topic;
        this.description = description;
        this.resourceRatings = resourceRatings;
    }

    public Resource() {
    }

    public String getResourceUrl() {
        if (this instanceof LinkResource) {
            LinkResource linkResource = (LinkResource) this;
            return linkResource.getUrl();
        } else {
            DocumentResource documentResource = (DocumentResource) this;
            return documentResource.getPath();
        }

    }
}
