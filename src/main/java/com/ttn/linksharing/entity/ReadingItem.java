package com.ttn.linksharing.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "resource_id"}))
public class ReadingItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    private User user;

    @OneToOne
    private Resource resource;

    private Boolean isRead;
}
