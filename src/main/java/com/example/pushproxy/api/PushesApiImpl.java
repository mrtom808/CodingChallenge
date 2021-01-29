package com.example.pushproxy.api;

import com.example.pushproxy.model.Notification;
import com.example.pushproxy.notification.PushButtonNotificationSender;
import com.example.pushproxy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PushesApiImpl implements PushesApiDelegate {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PushButtonNotificationSender pushButtonNotificationSender;

    @Override
    public ResponseEntity<Void> sendNotification(Notification body) {
        var user = userRepository.getUser(body.getUsername());
        pushButtonNotificationSender.sendNotification(body, user.getAccessToken());
        userRepository.incrementSendCountForUser(user.getUsername());
        return ResponseEntity.noContent().build();
    }
}
