package com.example.pushproxy.api;

import com.example.pushproxy.model.User;
import com.example.pushproxy.model.UserDetail;
import com.example.pushproxy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserApiImpl implements UsersApiDelegate {
    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<List<UserDetail>> getAllUsers() {
        return ResponseEntity.ok(userRepository.getUsers());
    }

    @Override
    public ResponseEntity<UserDetail> createUser(User body) {
        var user = userRepository.createUser(body);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
