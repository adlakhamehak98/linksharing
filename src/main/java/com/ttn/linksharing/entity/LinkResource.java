package com.ttn.linksharing.entity;

import lombok.Data;

import javax.persistence.Entity;


@Data
@Entity
public class LinkResource extends Resource {
    String url;
}