package com.example.pushproxy.repository;

import com.example.pushproxy.model.User;
import com.example.pushproxy.model.UserDetail;

import java.util.List;

public interface UserRepository {
    User getUser(String username);
    UserDetail createUser(User user);
    List<UserDetail> getUsers();
    void incrementSendCountForUser(String username);
}
