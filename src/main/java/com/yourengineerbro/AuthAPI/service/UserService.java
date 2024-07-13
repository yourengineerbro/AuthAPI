package com.yourengineerbro.AuthAPI.service;

// UserService.java

import com.yourengineerbro.AuthAPI.entity.User;
import com.yourengineerbro.AuthAPI.exception.ResourceAlreadyExistException;
import com.yourengineerbro.AuthAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.yourengineerbro.AuthAPI.util.Constants.Messages.USER_ALREADY_EXIST;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new ResourceAlreadyExistException(USER_ALREADY_EXIST);
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
