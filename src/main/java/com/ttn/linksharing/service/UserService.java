package com.ttn.linksharing.service;

import com.ttn.linksharing.entity.User;
import com.ttn.linksharing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public void saveUser(User responseData) {
        userRepository.save(responseData);
    }

    public User checkUser(String username) {
        return userRepository.findByUsername(username);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findById(int id) {
        return userRepository.findById(id);
    }

    public User validateUser(User responseData) {
        User loginStatus = userRepository.findByUsernameAndPassword(responseData.getUsername(), responseData.getPassword());
        return loginStatus;
    }

    public String storeProfilePic(MultipartFile profilePic) {
        try (InputStream inputStream = profilePic.getInputStream()) {
            if (profilePic.getContentType().split("/")[0].equals("image")) {
                String filename = StringUtils.cleanPath(profilePic.getOriginalFilename());
                Files.copy(inputStream, Paths.get("/home/ttn/project/uploads/images").resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
                return filename;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
