package com.ttn.linksharing.repository;

import com.ttn.linksharing.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);

    User findByUsernameAndPassword(String userName, String Password); //login

    List<User> findAll();

    User findById(int id);

    User findByEmail(String email);
}
