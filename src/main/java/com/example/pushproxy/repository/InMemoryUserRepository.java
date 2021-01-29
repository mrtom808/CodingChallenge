package com.example.pushproxy.repository;

import com.example.pushproxy.model.User;
import com.example.pushproxy.model.UserDetail;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryUserRepository implements UserRepository {
    private final Map<String, UserDetail> users = new ConcurrentHashMap<>();

    @Override
    public User getUser(String username) {
        return users.get(username);
    }

    @Override
    public UserDetail createUser(User user) {
        var userDetail = createUserDetail(user);
        users.put(user.getUsername(), userDetail);
        return userDetail;
    }

    @Override
    public List<UserDetail> getUsers() {
        return new ArrayList<>(users.values());
    }

    public UserDetail createUserDetail(User user) {
        var userDetail = new UserDetail();
        userDetail.setUsername(user.getUsername());
        userDetail.setAccessToken(user.getAccessToken());

        var date = LocalDateTime.now();
        var now = date.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        userDetail.setCreationTime(now);

        userDetail.setNumOfNotificationsPushed(0);
        return userDetail;
    }

    public void incrementSendCountForUser(String username) {
        Optional.ofNullable(users.get(username))
                .map(this::incrementSendCountForUser);
    }

    synchronized public UserDetail incrementSendCountForUser(UserDetail user) {
        var count = user.getNumOfNotificationsPushed();
        user.setNumOfNotificationsPushed(count + 1);
        return user;
    }
}
