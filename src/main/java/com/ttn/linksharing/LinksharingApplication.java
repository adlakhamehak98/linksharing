package com.ttn.linksharing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EntityScan("com.ttn.linksharing.entity")
@EnableJpaRepositories("com.ttn.linksharing.repository")
@SpringBootApplication
@EnableTransactionManagement
@EnableJpaAuditing //raises event auditentitylistener
public class LinksharingApplication {

    public static void main(String[] args) {
        SpringApplication.run(LinksharingApplication.class, args);
    }

}
