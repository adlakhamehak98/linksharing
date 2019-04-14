package com.ttn.linksharing.repository;

import com.ttn.linksharing.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);

    User findByUsernameAndPasswordAndIsActive(String userName, String Password, Boolean isActive); //login

    List<User> findAll();

    User findById(int id);

    User findByEmail(String email);
}
