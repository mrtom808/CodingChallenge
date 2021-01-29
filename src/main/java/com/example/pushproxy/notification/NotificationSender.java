package com.example.pushproxy.notification;

import com.example.pushproxy.model.Notification;

public interface NotificationSender {
    void sendNotification(Notification notification, String apiKey);
}
