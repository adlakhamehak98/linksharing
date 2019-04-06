package com.ttn.linksharing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan("com.ttn.linksharing.entity")
@EnableJpaRepositories("com.ttn.linksharing.repository")
@SpringBootApplication
public class LinksharingApplication {

	public static void main(String[] args) {
		SpringApplication.run(LinksharingApplication.class, args);
	}

}
