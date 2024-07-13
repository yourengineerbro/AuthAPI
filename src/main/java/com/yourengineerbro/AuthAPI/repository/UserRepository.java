package com.yourengineerbro.AuthAPI.repository;

// UserRepository.java

import com.yourengineerbro.AuthAPI.entity.User;
import org.springframework.stereotype.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
