package com.ttn.linksharing.service;

import com.ttn.linksharing.entity.User;
import com.ttn.linksharing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public void saveUser(User responseData)
    {
        userRepository.save(responseData);
    }

    public List<User> checkUser(User responseData) {

        return userRepository.findByUsername(responseData.getUsername());
    }

    public User validateUser(User responseData) {
        User loginStatus = userRepository.findByUsernameAndPassword(responseData.getUsername(), responseData.getPassword());
        return loginStatus;

    }
}
