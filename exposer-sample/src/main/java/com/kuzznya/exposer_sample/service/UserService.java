package com.kuzznya.exposer_sample.service;

import com.kuzznya.exposer_sample.model.User;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Service
public class UserService {

    private final Map<String, User> users = new LinkedHashMap<>();

    public void createUser(User user) {
        users.put(user.getUsername(), user);
    }

    public User getUser(String username) {
        return users.get(username);
    }

    public User findUserByEmail(Set<String> emails) {
        return users.values()
                .stream()
                .filter(user -> emails.contains(user.getEmail()))
                .findAny()
                .orElseThrow();
    }

    public void deleteUser(String username) {
        users.remove(username);
    }
}
