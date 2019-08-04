package com.sha.microserviceusermanagement.service;

import com.sha.microserviceusermanagement.model.User;

import java.util.List;

public interface UserService {
    User save(User user);

    User findByUsername(String username);

    List<User> findUsers(List<Long> idList);
}
